package com.course;

import java.util.ArrayList;
import java.util.List;

public class Histogram {
    private class HistogramNode {
        public String moduleName;
        public int average;

        public HistogramNode(String theme, int n) {
            this.moduleName = theme;
            this.average = n;
        }

    }

    public List<HistogramNode> histogram = new ArrayList<>();

    public void add(String t, int n) {
        this.histogram.add(new HistogramNode(t, n));
    }

    public List<HistogramNode> getHistogramList() {
        return this.histogram;
    }

}
