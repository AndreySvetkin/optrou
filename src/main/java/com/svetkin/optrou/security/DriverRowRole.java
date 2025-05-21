package com.svetkin.optrou.security;

import com.svetkin.optrou.entity.Trip;
import io.jmix.security.role.annotation.JpqlRowLevelPolicy;
import io.jmix.security.role.annotation.RowLevelRole;

@RowLevelRole(name = "DriverRowRole", code = DriverRowRole.CODE)
public interface DriverRowRole {
    String CODE = "driver-row-role";

    @JpqlRowLevelPolicy(
            entityClass = Trip.class,
            where = "{E}.driver.user.username = :current_user_username")
    void customer();
}