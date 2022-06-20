package com.cjs.android.testtab.tab;

import com.flyco.tablayout.listener.CustomTabEntity;

/**
 * 自定义简单的文字tab
 *
 * @author JasonChen
 * @email chenjunsen@outlook.com
 * @createTime 2022/6/1 14:11
 */
public class TextTab implements CustomTabEntity {
    private String tabTitle;
    private int tabSelectedIcon;
    private int tabUnselectedIcon;

    public TextTab() {
    }

    public TextTab(String tabTitle) {
        this.tabTitle = tabTitle;
    }

    @Override
    public String getTabTitle() {
        return this.tabTitle;
    }

    @Override
    public int getTabSelectedIcon() {
        return this.tabSelectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return this.tabUnselectedIcon;
    }

    public void setTabTitle(String tabTitle) {
        this.tabTitle = tabTitle;
    }

    public void setTabSelectedIcon(int tabSelectedIcon) {
        this.tabSelectedIcon = tabSelectedIcon;
    }

    public void setTabUnselectedIcon(int tabUnselectedIcon) {
        this.tabUnselectedIcon = tabUnselectedIcon;
    }
}
