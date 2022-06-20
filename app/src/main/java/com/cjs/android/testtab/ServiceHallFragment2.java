package com.cjs.android.testtab;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.cjs.android.testtab.addtion.SimpleCardFragment;
import com.cjs.android.testtab.model.TabChild;
import com.cjs.android.testtab.model.TabGroup;
import com.cjs.android.testtab.recycler.TabRecyclerAdapter;
import com.cjs.android.testtab.recycler.TabRecyclerChildAdapter;
import com.cjs.android.testtab.recycler.TabRecyclerHeaderAdapter;
import com.cjs.android.testtab.recycler.drag.EmptyCallBack;
import com.cjs.android.testtab.recycler.drag.GridSortHelperCallBack;
import com.cjs.android.testtab.recycler.drag.VerticalDragSortHelperCallBack;
import com.cjs.android.testtab.utils.DensityUtil;
import com.cjs.android.testtab.utils.MenuParser;
import com.cjs.android.testtab.utils.ResourceUtil;
import com.donkingliang.consecutivescroller.ConsecutiveScrollerLayout;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务大厅 采用第三方的SlidingTabLayout实现 (完美实现)
 *
 * @author JasonChen
 * @email chenjunsen@outlook.com
 * @date 2022/6/16 16:18
 */
public class ServiceHallFragment2 extends Fragment {
    private MenuParser menuParser;
    private RecyclerView recycler_list;
    private SlidingTabLayout tabLayout;
    private TabRecyclerAdapter recyclerListAdapter;
    private LinearLayoutManager groupLayoutManager;
    private List<TabGroup> tabGroupList;

    private ConsecutiveScrollerLayout scrollerLayout;
    private LinearLayout header_edit;
    private Button btn_edit;
    private RecyclerView recycler_header;
    private List<TabChild> headerList = new ArrayList<>();
    private TabRecyclerHeaderAdapter recyclerHeaderAdapter;

    private boolean isEditMode = false; // 当前是否处于编辑模式

    private GridSortHelperCallBack gridSortHelperCallBack;


    private FragmentActivity activity;
    //联动SlidingTabLayout的viewpager 实际并无实质作用
    private ViewPager vp;

    /**
     * 头部最大选择个数
     */
    private int maxHeaderNum = 8;
    /**
     * 每行显示最大个数
     */
    private int layoutColumnCount = 4;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (FragmentActivity) context;
        menuParser = new MenuParser(context, false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_service_hall2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycler_list = findViewById(R.id.recycler_list);
        tabLayout = findViewById(R.id.tabLayout);
        btn_edit = findViewById(R.id.btn_edit);
        recycler_header = findViewById(R.id.recycler_header);
        scrollerLayout = findViewById(R.id.scrollerLayout);
        header_edit = findViewById(R.id.header_edit);
        vp = findViewById(R.id.vp);

        tabGroupList = menuParser.getServiceHallList();

        initRecyclerHeader();
        initTabLayout();
        initRecyclerList();
    }

    private <T extends View> T findViewById(@IdRes int id) {
        return activity.findViewById(id);
    }

    private void initRecyclerHeader() {
        headerList = menuParser.getMyCommonList(tabGroupList);
        TabGroup group = new TabGroup();
        group.setGroupTitle("header");
        group.setChildList(headerList);
        recyclerHeaderAdapter = new TabRecyclerHeaderAdapter(group);
        recyclerHeaderAdapter.setMaxHeaderNum(maxHeaderNum);
        recyclerHeaderAdapter.setOnChildItemClickListener(new ServiceHallFragment2.OnHeaderItemClickListener());
        recycler_header.setLayoutManager(new GridLayoutManager(activity, layoutColumnCount));
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
            btn_edit.setTextColor(ResourceUtil.getColor(activity, R.color.red));
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
            btn_edit.setTextColor(ResourceUtil.getColor(activity, R.color.blue));
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
        int screenH = DensityUtil.getScreenHeight(activity);
        int statusBarH = DensityUtil.getStatusBarHeight(activity);
        int tabH = DensityUtil.dpToPx(activity, 50);
        int titleBarH = DensityUtil.dpToPx(activity, 50);
        int bottomBarH = DensityUtil.dpToPx(activity, 50);
//        int diff = DensityUtil.dpToPx(activity, 4);
        int diff = 1;
        int lastH = 0;
        Log.d("LastH_", String.format("%1$s %2$s %3$s %4$s", screenH, statusBarH, tabH, titleBarH));
        if (isEditMode) {
            int headH = header_edit.getMeasuredHeight();
            lastH = screenH - statusBarH - tabH - titleBarH - headH - bottomBarH + diff;
        } else {
            lastH = screenH - statusBarH - tabH - titleBarH + diff;
        }

        Log.d("LastH", lastH + "");
        return lastH;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initRecyclerList() {
        groupLayoutManager = new LinearLayoutManager(activity, RecyclerView.VERTICAL, false);
        groupLayoutManager.setAutoMeasureEnabled(true);
        recycler_list.setLayoutManager(groupLayoutManager);
        recyclerListAdapter = new TabRecyclerAdapter(tabGroupList, getLastH(), new ServiceHallFragment2.OnListItemClickListener());
        recyclerListAdapter.setLayoutColumnCount(layoutColumnCount);
        recyclerListAdapter.setHasStableIds(true);
        recycler_list.setAdapter(recyclerListAdapter);


        recycler_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int position = groupLayoutManager.findFirstVisibleItemPosition();
                tabLayout.setCurrentTab(position);
            }
        });
    }

    private void initTabLayout() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        for (TabGroup group : tabGroupList) {
            fragments.add(SimpleCardFragment.getInstance(group.getGroupTitle()));
        }
        vp.setAdapter(new MyPagerAdapter(activity.getSupportFragmentManager(), fragments));
        tabLayout.setViewPager(vp);
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                // 关键步骤1 Tab切换的时候需要将Tab置顶 否则RecyclerView不能滑动 同时不能完全置顶 需要预留一些像素的位置 否则也不能滑动
                scrollerLayout.scrollToChildWithOffset(tabLayout, -5);
                // 关键步骤2 不能调用RecyclerView的scrollToPosition 只能调用LayoutManager的scrollTo 否则会出现滑不准
                groupLayoutManager.scrollToPositionWithOffset(position, 0);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
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

    private class MyPagerAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> fragments;

        public MyPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabGroupList.get(position).getGroupTitle();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }
    }
}
