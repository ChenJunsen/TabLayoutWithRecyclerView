package com.cjs.android.testtab.recycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cjs.android.testtab.R;

/**
 * 条目绑定器
 *
 * @author JasonChen
 * @email chenjunsen@outlook.com
 * @createTime 2022/6/1 14:38
 */
public class TabChildItemHolder extends RecyclerView.ViewHolder {

    public ImageView cornerView;
    public ImageView iconView;
    public TextView labelView;

    public TabChildItemHolder(@NonNull View itemView) {
        super(itemView);
        iconView = itemView.findViewById(R.id.icon);
        labelView = itemView.findViewById(R.id.label);
        cornerView = itemView.findViewById(R.id.ic_corner);
    }
}
