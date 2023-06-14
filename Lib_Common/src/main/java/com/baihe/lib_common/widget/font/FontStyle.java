package com.baihe.lib_common.widget.font;

public enum FontStyle {
    /**
     * 细体（实际为常规字体，当作设计图的细体）
     */
    LIGHT(0),
    /**
     * 常规字体（实际为中粗体，当作设计图的常规字体）
     */
    NORMAL(1),
    /**
     * 中粗体（实际为粗体，当作设计图的中粗体）
     */
    HALF_BOLD(2),
    /**
     * 粗体 (更粗的字体)
     */
    BOLD(3);

    final int value;

    FontStyle(int value) {
        this.value = value;
    }

    public int valueOf() {
        return value;
    }
}

