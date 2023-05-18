package com.baihe.lihepro.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Author：xubo
 * Time：2019-09-23
 * Description：文字过渡转换RadioGroup
 */
public class TextTransitionRadioGroup extends SliderRadioGroup {

    public TextTransitionRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView instanceof TextTransitionRadioButton) {
                TextTransitionRadioButton radioButton = (TextTransitionRadioButton) childView;
                if (i == selectIndex) {
                    radioButton.move(0);
                } else {
                    radioButton.move(1);
                }
            } else {
                throw new IllegalStateException("TextTransitionRadioGroup的子元素只能为TextTransitionRadioButton");
            }
        }
    }


    public void move(int index, float percentage) {
        super.move(index, percentage);
        int childCount = getChildCount();
        if (index >= 0 && index < childCount) {
            if (childCount == 1) {
                TextTransitionRadioButton textTransitionRadioButton = (TextTransitionRadioButton) getChildAt(index);
                textTransitionRadioButton.move(0.0f);
                return;
            }
            if (index == childCount - 1) {
                TextTransitionRadioButton textTransitionRadioButton = (TextTransitionRadioButton) getChildAt(index);
                textTransitionRadioButton.move(0.0f);
                TextTransitionRadioButton preTextTransitionRadioButton = (TextTransitionRadioButton) getChildAt(index - 1);
                preTextTransitionRadioButton.move(1.0f);
            } else if (index == 0) {
                TextTransitionRadioButton textTransitionRadioButton = (TextTransitionRadioButton) getChildAt(index);
                textTransitionRadioButton.move(percentage);
                TextTransitionRadioButton nextTextTransitionRadioButton = (TextTransitionRadioButton) getChildAt(index + 1);
                nextTextTransitionRadioButton.move(1.0f - percentage);
            } else {
                TextTransitionRadioButton textTransitionRadioButton = (TextTransitionRadioButton) getChildAt(index);
                textTransitionRadioButton.move(percentage);
                TextTransitionRadioButton nextTextTransitionRadioButton = (TextTransitionRadioButton) getChildAt(index + 1);
                nextTextTransitionRadioButton.move(1.0f - percentage);
                TextTransitionRadioButton preTextTransitionRadioButton = (TextTransitionRadioButton) getChildAt(index - 1);
                preTextTransitionRadioButton.move(1.0f);
            }
        } else {
            throw new IllegalStateException("TextTransitionRadioGroup中move(int, int)传入的子元素索引并不存在");
        }
    }
}
