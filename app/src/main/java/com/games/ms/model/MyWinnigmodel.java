package com.games.ms.model;

public class MyWinnigmodel {


    public  String id;
    public  String table_id;
    public  String amount;
    public  String winner_id;
    public  String name;
    public  String totalwin;
    public  String userimage;

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getTotalwin() {
        return totalwin;
    }

    public void setTotalwin(String totalwin) {
        this.totalwin = totalwin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTable_id() {
        return table_id;
    }

    public void setTable_id(String table_id) {
        this.table_id = table_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getWinner_id() {
        return winner_id;
    }

    public void setWinner_id(String winner_id) {
        this.winner_id = winner_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
