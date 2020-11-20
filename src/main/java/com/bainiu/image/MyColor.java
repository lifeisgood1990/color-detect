package com.bainiu.image;

import java.util.Arrays;

/**
 * @author: yf
 * @date: 2020/11/13  10:55
 * @desc:
 */

public class MyColor {

    private int[] rgb;

    private String percentStr;

    private double percent;

    public int[] getRgb() {
        return rgb;
    }

    public void setRgb(int[] rgb) {
        this.rgb = rgb;
    }

    public String getPercentStr() {
        return percentStr;
    }

    public void setPercentStr(String percentStr) {
        this.percentStr = percentStr;
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    @Override
    public String toString() {
        return "MyColor{" +
                "rgb=" + Arrays.toString(rgb) +
                ", percentStr='" + percentStr + '\'' +
                ", percent=" + percent +
                '}';
    }
}
