package com.example.api.dto;

import java.util.Objects;

public final class PlayerItem {
    private Long id;
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
        PlayerItem playerItem = (PlayerItem) o;
        return Objects.equals(id, playerItem.id)
                && Objects.equals(screenName, playerItem.screenName)
                && Objects.equals(role, playerItem.role)
                && Objects.equals(age, playerItem.age)
                && Objects.equals(gender, playerItem.gender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, screenName, role, age, gender);
    }
}
