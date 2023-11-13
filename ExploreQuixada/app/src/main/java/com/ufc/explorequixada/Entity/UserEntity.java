package com.ufc.explorequixada.Entity;

import java.util.UUID;

public class UserEntity {

    private UUID id;
    private String username;
    private String email;
    private String password;
    public UserEntity(String username, String email, String password) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public UserEntity(){this.id = UUID.randomUUID();};

    public UUID getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                ", name='" + username + '\'' +
                '}';
    }
}
