package com.baihe.lib_common.ui.widget.richText;

import android.content.Context;
import android.text.SpannableString;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @author xukankan
 * @date 2023/7/6 10:44
 * @email：xukankan@jiayuan.com
 * @description：
 */
public class RichTextView extends AppCompatTextView {
    public RichTextView(Context context) {
        super(context);
    }

    public RichTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RichTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setRichText(JSONArray textArr, MPParseRichText.SpanClick spanClick) throws JSONException {

        if (textArr == null || textArr.length() == 0) {
            return;
        }

        ArrayList<MPRichTextBaseBean> units = new ArrayList<>();

        for (int i = 0; i < textArr.length(); i++) {

            JSONObject eachObj = textArr.getJSONObject(i);

            int type = eachObj.getInt("type");
            if (type == 1) {
                MPRTTextBean textBean = new MPRTTextBean();
                textBean.createFromJson(eachObj);
                textBean.setSpannableString(new SpannableString(textBean.getText()));
                units.add(textBean);
            }

        }

        MPSpanUtils spanUtils = MPParseRichText
                .parseRichTextSpannableStringList(units, spanClick, getTextColors().getDefaultColor());

        setText(spanUtils.create());

    }
}
