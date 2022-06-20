package com.cjs.android.testtab.utils;

import com.cjs.android.testtab.model.TabChild;
import com.cjs.android.testtab.model.TabGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 模拟数据构造器
 *
 * @author JasonChen
 * @email chenjunsen@outlook.com
 * @date 2022/6/15 11:33
 */
public class Dummy {
    private static final String[] titles = new String[]{"行前准备", "机场相关", "售后服务", "旅行预定", "餐食服务", "其他"};

    private AtomicInteger counter;
    private Random random;

    private static Dummy instance;

    public static Dummy getInstance() {
        if (instance == null) {
            instance = new Dummy();
        }
        return instance;
    }

    public Dummy() {
        counter = new AtomicInteger();
        random = new Random();
    }

    public List<TabGroup> generateGroupList() {
        List<TabGroup> list = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            List<TabChild> childList = new ArrayList<>();
            int num = random.nextInt(10) + 1;
            for (int j = 0; j < num; j++) {
                childList.add(new TabChild(counter.incrementAndGet(), i, titles[i] + j));
            }
            TabGroup group = new TabGroup();
            group.setShowGroupTitle(i != 0);
            group.setId(i);
            group.setGroupTitle(titles[i]);
            group.setChildList(childList);
            list.add(group);
        }
        return list;
    }

    public List<TabChild> generateHeaderList(List<TabGroup> groupList) {
        List<TabChild> childList = new ArrayList<>();
        if (groupList != null) {
            int groups = random.nextInt(groupList.size());
            for (int i = 0; i < groups; i++) {
                childList.add(groupList.get(i).getChildList().get(0).clone());
            }
        }
        return childList;
    }
}
