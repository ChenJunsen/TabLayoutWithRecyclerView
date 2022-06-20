package com.cjs.android.testtab.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cjs.android.testtab.model.TabChild;
import com.cjs.android.testtab.model.TabGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 菜单解析器
 *
 * @author JasonChen
 * @email chenjunsen@outlook.com
 * @date 2022/6/16 14:43
 */
public class MenuParser {
    private Context context;
    private boolean useDummyData; // 是否采用模拟数据
    private Dummy dummy;

    private static final String TAG = "MenuParser";

    public MenuParser(Context context) {
        this.context = context;
    }

    public MenuParser(Context context, boolean useDummyData) {
        this.context = context;
        this.useDummyData = useDummyData;
        if (this.useDummyData) {
            dummy = new Dummy();
        }
    }

    public boolean isUseDummyData() {
        return useDummyData;
    }

    public void setUseDummyData(boolean useDummyData) {
        this.useDummyData = useDummyData;
        if (this.useDummyData) {
            dummy = new Dummy();
        }
    }

    public JSONObject getMenuJSONData() {
        JSONObject obj = null;
        String json = IOUtil.getFromAssets(context, "menu.json");
        if (!TextUtils.isEmpty(json)) {
            JSONObject o = JSON.parseObject(json);
            if (o != null) {
                obj = o.getJSONObject("serviceLayout");
            }
        }
        return obj;
    }

    /**
     * 获取服务大厅数据列表
     *
     * @return
     */
    public List<TabGroup> getServiceHallList() {
        if (useDummyData) {
            return dummy.generateGroupList();
        } else {
            List<TabGroup> tabGroupList = new ArrayList<>();
            JSONArray array = getMenuJSONData().getJSONArray("categoryList");
            if (array != null && !array.isEmpty()) {
                int size = array.size();
                for (int i = 0; i < size; i++) {
                    JSONObject obj = array.getJSONObject(i);
                    TabGroup group = new TabGroup();
                    group.setShowGroupTitle(i != 0);
                    group.setId(i);
                    group.setGroupTitle(obj.getString("categoryName"));
                    JSONArray childArray = obj.getJSONArray("serviceList");
                    List<TabChild> childList = new ArrayList<>();
                    if (childArray != null && !childArray.isEmpty()) {
                        int cSize = childArray.size();
                        for (int j = 0; j < cSize; j++) {
                            TabChild child = JSON.toJavaObject(childArray.getJSONObject(j), TabChild.class);
                            child.setGroupId(group.getId());
                            childList.add(child);
                        }
                    }
                    group.setChildList(childList);
                    tabGroupList.add(group);
                }
            }
            return tabGroupList;
        }
    }

    public List<TabChild> getMyCommonList(List<TabGroup> groupList) {
        if (useDummyData) {
            return dummy.generateHeaderList(groupList);
        } else {
            List<TabChild> childList = new ArrayList<>();
            int groups = new Random().nextInt(groupList.size()) + 1;
            for (int i = 0; i < groups; i++) {
                childList.add(groupList.get(i).getChildList().get(0).clone());
            }
            return childList;
        }
    }
}
