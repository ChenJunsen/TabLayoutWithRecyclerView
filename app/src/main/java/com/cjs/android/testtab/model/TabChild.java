package com.cjs.android.testtab.model;

import com.cjs.android.testtab.annotation.TabCornerStatus;
import com.cjs.android.testtab.annotation.TabItemStatus;

import org.apache.commons.lang3.SerializationUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 菜单元素数据模型
 *
 * @author JasonChen
 * @email chenjunsen@outlook.com
 * @date 2022/6/16 11:31
 */
public class TabChild implements Serializable {

    /**
     * 没有角标（默认）
     */
    public static final int CORNER_NONE = 0;
    /**
     * 减号角标
     */
    public static final int CORNER_SUB = 1;
    /**
     * 加号角标
     */
    public static final int CORNER_ADD = 2;
    /**
     * 已经勾选的角标
     */
    public static final int CORNER_CORRECT = 3;


    /**
     * 正常的元素条目（默认）
     */
    public static final int STATUS_NORMAL = 0;
    /**
     * 虚线框占位格子
     */
    public static final int STATUS_PLACEHOLDER = 1;

    // 自有字段
    private int groupId; // 分组索引 标记当前子元素的所属父元素组的id
    @TabCornerStatus
    private int corner; // 角标状态
    @TabItemStatus
    private int itemStatus;  // 条目类型

    // 服务端原始数据字段
    private int id;
    private String appletsName;// 小程序标识
    private String content;
    private String disclaimerContent;
    private String disclaimerTitle; // "免责声明"标题
    private String iconUrl;  // 标签图标链接
    private String jumpKeywords;// 原生跳转标识（重定向标识）
    private String jumpType; // 跳转类型（0-跳转地址，1-原生页面，2-小程序）
    private String jumpUrl;
    private String labelPicUrl;//首页服务item标签icon地址
    private String labelText;//首页服务item标签icon名字
    private String name; // 标签名字
    private String parameters;//类似"customerId,channelType,deviceType,customerName,clientKey" 签名参数
    private int priority;
    private int showCustomerService;//是否显示在线客服(0不显示，1显示)
    private int showTitleBar;//是否显示标题栏(0不显示，1显示)
    private int singleSignType;//单点登录校验，0：不需要校验，1：需要校验（根据jumpType来判断，jumpType为0时使用）
    private int status;
    private List<Tip> tips;

    public TabChild() {
    }


    public TabChild(int id, int groupId, String label) {
        this.id = id;
        this.groupId = groupId;
        this.name = label;
    }

    public TabChild(int id, String label, String iconUrl) {
        this.id = id;
        this.name = label;
        this.iconUrl = iconUrl;
    }


    public TabChild(int itemStatus) {
        this.itemStatus = itemStatus;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getCorner() {
        return corner;
    }

    public void setCorner(@TabCornerStatus int corner) {
        this.corner = corner;
    }

    public int getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(@TabItemStatus int itemStatus) {
        this.itemStatus = itemStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppletsName() {
        return appletsName;
    }

    public void setAppletsName(String appletsName) {
        this.appletsName = appletsName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDisclaimerContent() {
        return disclaimerContent;
    }

    public void setDisclaimerContent(String disclaimerContent) {
        this.disclaimerContent = disclaimerContent;
    }

    public String getDisclaimerTitle() {
        return disclaimerTitle;
    }

    public void setDisclaimerTitle(String disclaimerTitle) {
        this.disclaimerTitle = disclaimerTitle;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getJumpKeywords() {
        return jumpKeywords;
    }

    public void setJumpKeywords(String jumpKeywords) {
        this.jumpKeywords = jumpKeywords;
    }

    public String getJumpType() {
        return jumpType;
    }

    public void setJumpType(String jumpType) {
        this.jumpType = jumpType;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public String getLabelPicUrl() {
        return labelPicUrl;
    }

    public void setLabelPicUrl(String labelPicUrl) {
        this.labelPicUrl = labelPicUrl;
    }

    public String getLabelText() {
        return labelText;
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getShowCustomerService() {
        return showCustomerService;
    }

    public void setShowCustomerService(int showCustomerService) {
        this.showCustomerService = showCustomerService;
    }

    public int getShowTitleBar() {
        return showTitleBar;
    }

    public void setShowTitleBar(int showTitleBar) {
        this.showTitleBar = showTitleBar;
    }

    public int getSingleSignType() {
        return singleSignType;
    }

    public void setSingleSignType(int singleSignType) {
        this.singleSignType = singleSignType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<Tip> getTips() {
        return tips;
    }

    public void setTips(List<Tip> tips) {
        this.tips = tips;
    }

    @Override
    public String toString() {
        return "TabChild2{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                '}';
    }

    /**
     * 重写equals，只要id、groupId和itemStatus相同就认为是同一个元素
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TabChild tabChild2 = (TabChild) o;
        return groupId == tabChild2.groupId && id == tabChild2.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, id);
    }

    /**
     * 深度拷贝
     *
     * @return
     */
    public TabChild clone() {
        return SerializationUtils.clone(this);
    }
}
