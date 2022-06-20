package com.cjs.android.testtab.model;

public class TabGroup extends TabHeader{
    private boolean showGroupTitle=true;

    private String groupTitle;
    private int id;

    public TabGroup() {
    }

    public String getGroupTitle() {
        return groupTitle;
    }

    public void setGroupTitle(String groupTitle) {
        this.groupTitle = groupTitle;
    }

    public boolean isShowGroupTitle() {
        return showGroupTitle;
    }

    public void setShowGroupTitle(boolean showGroupTitle) {
        this.showGroupTitle = showGroupTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
