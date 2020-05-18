package com.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserDto {

    private Long id;
    private String name;
    private List<String> roles;

    public UserDto(long id) {
        this.id = id;
    }

    public UserDto() {
        this.roles = new ArrayList<>();
        this.roles.add("ROLE_USER");
    }

    public UserDto(String name, String password, String... roles) {
        this.name = name;
        this.roles = new ArrayList<>(Arrays.asList(roles));
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
