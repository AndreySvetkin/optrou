package com.svetkin.optrou.entity.dto;

import io.jmix.core.metamodel.annotation.JmixEntity;

@JmixEntity(name = "optrou_GlonassSoftAuthenticationRequestDto")
public class GlonassSoftAuthenticationRequestDto {

    private String login;

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}