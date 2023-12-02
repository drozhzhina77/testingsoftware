package model;

import java.util.ArrayList;

public class Results {
    private User user;
    private ArrayList<String> namesCateg = new ArrayList<String>();
    private int mark;

    public Results(){
        this.user = new User();
        this.namesCateg = new ArrayList<String>();
        this.mark = 2;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<String> getNamesCateg() {
        return namesCateg;
    }

    public void setNamesCateg(ArrayList<String> namesCateg) {
        this.namesCateg = namesCateg;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

}
