package com.example.estudenti;

public class Users {

    private int id;
    private String fullname;
    private String username;
    private String password;
    private String DOB;
    private String phoneNumber;

    public Users(int id, String fullname, String username, String password, String DOB, String phoneNumber) {
        this.id = id;
        this.fullname = fullname;
        this.username = username;
        this.password = password;
        this.DOB = DOB;
        this.phoneNumber = phoneNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
