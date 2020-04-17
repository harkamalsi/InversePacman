package com.mygdx.game.utils;

public class Node {

    private int cost;
    private String type;
    private boolean walkThrough;

    public Node(int cost, String type, boolean walkThrough) {
        this.cost = cost;
        this.type = type;
        this.walkThrough = walkThrough;
    }

    public int getCost() {
        return cost;
    }

    public String getType() {
        return type;
    }

    public boolean isWalkThrough() {
        return walkThrough;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setWalkThrough(boolean walkThrough) {
        this.walkThrough = walkThrough;
    }
}
