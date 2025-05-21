package com.svetkin.optrou.security;

import com.svetkin.optrou.entity.Driver;
import com.svetkin.optrou.entity.DriverLicenceCategoryRelation;
import com.svetkin.optrou.entity.FactRefuelling;
import com.svetkin.optrou.entity.FactRefuellingPlan;
import com.svetkin.optrou.entity.FuelStation;
import com.svetkin.optrou.entity.FuelStationBrand;
import com.svetkin.optrou.entity.FuelStationPrice;
import com.svetkin.optrou.entity.Refuelling;
import com.svetkin.optrou.entity.RefuellingPlan;
import com.svetkin.optrou.entity.Route;
import com.svetkin.optrou.entity.RouteFuelStation;
import com.svetkin.optrou.entity.RoutePoint;
import com.svetkin.optrou.entity.Trip;
import com.svetkin.optrou.entity.TripFuelStation;
import com.svetkin.optrou.entity.TripPoint;
import com.svetkin.optrou.entity.TripReport;
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

@ResourceRole(name = "LogistRole", code = LogistRole.CODE)
public interface LogistRole {
    String CODE = "logist-role";

    @EntityPolicy(
            entityClass = Trip.class,
            actions = {EntityPolicyAction.ALL})
    @EntityAttributePolicy(
            entityClass = Trip.class,
            attributes = "*",
            action = EntityAttributePolicyAction.MODIFY)
    @ViewPolicy(
            viewIds = {"optrou_Trip.list", "optrou_Trip.detail"})
    @MenuPolicy(
            menuIds = {"optrou_Trip.list"})
    void trip();

    @EntityPolicy(
            entityClass = TripReport.class,
            actions = {EntityPolicyAction.ALL})
    @EntityAttributePolicy(
            entityClass = TripReport.class,
            attributes = "*",
            action = EntityAttributePolicyAction.MODIFY)
    @ViewPolicy(
            viewIds = {"optrou_TripReport.list", "optrou_TripReport.detail"})
    @MenuPolicy(
            menuIds = {"optrou_TripReport.list"})
    void tripReport();

    @EntityPolicy(
            entityClass = Route.class,
            actions = {EntityPolicyAction.ALL})
    @EntityAttributePolicy(
            entityClass = Route.class,
            attributes = "*",
            action = EntityAttributePolicyAction.MODIFY)
    @ViewPolicy(
            viewIds = {"optrou_Route.list", "optrou_Route.detail"})
    @MenuPolicy(
            menuIds = {"optrou_Route.list"})
    void route();

    @EntityPolicy(
            entityClass = Driver.class,
            actions = {EntityPolicyAction.ALL})
    @EntityAttributePolicy(
            entityClass = Driver.class,
            attributes = "*",
            action = EntityAttributePolicyAction.MODIFY)
    @ViewPolicy(
            viewIds = {"optrou_Driver.list", "optrou_Driver.detail"})
    @MenuPolicy(
            menuIds = {"optrou_Driver.list"})
    void driver();

    @EntityPolicy(
            entityClass = FuelStationBrand.class,
            actions = {EntityPolicyAction.ALL})
    @EntityAttributePolicy(
            entityClass = FuelStationBrand.class,
            attributes = "*",
            action = EntityAttributePolicyAction.MODIFY)
    @ViewPolicy(
            viewIds = {"optrou_FuelStationBrand.list", "optrou_FuelStationBrand.detail"})
    @MenuPolicy(
            menuIds = {"optrou_FuelStationBrand.list"})
    void fuelStationBrand();

    @EntityPolicy(
            entityClass = DriverLicenceCategoryRelation.class,
            actions = {EntityPolicyAction.ALL})
    @EntityAttributePolicy(
            entityClass = DriverLicenceCategoryRelation.class,
            attributes = "*",
            action = EntityAttributePolicyAction.MODIFY)
    void driverLicenseCategoryRelation();

    @EntityPolicy(
            entityClass = TripFuelStation.class,
            actions = {EntityPolicyAction.ALL})
    @EntityAttributePolicy(
            entityClass = TripFuelStation.class,
            attributes = "*",
            action = EntityAttributePolicyAction.MODIFY)
    void tripFuelStation();

    @EntityPolicy(
            entityClass = RouteFuelStation.class,
            actions = {EntityPolicyAction.ALL})
    @EntityAttributePolicy(
            entityClass = RouteFuelStation.class,
            attributes = "*",
            action = EntityAttributePolicyAction.MODIFY)
    void routeFuelStation();

    @EntityPolicy(
            entityClass = FuelStation.class,
            actions = {EntityPolicyAction.ALL})
    @EntityAttributePolicy(
            entityClass = FuelStation.class,
            attributes = "*",
            action = EntityAttributePolicyAction.MODIFY)
    @ViewPolicy(
            viewIds = {"optrou_FuelStation.list", "optrou_FuelStation.detail"})
    @MenuPolicy(
            menuIds = {"optrou_FuelStation.list"})
    void fuelStation();

    @EntityPolicy(
            entityClass = FuelStationPrice.class,
            actions = {EntityPolicyAction.ALL})
    @EntityAttributePolicy(
            entityClass = FuelStationPrice.class,
            attributes = "*",
            action = EntityAttributePolicyAction.MODIFY)
    @ViewPolicy(
            viewIds = {"optrou_FuelStationPrice.detail"})
    void fuelStationPrice();

    @EntityPolicy(
            entityClass = RefuellingPlan.class,
            actions = {EntityPolicyAction.ALL})
    @EntityAttributePolicy(
            entityClass = RefuellingPlan.class,
            attributes = "*",
            action = EntityAttributePolicyAction.MODIFY)
    @ViewPolicy(
            viewIds = "optrou_RefuellingPlan.detail")
    void refuellingPlan();

    @EntityPolicy(
            entityClass = Refuelling.class,
            actions = {EntityPolicyAction.ALL})
    @EntityAttributePolicy(
            entityClass = Refuelling.class,
            attributes = "*",
            action = EntityAttributePolicyAction.MODIFY)
    @ViewPolicy(
            viewIds = "optrou_Refuelling.detail")
    void refuelling();

    @EntityPolicy(
            entityClass = TripPoint.class,
            actions = {EntityPolicyAction.ALL})
    @EntityAttributePolicy(
            entityClass = TripPoint.class,
            attributes = "*",
            action = EntityAttributePolicyAction.MODIFY)
    void tripPoint();

    @EntityPolicy(
            entityClass = Vehicle.class,
            actions = {EntityPolicyAction.ALL})
    @EntityAttributePolicy(
            entityClass = Vehicle.class,
            attributes = "*",
            action = EntityAttributePolicyAction.MODIFY)
    @ViewPolicy(
            viewIds = {"optrou_Vehicle.list", "optrou_Vehicle.detail"})
    @MenuPolicy(
            menuIds = {"optrou_Vehicle.list"})
    void vehicle();

    @EntityPolicy(
            entityClass = RoutePoint.class,
            actions = {EntityPolicyAction.ALL})
    @EntityAttributePolicy(
            entityClass = RoutePoint.class,
            attributes = "*",
            action = EntityAttributePolicyAction.MODIFY)
    void routePoint();

    @EntityPolicy(
            entityClass = FactRefuellingPlan.class,
            actions = {EntityPolicyAction.ALL})
    @EntityAttributePolicy(
            entityClass = FactRefuellingPlan.class,
            attributes = "*",
            action = EntityAttributePolicyAction.MODIFY)
    void factRefuellingPlan();

    @EntityPolicy(
            entityClass = FactRefuelling.class,
            actions = {EntityPolicyAction.ALL})
    @EntityAttributePolicy(
            entityClass = FactRefuelling.class,
            attributes = "*",
            action = EntityAttributePolicyAction.MODIFY)
    void factRefuelling();

    @EntityPolicy(
            entityClass = User.class,
            actions = {EntityPolicyAction.READ})
    @EntityAttributePolicy(
            entityClass = User.class,
            attributes = "*",
            action = EntityAttributePolicyAction.VIEW)
    void user();

    @SpecificPolicy(
            resources = {"trip.changeStatus"})
    void tripChangeStatus();
}