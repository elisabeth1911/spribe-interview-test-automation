package com.example.api.dto;

import java.util.Objects;

public final class PlayerUpdateRequest {
    private String login;
    private String password;
    private String screenName;
    private String role;
    private Integer age;
    private String gender;

    public String getLogin() {
        return login;
    }

    public PlayerUpdateRequest setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public PlayerUpdateRequest setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getScreenName() {
        return screenName;
    }

    public PlayerUpdateRequest setScreenName(String screenName) {
        this.screenName = screenName;
        return this;
    }

    public String getRole() {
        return role;
    }

    public PlayerUpdateRequest setRole(String role) {
        this.role = role;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public PlayerUpdateRequest setAge(Integer age) {
        this.age = age;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public PlayerUpdateRequest setGender(String gender) {
        this.gender = gender;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerUpdateRequest that = (PlayerUpdateRequest) o;
        return Objects.equals(login, that.login)
                && Objects.equals(password, that.password)
                && Objects.equals(screenName, that.screenName)
                && Objects.equals(role, that.role)
                && Objects.equals(age, that.age)
                && Objects.equals(gender, that.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password, screenName, role, age, gender);
    }
}
