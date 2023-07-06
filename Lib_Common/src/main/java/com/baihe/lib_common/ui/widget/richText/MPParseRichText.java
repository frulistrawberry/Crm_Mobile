package com.baihe.lib_common.ui.widget.richText;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MPParseRichText {

    /**
     * clickspan 回调
     */
    public interface SpanClick {
        public abstract void onSpanClicked(View widget, MPRichTextBaseBean obj);
    }

    /**
     * 解析富文本   jsonarray
     *
     * @param jsonArray    messageArray
     * @param spanClick    clickspan 回调
     * @param defaultColor == -1 默认白色 若无色传透明色值
     * @return
     */
    public synchronized static MPSpanUtils parseRichText(JSONArray jsonArray, final SpanClick spanClick, final @ColorInt int defaultColor) {
        MPSpanUtils spanUtils = new MPSpanUtils();
        ArrayList<MPRichTextBaseBean> parseList = parse(jsonArray);
        if (parseList == null || parseList.size() == 0) {
            return spanUtils;
        } else {
            for (int i = 0; i < parseList.size(); i++) {
                MPRichTextBaseBean mpRichTextBaseBean = parseList.get(i);

                //type
                if (mpRichTextBaseBean.getType() == 1) {

                    final MPRTTextBean textBaseBean = (MPRTTextBean) mpRichTextBaseBean;
                    //无颜色 且 有默认颜色
                    if (textBaseBean.getColor() == -100086) {
                        textBaseBean.setColor(defaultColor);
                    }
                    spanUtils.append(textBaseBean.getText());
                    if(textBaseBean.getSize()>0){
                        spanUtils.setFontSize(textBaseBean.getSize(),true);
                    }
                    spanUtils.setForegroundColor(textBaseBean.getColor());
                    spanUtils.setBackgroundColor(textBaseBean.getBgColor());

                    //span
                    switch (textBaseBean.getSpan()) {
                        case 0:
                            break;
                        case 1:
                            spanUtils.setBold();
                            break;
                        case 2:
                            spanUtils.setItalic();
                            break;
                        case 3:
                            spanUtils.setBoldItalic();
                            break;
                        case 4:
                            spanUtils.setStrikethrough();
                            break;
                        case 5:
                            spanUtils.setUnderline();
                            break;
                    }

                    //jump
                    if (!TextUtils.isEmpty(textBaseBean.getJump()) && spanClick != null) {
                        MPClickableSpan userClickSpan = new MPClickableSpan<MPRichTextBaseBean>(textBaseBean) {
                            @Override
                            public void onSpanClicked(View widget, MPRichTextBaseBean obj) {
                                spanClick.onSpanClicked(widget, obj);
                            }

                            @Override
                            public void updateDrawState(@NonNull TextPaint ds) {
                                if (textBaseBean.getColor() == -10086) {
                                    textBaseBean.setColor(defaultColor);
                                }else {
                                    ds.setColor(textBaseBean.getColor());
                                }

                                ds.setUnderlineText(false);
                            }
                        };
                        spanUtils.setClickSpan(userClickSpan);
                    }

                } else if (mpRichTextBaseBean.getType() == 2) {

                }
            }
            return spanUtils;
        }
    }

    /**
     * 解析富文本   jsonarray
     *
     * @param spanUtils    span解析
     * @param jsonArray    messageArray
     * @param spanClick    clickspan 回调
     * @param defaultColor == -1 无默认颜色值
     * @return
     */
    public synchronized static MPSpanUtils parseRichText(MPSpanUtils spanUtils, JSONArray jsonArray, final SpanClick spanClick, final @ColorInt int defaultColor) {
        if (spanUtils == null) {
            spanUtils = new MPSpanUtils();
        }
        ArrayList<MPRichTextBaseBean> parseList = parse(jsonArray);
        if (parseList == null || parseList.size() == 0) {
            return spanUtils;
        } else {
            for (int i = 0; i < parseList.size(); i++) {
                MPRichTextBaseBean mpRichTextBaseBean = parseList.get(i);

                //type
                if (mpRichTextBaseBean.getType() == 1) {

                    final MPRTTextBean textBaseBean = (MPRTTextBean) mpRichTextBaseBean;
                    //无颜色 且 有默认颜色
                    if (textBaseBean.getColor() == -10086) {
                        textBaseBean.setColor(defaultColor);
                    }
                    spanUtils.append(textBaseBean.getText());
                    spanUtils.setForegroundColor(textBaseBean.getColor());
                    if(textBaseBean.getSize()>0){
                        spanUtils.setFontSize(textBaseBean.getSize(),true);
                    }
                    spanUtils.setBackgroundColor(textBaseBean.getBgColor());

                    //span
                    switch (textBaseBean.getSpan()) {
                        case 0:
                            break;
                        case 1:
                            spanUtils.setBold();
                            break;
                        case 2:
                            spanUtils.setItalic();
                            break;
                        case 3:
                            spanUtils.setBoldItalic();
                            break;
                        case 4:
                            spanUtils.setStrikethrough();
                            break;
                        case 5:
                            spanUtils.setUnderline();
                            break;
                    }

                    //jump
                    if (!TextUtils.isEmpty(textBaseBean.getJump()) && spanClick != null) {
                        MPClickableSpan userClickSpan = new MPClickableSpan<MPRichTextBaseBean>(textBaseBean) {
                            @Override
                            public void onSpanClicked(View widget, MPRichTextBaseBean obj) {
                                spanClick.onSpanClicked(widget, obj);
                            }

                            @Override
                            public void updateDrawState(@NonNull TextPaint ds) {
                                if (textBaseBean.getColor() == -10086) {
                                    textBaseBean.setColor(defaultColor);
                                }else {
                                    ds.setColor(textBaseBean.getColor());
                                }

                                ds.setUnderlineText(false);
                            }
                        };
                        spanUtils.setClickSpan(userClickSpan);
                    }

                } else if (mpRichTextBaseBean.getType() == 2) {

                }
            }
            return spanUtils;
        }
    }


    /**
     * 解析富文本   MPRichTextBaseBeanList
     *
     * @param parseList    messageArray
     * @param spanClick    clickspan 回调
     * @param defaultColor == -1 白色
     * @return
     */
    public synchronized static MPSpanUtils parseRichTextSpannableStringList(ArrayList<MPRichTextBaseBean> parseList, final SpanClick spanClick, final @ColorInt int defaultColor) {
        MPSpanUtils spanUtils = new MPSpanUtils();
        if (parseList == null || parseList.size() == 0) {
            return spanUtils;
        } else {
            for (int i = 0; i < parseList.size(); i++) {
                MPRichTextBaseBean mpRichTextBaseBean = parseList.get(i);

                //type
                if (mpRichTextBaseBean.getType() == 1) {

                    final MPRTTextBean textBaseBean = (MPRTTextBean) mpRichTextBaseBean;
                    //无颜色 且 有默认颜色
                    if (textBaseBean.getColor() == -10086) {
                        textBaseBean.setColor(defaultColor);
                    }

                    if (textBaseBean.getSpannableString() == null) {
                        spanUtils.append(textBaseBean.getText());
                    } else {
                        spanUtils.append(textBaseBean.getSpannableString());
                    }
                    if(textBaseBean.getSize()>0){
                        spanUtils.setFontSize(textBaseBean.getSize(),true);
                    }

                        spanUtils.setForegroundColor(textBaseBean.getColor());

                        spanUtils.setBackgroundColor(textBaseBean.getBgColor());

                    //span
                    switch (textBaseBean.getSpan()) {
                        case 0:
                            break;
                        case 1:
                            spanUtils.setBold();
                            break;
                        case 2:
                            spanUtils.setItalic();
                            break;
                        case 3:
                            spanUtils.setBoldItalic();
                            break;
                        case 4:
                            spanUtils.setStrikethrough();
                            break;
                        case 5:
                            spanUtils.setUnderline();
                            break;
                    }

                    //jump
                    if (!TextUtils.isEmpty(textBaseBean.getJump()) && spanClick != null) {
                        MPClickableSpan userClickSpan = new MPClickableSpan<MPRichTextBaseBean>(textBaseBean) {
                            @Override
                            public void onSpanClicked(View widget, MPRichTextBaseBean obj) {
                                spanClick.onSpanClicked(widget, obj);
                            }

                            @Override
                            public void updateDrawState(@NonNull TextPaint ds) {
                                if (textBaseBean.getColor() == -10086) {
                                    textBaseBean.setColor(defaultColor);
                                }else {
                                    ds.setColor(textBaseBean.getColor());
                                }

                                ds.setUnderlineText(false);
                            }
                        };
                        spanUtils.setClickSpan(userClickSpan);
                    }

                } else if (mpRichTextBaseBean.getType() == 2) {

                }
            }
            return spanUtils;
        }
    }


    /**
     * 消息数据解析
     *
     * @param message
     * @return
     */
    public synchronized static ArrayList<MPRichTextBaseBean> parse(JSONArray message) {

        ArrayList<MPRichTextBaseBean> list = new ArrayList<>();
        try {
            for (int i = 0; i < message.length(); i++) {
                JSONObject messageJson = message.getJSONObject(i);
                int type = JsonUtil.getInt("type", messageJson);
                if (type == 1) {
                    list.add(parseText(messageJson));
                } else if (type == 2) {
                    list.add(parseImg(messageJson));
                }
            }
            return list;
        } catch (JSONException e) {
            return null;
        }

    }

    /**
     * img部分解析
     *
     * @param messageJson
     * @return
     */
    public synchronized static MPRichTextBaseBean parseImg(JSONObject messageJson) {
        MPRTImageBean imageBean = new MPRTImageBean();
        imageBean.setFirsthandJson(messageJson.toString());
        imageBean.setType(JsonUtil.getInt("type", messageJson));
        imageBean.setResId(JsonUtil.getString("resId", messageJson));
        imageBean.setUrl(JsonUtil.getString("url", messageJson));
        imageBean.setJump(JsonUtil.getString("jump", messageJson));
        imageBean.setWidth(JsonUtil.getInt("width", messageJson));
        imageBean.setHeight(JsonUtil.getInt("height", messageJson));
        return imageBean;
    }

    /**
     * text部分解析
     *
     * @param messageJson
     * @return
     */
    public synchronized static MPRichTextBaseBean parseText(JSONObject messageJson) {
        MPRTTextBean textBean = new MPRTTextBean();
        textBean.setFirsthandJson(messageJson.toString());
        textBean.setType(JsonUtil.getInt("type", messageJson));
        textBean.setText(JsonUtil.getString("text", messageJson));
        try {
            int color = Color.parseColor(JsonUtil.getString("color", messageJson));
            textBean.setColor(color);
        } catch (Exception e) {
            textBean.setColor(-1);
        }

        textBean.setJump(JsonUtil.getString("jump", messageJson));
        textBean.setSpan(JsonUtil.getInt("span", messageJson));
        return textBean;
    }
}
