package com.bob.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Roy on 13-08-14.
 */
@Entity
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String login;
    private String password;

    public User() {}

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }
}
