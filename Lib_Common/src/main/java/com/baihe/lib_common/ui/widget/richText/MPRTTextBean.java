package com.baihe.lib_common.ui.widget.richText;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.TextUtils;

import androidx.annotation.ColorInt;

import org.json.JSONObject;


/**
 * 富文本 text
 */
public class MPRTTextBean extends MPRichTextBaseBean {

    private String text;
    @ColorInt   // -100086 不赋予颜色
    private int color = -10086;
    @ColorInt  //透明
    private int bgColor = -0;
    /**
     * span
     * 0：无特殊样式
     * 1:加粗
     * 2:倾斜
     * 3:加粗倾斜
     * 4:删除线
     * 5:下划线
     */
    private int span;

    private SpannableString spannableString;
    /**
     * 字体大小
     */
    private int size;


    public int getSize() {
        return size;
    }


    public void setSize(int size) {
        this.size = size;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @ColorInt
    public int getColor() {
        return color;
    }

    public void setColor(@ColorInt int color) {
        this.color = color;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public int getSpan() {
        return span;
    }

    public void setSpan(int span) {
        this.span = span;
    }

    public SpannableString getSpannableString() {
        return spannableString;
    }

    public void setSpannableString(SpannableString spannableString) {
        this.spannableString = spannableString;
    }


    public void createFromJson(JSONObject jsonObject) {

        setType(1);
        firsthandJson = jsonObject.toString();

        span = JsonUtil.getInt("span", jsonObject, 0);

        if (jsonObject.has("color")) {
            String colorStr = JsonUtil.getString("color", jsonObject);
            if (!TextUtils.isEmpty(colorStr)) {
                color = Color.parseColor(colorStr);
            }
        }

        if (jsonObject.has("bgColor")) {
            String colorStr = JsonUtil.getString("bgColor", jsonObject);
            if (!TextUtils.isEmpty(colorStr)) {
                bgColor = Color.parseColor(colorStr);
            }
        }

        if (jsonObject.has("text")) {
            text = JsonUtil.getString("text", jsonObject);
        }

        if (jsonObject.has("jump")) {
            jump = JsonUtil.getString("jump", jsonObject);
        }
        if (jsonObject.has("size")) {
            size = JsonUtil.getInt("size", jsonObject);
        }

        if (jsonObject.has("newLine")) {
            int newLine = JsonUtil.getInt("newLine", jsonObject, -1);
            if (newLine == 1) {
                text = "\n" + text;
            } else if (newLine == 2) {
                text = text + "\n";
            } else if (newLine == 3) {
                text = "\n" + text + "\n";
            }
        }
    }
}
