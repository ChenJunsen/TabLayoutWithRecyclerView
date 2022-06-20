package com.cjs.android.testtab.annotation;

import androidx.annotation.IntDef;

import com.cjs.android.testtab.model.TabChild;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Tab的角标类型限定
 *
 * @author JasonChen
 * @email chenjunsen@outlook.com
 * @date 2022/6/16 11:34
 */
@IntDef({TabChild.CORNER_ADD, TabChild.CORNER_CORRECT, TabChild.CORNER_SUB, TabChild.CORNER_NONE})
@Retention(RetentionPolicy.SOURCE)
public @interface TabCornerStatus {
}
