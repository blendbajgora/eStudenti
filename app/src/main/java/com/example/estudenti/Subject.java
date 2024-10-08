// Subject.java
package com.example.estudenti;

public class Subject {
    private String name;
    private int mark;

    public Subject(String name, int mark) {
        this.name = name;
        this.mark = mark;
    }

    public String getName() {
        return name;
    }

    public int getMark() {
        return mark;
    }
}
