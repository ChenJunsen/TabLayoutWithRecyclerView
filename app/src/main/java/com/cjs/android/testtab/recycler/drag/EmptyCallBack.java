package com.cjs.android.testtab.recycler.drag;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.cjs.android.testtab.model.TabChild;

import java.util.Collections;
import java.util.List;


/**
 *
 * @email chenjunsen@outlook.com
 * @author JasonChen
 * @date 2022/6/14 22:30
*/
public class EmptyCallBack extends ItemTouchHelper.Callback {

    public EmptyCallBack() {
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return 0;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }
}
