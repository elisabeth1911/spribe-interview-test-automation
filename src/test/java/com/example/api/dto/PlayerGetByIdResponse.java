package com.example.api.dto;

import java.util.Objects;

public final class PlayerGetByIdResponse {
    private Long id;
    private String login;
    private String password;
    private String screenName;
    private String role;
    private Integer age;
    private String gender;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerGetByIdResponse that = (PlayerGetByIdResponse) o;
        return Objects.equals(id, that.id)
                && Objects.equals(login, that.login)
                && Objects.equals(screenName, that.screenName)
                && Objects.equals(role, that.role)
                && Objects.equals(age, that.age)
                && Objects.equals(gender, that.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, screenName, role, age, gender);
    }
}
