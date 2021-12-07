package com.example.voca.realtimeDB;

// 각 날짜에 해당하는 목표와 목표달성률
public class GoalData {
    private String date; // 해당 날짜
    private String goal; // 해당하는 날짜의 목표치
    private String count; // 해당하는 날짜에 외운 단어 개수

    public GoalData() {
    }

    public GoalData(String date, String goal, String count) {
        this.date = date;
        this.goal = goal;
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public String getGoal() {
        return goal;
    }

    public String getCount() {
        return count;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
