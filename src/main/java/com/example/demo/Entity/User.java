package com.example.demo.Entity;


public class User {
    private Long id; // Unique identifier of the user (usually primary key in DB)
    private String name;
    private String email;
    private String password;//password (should be hashed in real apps)

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;

    }

    public User(){

    }

     //Partial constructor.
    //Used when only name and email are needed
    //  Parameterized Constructor
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    //Full constructor
    public User(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;

    }

    //Getters and Setters
    //Used to READ the identifier
    public Long getId() {
        return id;
    }

    //Used when DB returns generated id or during mapping
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    //Getter for password.
    //normally you should NOT expose a password like this.
    public String getPassword() {
        return password;
    }


     //Setter for password.
     //Should accept already-hashed password in real systems.
    public void setPassword(String password) {
        this.password = password;
    }
}
