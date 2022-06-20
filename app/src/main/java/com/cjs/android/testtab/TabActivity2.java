package com.cjs.android.testtab;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cjs.android.testtab.model.TabChild;
import com.cjs.android.testtab.model.TabGroup;
import com.cjs.android.testtab.recycler.TabRecyclerAdapter;
import com.cjs.android.testtab.recycler.TabRecyclerChildAdapter;
import com.cjs.android.testtab.recycler.TabRecyclerHeaderAdapter;
import com.cjs.android.testtab.recycler.drag.EmptyCallBack;
import com.cjs.android.testtab.recycler.drag.GridSortHelperCallBack;
import com.cjs.android.testtab.recycler.drag.VerticalDragSortHelperCallBack;
import com.cjs.android.testtab.utils.DensityUtil;
import com.cjs.android.testtab.utils.Dummy;
import com.cjs.android.testtab.utils.MenuParser;
import com.cjs.android.testtab.utils.ResourceUtil;
import com.donkingliang.consecutivescroller.ConsecutiveScrollerLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class TabActivity2 extends AppCompatActivity {
    private final MenuParser menuParser = new MenuParser(this, false);
    private RecyclerView recycler_list;
    private TabLayout tabLayout;
    private TabRecyclerAdapter recyclerListAdapter;
    private LinearLayoutManager groupLayoutManager;
    private List<TabGroup> tabGroupList;

    //判读是否是recyclerView主动引起的滑动，true- 是，false- 否，由tablayout引起的
    private boolean isRecyclerScroll;
    //记录上一次位置，防止在同一内容块里滑动 重复定位到tablayout
    private int lastPos;
    //用于recyclerView滑动到指定的位置
    private boolean canScroll;
    private int scrollToPosition;

    private ConsecutiveScrollerLayout scrollerLayout;
    private LinearLayout header_edit;
    private Button btn_edit;
    private RecyclerView recycler_header;
    private List<TabChild> headerList = new ArrayList<>();
    private TabRecyclerHeaderAdapter recyclerHeaderAdapter;

    private boolean isEditMode = false;

    private GridSortHelperCallBack gridSortHelperCallBack;

    private Activity activity;

    /**
     * 头部最大选择个数
     */
    private int maxHeaderNum = 8;
    /**
     * 每行显示最大个数
     */
    private int layoutColumnCount = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab2);

        recycler_list = findViewById(R.id.recycler_list);
        tabLayout = findViewById(R.id.tabLayout);
        btn_edit = findViewById(R.id.btn_edit);
        recycler_header = findViewById(R.id.recycler_header);
        scrollerLayout = findViewById(R.id.scrollerLayout);
        header_edit = findViewById(R.id.header_edit);

        tabGroupList = menuParser.getServiceHallList();

        initRecyclerHeader();
        initTabLayout();
        initRecyclerList();
    }

    private void initRecyclerHeader() {
        headerList = menuParser.getMyCommonList(tabGroupList);
        TabGroup group = new TabGroup();
        group.setGroupTitle("header");
        group.setChildList(headerList);
        recyclerHeaderAdapter = new TabRecyclerHeaderAdapter(group);
        recyclerHeaderAdapter.setMaxHeaderNum(maxHeaderNum);
        recyclerHeaderAdapter.setOnChildItemClickListener(new OnHeaderItemClickListener());
        recycler_header.setLayoutManager(new GridLayoutManager(this, layoutColumnCount));
        recycler_header.setAdapter(recyclerHeaderAdapter);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchMode();
            }
        });

    }

    /**
     * 切换模式 编辑<=>保存
     */
    private void switchMode() {
        isEditMode = !isEditMode;
        ConsecutiveScrollerLayout.LayoutParams lp = (ConsecutiveScrollerLayout.LayoutParams) header_edit.getLayoutParams();
        initDraggable();
        if (isEditMode) {
            // 编辑模式
            btn_edit.setText("保存");
            btn_edit.setBackgroundResource(R.drawable.selector_btn_save);
            btn_edit.setTextColor(ResourceUtil.getColor(this, R.color.red));
            scrollerLayout.setPermanent(true); // 设置成true可以开启布局吸顶常驻 然后可以和多个layout_sticky为true的布局进行叠加吸顶
            lp.isSticky = true; // 设置成true可以将该布局标记为吸顶布局
            recyclerHeaderAdapter.addPlaceholder();
            recyclerHeaderAdapter.changeCornerStatus(TabChild.CORNER_SUB);
            gridSortHelperCallBack.setDragEnable(true);
            recyclerListAdapter.changeChildCornerStatus(TabChild.CORNER_ADD, false);
            recyclerListAdapter.changeClickedChildCornerStatus(headerList, TabChild.CORNER_CORRECT, true);
        } else {
            // 静态回显
            btn_edit.setText("编辑");
            btn_edit.setBackgroundResource(R.drawable.selector_btn_edit);
            btn_edit.setTextColor(ResourceUtil.getColor(this, R.color.blue));
            scrollerLayout.setPermanent(false);
            lp.isSticky = false;
            recyclerHeaderAdapter.removePlaceholder();
            recyclerHeaderAdapter.changeCornerStatus(TabChild.CORNER_NONE);
            // 移除拖拽模式
            new ItemTouchHelper(new EmptyCallBack()).attachToRecyclerView(recycler_header);
            gridSortHelperCallBack.setDragEnable(false);
            recyclerListAdapter.changeChildCornerStatus(TabChild.CORNER_NONE, true);
        }
        header_edit.setLayoutParams(lp);
        recyclerListAdapter.setLastH(getLastH());
        recycler_list.requestLayout();
    }

    /**
     * 对Header初始化拖拽功能
     */
    private void initDraggable() {
        if (gridSortHelperCallBack == null) {
            gridSortHelperCallBack = new GridSortHelperCallBack(headerList);
            // 切换为可以拖拽的模式
            gridSortHelperCallBack.setOnDragListener(new VerticalDragSortHelperCallBack.OnDragListener() {
                @Override
                public void onItemMove(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target, int fromPos, int toPos) {

                }

                @Override
                public void onItemMoved(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target, int fromPos, int toPos) {

                }
            });
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(gridSortHelperCallBack);
            itemTouchHelper.attachToRecyclerView(recycler_header);
        }
    }

    private int getLastH() {
        //计算内容块所在的高度，全屏高度-状态栏高度-tablayout的高度(这里固定高度50dp)-标题栏高度，用于recyclerView的最后一个item view填充高度
        int screenH = DensityUtil.getScreenHeight(this);
        int statusBarH = DensityUtil.getStatusBarHeight(this);
        int tabH = DensityUtil.dpToPx(this, 50);
        int titleBarH = DensityUtil.dpToPx(this, 50);
        int diff = DensityUtil.dpToPx(this, 20);
        int lastH = 0;
        Log.d("LastH_", String.format("%1$s %2$s %3$s %4$s", screenH, statusBarH, tabH, titleBarH));
        if (isEditMode) {
            int headH = header_edit.getMeasuredHeight();
            lastH = screenH - statusBarH - tabH - titleBarH - headH + diff;
        } else {
            lastH = screenH - statusBarH - tabH - titleBarH + diff;
        }

        Log.d("LastH", lastH + "");
        return lastH;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initRecyclerList() {
        groupLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        groupLayoutManager.setAutoMeasureEnabled(true);
        recycler_list.setLayoutManager(groupLayoutManager);
        recyclerListAdapter = new TabRecyclerAdapter(tabGroupList, getLastH(), new OnListItemClickListener());
        recyclerListAdapter.setLayoutColumnCount(layoutColumnCount);
        recyclerListAdapter.setHasStableIds(true);
        recycler_list.setAdapter(recyclerListAdapter);

        recycler_list.setOnTouchListener((v, event) -> {
            //当滑动由recyclerView触发时，isRecyclerScroll 置true
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                isRecyclerScroll = true;
            }
            return false;
        });

        recycler_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (canScroll) {
                    canScroll = false;
                    moveToPosition(groupLayoutManager, recyclerView, scrollToPosition);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                if (isRecyclerScroll) {
                //第一个可见的view的位置，即tablayout需定位的位置
                int position = groupLayoutManager.findFirstVisibleItemPosition();
                if (lastPos != position) {
                    tabLayout.setScrollPosition(position, 0, true);
                }
                lastPos = position;
//                }
            }
        });
    }

    private void initTabLayout() {
        for (TabGroup group : tabGroupList) {
            tabLayout.addTab(tabLayout.newTab().setText(group.getGroupTitle()));
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //点击标签，使recyclerView滑动，isRecyclerScroll置false
                int pos = tab.getPosition();
                isRecyclerScroll = false;
                moveToPosition(groupLayoutManager, recycler_list, pos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void moveToPosition(LinearLayoutManager manager, RecyclerView mRecyclerView, int position) {
        // 第一个可见的view的位置
        int firstItem = manager.findFirstVisibleItemPosition();
        // 最后一个可见的view的位置
        int lastItem = manager.findLastVisibleItemPosition();
        if (position <= firstItem) {
            // 如果跳转位置firstItem 之前(滑出屏幕的情况)，就smoothScrollToPosition可以直接跳转，
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 跳转位置在firstItem 之后，lastItem 之间（显示在当前屏幕），smoothScrollBy来滑动到指定位置
            int top = mRecyclerView.getChildAt(position - firstItem).getTop();
            mRecyclerView.smoothScrollBy(0, top);
        } else {
            // 如果要跳转的位置在lastItem 之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用当前moveToPosition方法，执行上一个判断中的方法
            mRecyclerView.smoothScrollToPosition(position);
            scrollToPosition = position;
            canScroll = true;
        }
    }

    /**
     * 头部元素点击监听器
     *
     * @author JasonChen
     * @email chenjunsen@outlook.com
     * @date 2022/6/15 10:26
     */
    private class OnHeaderItemClickListener implements TabRecyclerChildAdapter.OnChildItemClickListener {

        @Override
        public void onChildItemClick(TabGroup group, TabChild child, int position) {
            if (isEditMode) {
                //编辑模式下
                //1、占位区无法点击
                if (child.getItemStatus() != TabChild.STATUS_PLACEHOLDER) {
                    //2、移除当前点击的元素 同时占位区补上一个空格
                    recyclerHeaderAdapter.removeChild(position);
                    //3、列表区对应的元素改变角标状态为加号
                    TabRecyclerChildAdapter childAdapter = recyclerListAdapter.getChildAdapterMap().get(child.getGroupId());
                    childAdapter.changeCornerStatus(TabChild.CORNER_ADD, child); // 这里只是改变对应元素的角标状态，不用clone
                }
            } else {
                Toast.makeText(activity, group.getGroupTitle() + " " + child, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 列表元素点击监听器
     *
     * @author JasonChen
     * @email chenjunsen@outlook.com
     * @date 2022/6/15 10:27
     */
    private class OnListItemClickListener implements TabRecyclerChildAdapter.OnChildItemClickListener {

        @Override
        public void onChildItemClick(TabGroup group, TabChild child, int position) {
            if (isEditMode) {
                //编辑模式下
                if (child.getCorner() == TabChild.CORNER_CORRECT) {
                    //1、已选择模块提示不能选择
                    Toast.makeText(activity, "模块已添加", Toast.LENGTH_SHORT).show();
                } else if (child.getCorner() == TabChild.CORNER_ADD) {
                    if (recyclerHeaderAdapter.getNonePlaceholderCount() == maxHeaderNum) {
                        Toast.makeText(activity, "最多只能添加" + maxHeaderNum + "个模块", Toast.LENGTH_SHORT).show();
                    } else {
                        //2、列表区对应元素更改角标状态为不可添加
                        TabRecyclerChildAdapter childAdapter = recyclerListAdapter.getChildAdapterMap().get(group.getId());
                        childAdapter.changeCornerStatus(TabChild.CORNER_CORRECT, child, position);
                        //3、头部区增加对应元素 增加模式为替换第一个占位格子
                        recyclerHeaderAdapter.addChild(child.clone(), position); // 这里涉及到了header的数组改变，而上下两个list是相互独立的，需要clone
                    }
                }
            } else {
                Toast.makeText(activity, group.getGroupTitle() + " " + child, Toast.LENGTH_SHORT).show();
                Log.d("ddd", child.getTips() + "");
            }
        }
    }

}
