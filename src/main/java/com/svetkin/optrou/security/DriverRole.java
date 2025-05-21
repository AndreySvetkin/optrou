package com.svetkin.optrou.security;

import com.svetkin.optrou.entity.Driver;
import com.svetkin.optrou.entity.FuelStation;
import com.svetkin.optrou.entity.FuelStationPrice;
import com.svetkin.optrou.entity.Refuelling;
import com.svetkin.optrou.entity.RefuellingPlan;
import com.svetkin.optrou.entity.Route;
import com.svetkin.optrou.entity.Trip;
import com.svetkin.optrou.entity.TripFuelStation;
import com.svetkin.optrou.entity.TripPoint;
import com.svetkin.optrou.entity.User;
import com.svetkin.optrou.entity.Vehicle;
import io.jmix.security.model.EntityAttributePolicyAction;
import io.jmix.security.model.EntityPolicyAction;
import io.jmix.security.role.annotation.EntityAttributePolicy;
import io.jmix.security.role.annotation.EntityPolicy;
import io.jmix.security.role.annotation.ResourceRole;
import io.jmix.security.role.annotation.SpecificPolicy;
import io.jmix.securityflowui.role.annotation.MenuPolicy;
import io.jmix.securityflowui.role.annotation.ViewPolicy;

@ResourceRole(name = "DriverRole", code = DriverRole.CODE)
public interface DriverRole {
    String CODE = "driver-role";

    @EntityPolicy(
            entityClass = Trip.class,
            actions = {EntityPolicyAction.READ,
                    EntityPolicyAction.UPDATE})
    @EntityAttributePolicy(
            entityClass = Trip.class,
            attributes = "*",
            action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(
            entityClass = Trip.class,
            attributes = {"status"},
            action = EntityAttributePolicyAction.MODIFY)
    @ViewPolicy(
            viewIds = {"optrou_Trip.list", "optrou_Trip.detail"})
    @MenuPolicy(
            menuIds = {"optrou_Trip.list"})
    void trip();

    @EntityPolicy(
            entityClass = Route.class,
            actions = {EntityPolicyAction.READ})
    @EntityAttributePolicy(
            entityClass = Route.class,
            attributes = "*",
            action = EntityAttributePolicyAction.VIEW)
    void route();

    @EntityPolicy(
            entityClass = Driver.class,
            actions = {EntityPolicyAction.READ})
    @EntityAttributePolicy(
            entityClass = Driver.class,
            attributes = "*",
            action = EntityAttributePolicyAction.VIEW)
    void driver();

    @EntityPolicy(
            entityClass = TripFuelStation.class,
            actions = {EntityPolicyAction.READ})
    @EntityAttributePolicy(
            entityClass = TripFuelStation.class,
            attributes = "*",
            action = EntityAttributePolicyAction.VIEW)
    void tripFuelStation();

    @EntityPolicy(
            entityClass = FuelStation.class,
            actions = {EntityPolicyAction.READ})
    @EntityAttributePolicy(
            entityClass = FuelStation.class,
            attributes = "*",
            action = EntityAttributePolicyAction.VIEW)
    void fuelStation();

    @EntityPolicy(
            entityClass = FuelStationPrice.class,
            actions = {EntityPolicyAction.READ})
    @EntityAttributePolicy(
            entityClass = FuelStationPrice.class,
            attributes = "*",
            action = EntityAttributePolicyAction.VIEW)
    void fuelStationPrice();

    @EntityPolicy(
            entityClass = RefuellingPlan.class,
            actions = {EntityPolicyAction.READ})
    @EntityAttributePolicy(
            entityClass = RefuellingPlan.class,
            attributes = "*",
            action = EntityAttributePolicyAction.VIEW)
    @ViewPolicy(
            viewIds = "optrou_RefuellingPlan.detail")
    void refuellingPlan();

    @EntityPolicy(
            entityClass = Refuelling.class,
            actions = {EntityPolicyAction.READ})
    @EntityAttributePolicy(
            entityClass = Refuelling.class,
            attributes = "*",
            action = EntityAttributePolicyAction.VIEW)
    void refuelling();

    @EntityPolicy(
            entityClass = TripPoint.class,
            actions = {EntityPolicyAction.READ})
    @EntityAttributePolicy(
            entityClass = TripPoint.class,
            attributes = "*",
            action = EntityAttributePolicyAction.VIEW)
    void tripPoint();

    @EntityPolicy(
            entityClass = Vehicle.class,
            actions = {EntityPolicyAction.READ,
                    EntityPolicyAction.UPDATE})
    @EntityAttributePolicy(
            entityClass = Vehicle.class,
            attributes = "*",
            action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(
            entityClass = Vehicle.class,
            attributes = "remainingFuel",
            action = EntityAttributePolicyAction.VIEW)
    void vehicle();

    @EntityPolicy(
            entityClass = User.class,
            actions = {EntityPolicyAction.READ})
    @EntityAttributePolicy(
            entityClass = User.class,
            attributes = "*",
            action = EntityAttributePolicyAction.VIEW)
    void logist();

    @SpecificPolicy(
            resources = {"trip.changeStatus"})
    void tripChangeStatus();
}