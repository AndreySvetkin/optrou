package com.svetkin.optrou.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.jmix.core.metamodel.annotation.JmixEntity;

@JmixEntity(name = "optrou_GlonasssoftAuthenticationDto")
public class GlonassSoftAuthenticationDto {

    @JsonProperty("AuthId")
    private String token;

    private String user;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}