package com.cjs.android.testtab.recycler;

import android.graphics.Color;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cjs.android.testtab.R;
import com.cjs.android.testtab.model.TabChild;
import com.cjs.android.testtab.model.TabGroup;
import com.cjs.android.testtab.model.TabHeader;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单组条目适配器
 *
 * @author JasonChen
 * @email chenjunsen@outlook.com
 * @createTime 2022/6/2 10:49
 */
public class TabRecyclerAdapter extends RecyclerView.Adapter<TabGroupItemHolder> {
    public static final int VIEW_TYPE_HEADER = 1;

    private SparseArray<TabRecyclerChildAdapter> childAdapterMap = new SparseArray<>();
    private List<TabGroup> list;
    private int lastH;
    private TabRecyclerChildAdapter.OnChildItemClickListener onChildItemClickListener;

    private int layoutColumnCount = 4;

    public TabRecyclerAdapter(List<TabGroup> list, int lastH, TabRecyclerChildAdapter.OnChildItemClickListener onChildItemClickListener) {
        this.list = list;
        this.lastH = lastH;
        // 由于初始化子列表适配器的操作放到了构造函数里面 所以子条目点击监听器的设置要放在其之前 否则后面再设置会取不到点击事件
        this.onChildItemClickListener = onChildItemClickListener;
        // 初始化子列表适配器的操作不能放在onBindViewHolder中，如果元素过长，后面的子列表还没在当前屏显示，就不会走那个方法，进而导致childAdapterMap不完整
        // 放到构造函数里面可以确保能渠取到所有的子列表适配器
        initChildAdapter();
    }

    public int getLastH() {
        return lastH;
    }

    public void setLastH(int lastH) {
        this.lastH = lastH;
    }

    public int getLayoutColumnCount() {
        return layoutColumnCount;
    }

    public void setLayoutColumnCount(int layoutColumnCount) {
        this.layoutColumnCount = layoutColumnCount;
    }

    private void initChildAdapter() {
        for (int i = 0; i < getItemCount(); i++) {
            TabGroup tabGroup = list.get(i);
            TabRecyclerChildAdapter childAdapter = new TabRecyclerChildAdapter(tabGroup);
            childAdapter.setHasStableIds(true);
            childAdapter.setOnChildItemClickListener(onChildItemClickListener);
            childAdapterMap.append(tabGroup.getId(), childAdapter);
        }
    }

    @NonNull
    @Override
    public TabGroupItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TabGroupItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tab_group, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull TabGroupItemHolder holder, int position) {
        TabGroup tabGroup = list.get(position);
        TabRecyclerChildAdapter childAdapter = childAdapterMap.get(tabGroup.getId());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(holder.maxHeightRecyclerView.getContext(), layoutColumnCount);
        gridLayoutManager.setAutoMeasureEnabled(true);
        holder.maxHeightRecyclerView.setLayoutManager(gridLayoutManager);
        holder.maxHeightRecyclerView.setAdapter(childAdapter);
        holder.groupTitleView.setText((tabGroup).getGroupTitle());
        holder.groupTitleView.setVisibility((tabGroup).isShowGroupTitle() ? ViewGroup.VISIBLE : View.GONE);
        //判断最后一个view
        if (position == list.size() - 1) {
            if (holder.groupView.getHeight() < lastH) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.height = lastH;
                holder.groupView.setLayoutParams(params);
                holder.setIsRecyclable(false); // 关键步骤 否则在切换编辑和静态模式时会出现数据高度错乱 原因是recyclerView会复用holder
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return list == null ? 0 : list.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public long getItemId(int position) {
        return list == null ? 0 : list.get(position).getId();
    }

    /**
     * 获取子列表集合
     *
     * @return
     */
    public SparseArray<TabRecyclerChildAdapter> getChildAdapterMap() {
        return childAdapterMap;
    }

    /**
     * 改变所有子元素的corner状态
     *
     * @param cornerStatus
     */
    public void changeChildCornerStatus(int cornerStatus, boolean updateNow) {
        for (int i = 0; i < childAdapterMap.size(); i++) {
            TabRecyclerChildAdapter childAdapter = childAdapterMap.get(i);
            childAdapter.changeCornerStatus(cornerStatus);
        }
        if (updateNow) {
            notifyDataSetChanged();
        }
    }

    /**
     * 改变所有选中的子元素状态
     *
     * @param headerList
     * @param cornerStatus
     * @param updateNow
     */
    public void changeClickedChildCornerStatus(List<TabChild> headerList, int cornerStatus, boolean updateNow) {
        if (headerList != null && headerList.size() > 0) {
            for (TabChild header : headerList) {
                TabRecyclerChildAdapter childAdapter = childAdapterMap.get(header.getGroupId());
                childAdapter.changeCornerStatus(cornerStatus, header);
            }
        }
        if (updateNow) {
            notifyDataSetChanged();
        }
    }

}
