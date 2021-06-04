package com.example.todo_app;

import java.util.Comparator;

public class Item  {

    private int id;
    private String title;
    private String date;
    private String textNote;

    public Item(int id, String title, String date, String textNote) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.textNote = textNote;
    }


    public static Comparator<Item> alphaCompare = new Comparator<Item>() {
        @Override
        public int compare(Item o1, Item o2) {
            return o1.getTitle().compareTo(o2.title);
        }
    };

    public static Comparator<Item> dateCompare = new Comparator<Item>() {
        @Override
        public int compare(Item o1, Item o2) {
            return o1.getDate().compareTo(o2.date);
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTextNote() {
        return textNote;
    }

    public void setTextNote(String textNote) {
        this.textNote = textNote;
    }


}
