package com.cjs.android.testtab.recycler;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cjs.android.testtab.R;
import com.cjs.android.testtab.widgets.MaxHeightRecyclerView;

/**
 * 菜单组条目绑定器
 *
 * @author JasonChen
 * @email chenjunsen@outlook.com
 * @createTime 2022/6/2 10:49
 */
public class TabGroupItemHolder extends RecyclerView.ViewHolder {

    public TextView groupTitleView;
    public RecyclerView maxHeightRecyclerView;
    public LinearLayout groupView;

    public TabGroupItemHolder(@NonNull View itemView) {
        super(itemView);
        maxHeightRecyclerView = itemView.findViewById(R.id.maxHeightRecyclerView);
        groupTitleView = itemView.findViewById(R.id.groupTitle);
        groupView = itemView.findViewById(R.id.group);
    }
}
