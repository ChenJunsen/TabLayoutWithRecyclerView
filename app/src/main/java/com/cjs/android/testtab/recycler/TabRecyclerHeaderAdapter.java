package com.cjs.android.testtab.recycler;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cjs.android.testtab.R;
import com.cjs.android.testtab.model.TabChild;
import com.cjs.android.testtab.model.TabGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Header适配器
 *
 * @author JasonChen
 * @email chenjunsen@outlook.com
 * @createTime 2022/6/2 10:48
 */
public class TabRecyclerHeaderAdapter extends TabRecyclerChildAdapter {
    private int maxHeaderNum = 8;

    public TabRecyclerHeaderAdapter(TabGroup group) {
        super(group);
    }

    public int getMaxHeaderNum() {
        return maxHeaderNum;
    }

    public void setMaxHeaderNum(int maxHeaderNum) {
        this.maxHeaderNum = maxHeaderNum;
    }

    @Override
    public void onBindViewHolder(@NonNull TabChildItemHolder holder, int position) {
        super.onBindViewHolder(holder, position);
    }

    /**
     * 往头部增加占位格子
     */
    public void addPlaceholder() {
        int currentCount = getItemCount();
        if (currentCount > 0 && currentCount < maxHeaderNum) {
            int count = maxHeaderNum - getItemCount();
            for (int i = 0; i < count; i++) {
                list.add(new TabChild(TabChild.STATUS_PLACEHOLDER));
            }
            notifyItemRangeInserted(getItemCount() - 1, count);
        }
    }

    /**
     * 清空头部的占位格子
     */
    public void removePlaceholder() {
        int currentCount = getItemCount();
        List<TabChild> toBeRemoved = getPlaceholderItemList();
        int toCount = toBeRemoved.size();
        if (toCount > 0 && currentCount > toCount) {
            list.removeAll(toBeRemoved);
            notifyItemRangeRemoved(currentCount - toCount - 1, toCount);
        }
    }

    /**
     * 获取占位子元素列表
     *
     * @return
     */
    public List<TabChild> getPlaceholderItemList() {
        int currentCount = getItemCount();
        List<TabChild> toBeRemoved = new ArrayList<>();
        if (currentCount > 0) {
            for (int i = 0; i < currentCount; i++) {
                if (list.get(i).getItemStatus() == TabChild.STATUS_PLACEHOLDER) {
                    toBeRemoved.add(list.get(i));
                }
            }
        }
        return toBeRemoved;
    }

    public void addChild(TabChild child, int position) {
        if (child == null) {
            return;
        }
        for (int i = 0; i < getItemCount(); i++) {
            if (list.get(i).getItemStatus() == TabChild.STATUS_PLACEHOLDER) {
                child.setCorner(TabChild.CORNER_SUB);
                list.set(i, child);
                break;
            }
        }
        notifyItemChanged(position);
    }

    /**
     * 移除子元素
     *
     * @param position
     */
    public void removeChild(int position) {
        //此处不要采用notifyItemRemoved和notifyItemInserted，因为是异步方法，会出现错位
        list.remove(position);
//        notifyItemRemoved(position);
        list.add(new TabChild(TabChild.STATUS_PLACEHOLDER));
//        notifyItemInserted(list.size() - 1);
        notifyDataSetChanged();
    }

    /**
     * 获取非占位元素的数量
     *
     * @return
     */
    public int getNonePlaceholderCount() {
        int count = 0;
        for (TabChild child : list) {
            if (child.getItemStatus() != TabChild.STATUS_PLACEHOLDER) {
                count++;
            }
        }
        return count;
    }
}
