package com.baihe.lib_common.ui.widget.richText;

import org.json.JSONObject;


/**
 * 富文本 img
 */
public class MPRTImageBean extends MPRichTextBaseBean {

    //本地资源 resId
    private String resId;
    //网络url
    private String url;
    //宽
    private int width;
    //高
    private int height;

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }


    public void createFromJson(JSONObject messageJson){

        setType(2);

        resId = JsonUtil.getString("resId", messageJson);
        url = JsonUtil.getString("url", messageJson);
        width = JsonUtil.getInt("width", messageJson);
        height = JsonUtil.getInt("height", messageJson);
        firsthandJson = messageJson.toString();


        if(messageJson.has("jump")){
            jump = JsonUtil.getString("jump",messageJson);
        }
    }
}
