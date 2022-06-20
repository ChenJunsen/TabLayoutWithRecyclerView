package com.cjs.android.testtab.model;

import java.util.List;

public class TabHeader {
    protected int viewType;
    protected List<TabChild> list;

    public TabHeader() {
    }

    public List<TabChild> getChildList() {
        return list;
    }

    public void setChildList(List<TabChild> list) {
        this.list = list;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
