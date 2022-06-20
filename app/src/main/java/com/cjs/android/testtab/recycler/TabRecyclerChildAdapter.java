package com.cjs.android.testtab.recycler;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cjs.android.testtab.R;
import com.cjs.android.testtab.model.TabChild;
import com.cjs.android.testtab.model.TabGroup;

import java.util.HashMap;
import java.util.List;

/**
 * 子级菜单元素适配器
 *
 * @author JasonChen
 * @email chenjunsen@outlook.com
 * @createTime 2022/6/2 10:48
 */
public class TabRecyclerChildAdapter extends RecyclerView.Adapter<TabChildItemHolder> {
    protected TabGroup group;
    protected List<TabChild> list;
    protected OnChildItemClickListener onChildItemClickListener;
//    protected HashMap<TabChild, Integer> itemClickCache;

    public TabRecyclerChildAdapter(TabGroup group) {
        this.group = group;
        this.list = group.getChildList();
    }

    public void setOnChildItemClickListener(OnChildItemClickListener onChildItemClickListener) {
        this.onChildItemClickListener = onChildItemClickListener;
    }

    @NonNull
    @Override
    public TabChildItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout;
        if (viewType == TabChild.STATUS_NORMAL) {
            layout = R.layout.item_tab_list;
        } else {
            layout = R.layout.item_tab_placeholder;
        }
        return new TabChildItemHolder(LayoutInflater.from(parent.getContext()).inflate(layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TabChildItemHolder holder, @SuppressLint("RecyclerView") int position) {
        TabChild tabChild = list.get(position);
        if (tabChild.getItemStatus() == TabChild.STATUS_NORMAL) {
            holder.labelView.setText(tabChild.getName());
            Glide.with(holder.iconView.getContext()).load(tabChild.getIconUrl()).placeholder(R.drawable.placeholder_2).into(holder.iconView);
            if (onChildItemClickListener != null) {
                holder.itemView.setOnClickListener(v -> onChildItemClickListener.onChildItemClick(group, tabChild, position));
            }
            int cornerSrc = -1;
            switch (tabChild.getCorner()) {
                case TabChild.CORNER_ADD:
                    cornerSrc = R.drawable.icon_add;
                    break;
                case TabChild.CORNER_SUB:
                    cornerSrc = R.drawable.icon_delete;
                    break;
                case TabChild.CORNER_CORRECT:
                    cornerSrc = R.drawable.icon_select_no_gou;
                    break;
            }
            if (cornerSrc == -1) {
                holder.cornerView.setImageDrawable(null);
                holder.cornerView.setVisibility(View.GONE);
            } else {
                holder.cornerView.setImageResource(cornerSrc);
                holder.cornerView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    /**
     * 子条目点击监听器
     *
     * @author JasonChen
     * @email chenjunsen@outlook.com
     * @date 2022/6/10 10:00
     */
    public interface OnChildItemClickListener {
        /**
         * 子条目被点击的时候
         *
         * @param group    当前子元素所在的组
         * @param child    当前点中的子元素
         * @param position 子元素所处的位置 start from 0
         */
        void onChildItemClick(TabGroup group, TabChild child, int position);
    }

    /**
     * 批量改变角标状态
     *
     * @param cornerStatus {@link TabChild#CORNER_ADD}、{@link TabChild#CORNER_SUB}、{@link TabChild#CORNER_NONE}、{@link TabChild#CORNER_CORRECT}
     * @param childList    需要处理的子元素集合
     */
    public void changeCornerStatus(int cornerStatus, List<TabChild> childList) {
        if (childList == null) {
            return;
        }
        for (TabChild child : childList) {
            child.setCorner(cornerStatus);
        }
        notifyDataSetChanged();
    }

    /**
     * 改变角标状态(高效方法)
     *
     * @param cornerStatus {@link TabChild#CORNER_ADD}、{@link TabChild#CORNER_SUB}、{@link TabChild#CORNER_NONE}、{@link TabChild#CORNER_CORRECT}
     * @param child        需要处理的子元素
     * @param position     子元素所处在的adapter里面的位置
     */
    public void changeCornerStatus(int cornerStatus, TabChild child, int position) {
        if (child == null) {
            return;
        }
        child.setCorner(cornerStatus);
        notifyItemChanged(position);
    }

    /**
     * 改变角标状态(自动定位，但是效率不如直接传position的高)
     *
     * @param cornerStatus {@link TabChild#CORNER_ADD}、{@link TabChild#CORNER_SUB}、{@link TabChild#CORNER_NONE}、{@link TabChild#CORNER_CORRECT}
     * @param child        需要处理的子元素
     */
    public void changeCornerStatus(int cornerStatus, TabChild child) {
        if (child == null) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            TabChild c = list.get(i);
            if (child.equals(c)) {
                c.setCorner(cornerStatus);
                notifyItemChanged(i);
                break;
            }
        }

    }

    /**
     * 改变所有子元素的角标状态
     *
     * @param cornerStatus {@link TabChild#CORNER_ADD}、{@link TabChild#CORNER_SUB}、{@link TabChild#CORNER_NONE}、{@link TabChild#CORNER_CORRECT}
     */
    public void changeCornerStatus(int cornerStatus) {
        changeCornerStatus(cornerStatus, list);
    }


    @Override
    public int getItemViewType(int position) {
        return list.get(position).getItemStatus();
    }

}
