package com.cjs.android.testtab.annotation;

import androidx.annotation.IntDef;

import com.cjs.android.testtab.model.TabChild;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * Tab子条目类型限定
 * @email chenjunsen@outlook.com
 * @author JasonChen
 * @date 2022/6/16 11:34
*/
@IntDef({TabChild.STATUS_NORMAL, TabChild.STATUS_PLACEHOLDER})
@Retention(RetentionPolicy.SOURCE)
public @interface TabItemStatus {
}
