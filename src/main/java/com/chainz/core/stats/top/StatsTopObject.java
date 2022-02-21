package com.chainz.core.stats.top;

import java.util.ArrayList;

public class StatsTopObject {
    private ArrayList<StatsTopRow> top_list;

    public StatsTopObject(ArrayList<StatsTopRow> top_list) {
        this.top_list = top_list;
    }

    public ArrayList<StatsTopRow> getToplist() {
        return this.top_list;
    }

    public StatsTopRow getRow(int index) {
        return this.top_list.get(index);
    }
}
