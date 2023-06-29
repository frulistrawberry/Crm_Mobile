package com.baihe.lib_home.widget.chart;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class PercentValueFormatter extends ValueFormatter {
    public DecimalFormat mFormat;

    public PercentValueFormatter() {
        mFormat = new DecimalFormat("###,###,##0");
    }

    @Override
    public String getFormattedValue(float value) {
        return "("+mFormat.format(value) + "%)";
    }

    @Override
    public String getPieLabel(float value, PieEntry pieEntry) {

        return ((int)pieEntry.getValue())+getFormattedValue(value);
    }

}
