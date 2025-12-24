package com.example.api.dto;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public final class PlayerCreateRequest {
    private String login;
    private String password;
    private String screenName;
    private String role;
    private Integer age;
    private String gender;

    public String getLogin() {
        return login;
    }

    public PlayerCreateRequest setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public PlayerCreateRequest setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getScreenName() {
        return screenName;
    }

    public PlayerCreateRequest setScreenName(String screenName) {
        this.screenName = screenName;
        return this;
    }

    public String getRole() {
        return role;
    }

    public PlayerCreateRequest setRole(String role) {
        this.role = role;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public PlayerCreateRequest setAge(Integer age) {
        this.age = age;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public PlayerCreateRequest setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("login", login);
        params.put("password", password);
        params.put("screenName", screenName);
        params.put("role", role);
        params.put("age", age);
        params.put("gender", gender);
        return params;
    }

    public PlayerCreateRequest copy() {
        return new PlayerCreateRequest()
                .setAge(age)
                .setGender(gender)
                .setLogin(login)
                .setPassword(password)
                .setRole(role)
                .setScreenName(screenName);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerCreateRequest that = (PlayerCreateRequest) o;
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
