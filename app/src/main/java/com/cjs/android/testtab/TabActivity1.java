package com.cjs.android.testtab;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.cjs.android.testtab.addtion.SimpleCardFragment;
import com.cjs.android.testtab.model.TabChild;
import com.cjs.android.testtab.model.TabGroup;
import com.cjs.android.testtab.recycler.TabRecyclerAdapter;
import com.cjs.android.testtab.recycler.TabRecyclerChildAdapter;
import com.cjs.android.testtab.utils.DensityUtil;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class TabActivity1 extends AppCompatActivity implements TabRecyclerChildAdapter.OnChildItemClickListener, OnTabSelectListener {
    private SlidingTabLayout tabLayout, tabLayout_;
    private RecyclerView recyclerView;
    private LinearLayoutManager groupLayoutManager;
    private ViewPager vp;

    //判读是否是recyclerView主动引起的滑动，true- 是，false- 否，由tablayout引起的
    private boolean isRecyclerScroll;
    //记录上一次位置，防止在同一内容块里滑动 重复定位到tablayout
    private int lastPos;
    //用于recyclerView滑动到指定的位置
    private boolean canScroll;
    private int scrollToPosition;

    private static final String[] titles = new String[]{"行前准备", "机场相关", "售后服务", "旅行预定", "餐食服务", "其他"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvents();
    }

    private void initView() {
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout_ = findViewById(R.id.tabLayout_);
        recyclerView = findViewById(R.id.recyclerView);
        vp = findViewById(R.id.vp);
    }

    private void initEvents() {
        initTabLayout();
        initRecyclerView();
    }

    private void initTabLayout() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        for (String title : titles) {
            fragments.add(SimpleCardFragment.getInstance(title));
        }
        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), fragments));
        tabLayout.setViewPager(vp, titles);
        tabLayout.setOnTabSelectListener(this);
        tabLayout_.setViewPager(vp, titles);
        tabLayout_.setOnTabSelectListener(this);
    }

    private void initRecyclerView() {
        List<TabGroup> tabGroupList = new ArrayList<>();
        Random random = new Random();
        AtomicInteger counter = new AtomicInteger();

        for (int i = 0; i < titles.length; i++) {
            String title = titles[i];
            TabGroup tabGroup = new TabGroup();
            tabGroup.setViewType(TabRecyclerAdapter.VIEW_TYPE_HEADER);
            tabGroup.setGroupTitle(title);
            tabGroup.setShowGroupTitle(i != 0);
            List<TabChild> tabChildList = new ArrayList<>();
            int num = random.nextInt(10) + 1;
            Log.d("RRRR", "随机数:" + num);
            for (int j = 0; j < num; j++) {
                tabChildList.add(new TabChild(counter.incrementAndGet(), i, "标签" + j));
            }
            tabGroup.setChildList(tabChildList);
            tabGroupList.add(tabGroup);
        }
        //计算内容块所在的高度，全屏高度-状态栏高度-tablayout的高度(这里固定高度50dp)-标题栏高度，用于recyclerView的最后一个item view填充高度
        int screenH = DensityUtil.getScreenHeight(this);
        int statusBarH = DensityUtil.getStatusBarHeight(this);
        int tabH = DensityUtil.dpToPx(this, 50);
        int titleBarH = DensityUtil.dpToPx(this, 50);
        int lastH = screenH - statusBarH - tabH;
        groupLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(groupLayoutManager);
        TabRecyclerAdapter tabRecyclerAdapter = new TabRecyclerAdapter(tabGroupList, lastH,this);
        recyclerView.setAdapter(tabRecyclerAdapter);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //当滑动由recyclerView触发时，isRecyclerScroll 置true
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    isRecyclerScroll = true;
                }
                return false;
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                Log.d("OnScrolled", "dy：" + dy);
                if (isRecyclerScroll) {
                    //第一个可见的view的位置，即tablayout需定位的位置
                    int position = groupLayoutManager.findFirstVisibleItemPosition();
                    if (lastPos != position) {
                        tabLayout.setCurrentTab(position, true);
                    }
                    lastPos = position;
                }
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
     * @param group    当前子元素所在的组
     * @param child    当前点中的子元素
     * @param position 子元素所处的位置 start from 0
     */
    @Override
    public void onChildItemClick(TabGroup group, TabChild child, int position) {
        Toast.makeText(this, group.getGroupTitle() + "-" + child.getName() + '-' + position + '-' + child.getId(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTabSelect(int position) {
        //点击标签，使recyclerView滑动，isRecyclerScroll置false
        isRecyclerScroll = false;
        moveToPosition(groupLayoutManager, recyclerView, position);
    }

    @Override
    public void onTabReselect(int position) {

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
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }
    }
}
