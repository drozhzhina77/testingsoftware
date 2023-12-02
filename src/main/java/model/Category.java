package model;

import java.util.ArrayList;

public class Category {
    private String name;
    private ArrayList<Word> word = new ArrayList<>();

    public Category() {
        ArrayList<Word> word = new ArrayList<>();
    }

    public Category(String name) { //агрегация
        this.name = name;
        ArrayList<Word> word = new ArrayList<>();
    }

    public Category(String name, Word word) { //агрегация
        this.name = name;
        this.word.add(word);
    }

    public void addWordIntoCategory(Word word) {
        this.word.add(word);
    }

    public ArrayList<Word> getListWord() {
        return word;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
