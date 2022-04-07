package com.example.mycourse.Modal;

import java.util.ArrayList;

public class Goods {
    private String name;
    private ArrayList<String> cost_duration = new ArrayList<>();
    private ArrayList<String> cost_num = new ArrayList<>();

    public Goods(String name, ArrayList<String> cost_duration, ArrayList<String> cost_num) {
        this.name = name;
        this.cost_duration = cost_duration;
        this.cost_num = cost_num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getCost_duration() {
        return cost_duration;
    }

    public ArrayList<String> getCost_num() {
        return cost_num;
    }
}
