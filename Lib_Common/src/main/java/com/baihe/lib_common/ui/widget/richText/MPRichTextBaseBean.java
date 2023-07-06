package com.baihe.lib_common.ui.widget.richText;

/**
 * 聊天富文本消息标签定义
 * lfx
 * <p>
 * <p>
 * 属性名	含义
 * type	1:文字 2:图片(本地or网络)
 * color	仅Type=1生效
 * bgColor  仅Type=1生效 文字背景颜色
 * 文字颜色
 * span	仅Type=1生效
 * 0：无特殊样式
 * 1:加粗
 * 2:倾斜
 * 3:加粗倾斜
 * 4:删除线
 * 5:下划线
 * jump	跳转
 * resId	仅Type=2生效
 * 本地资源编号
 * url	仅Type=2生效
 * 网络图片地址
 * width	仅Type=2生效
 * 显示的图片宽度
 * height	仅Type=2生效
 * 显示的图片高度
 */
public class MPRichTextBaseBean {
    //type	1:文字 2:图片(本地or网络)
    protected int type;
    //跳转
    protected  String jump;
    //原始json 数据
    protected String firsthandJson;

    public String getJump() {
        return jump;
    }

    public void setJump(String jump) {
        this.jump = jump;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFirsthandJson() {
        return firsthandJson;
    }

    public void setFirsthandJson(String firsthandJson) {
        this.firsthandJson = firsthandJson;
    }
}
