package com.example.voca.realtimeDB;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Today {
    private static final String TAG = "Today";


    private int goal;
    private int count;

    public Today() {
    }

    public Today(int goal, int count) {
        this.goal = goal;
        this.count = count;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("goal", goal);
        map.put("count", count);

        return map;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getGoal() {
        return goal;
    }

    public int getCount() {
        return count;
    }

}
