package com.svetkin.optrou.service;

import com.svetkin.optrou.entity.FuelStation;
import com.svetkin.optrou.entity.FuelStationPrice;
import com.svetkin.optrou.entity.Refuelling;
import com.svetkin.optrou.entity.RefuellingPlan;
import com.svetkin.optrou.entity.Trip;
import com.svetkin.optrou.entity.TripFuelStation;
import com.svetkin.optrou.entity.Vehicle;
import com.svetkin.optrou.entity.type.FuelType;
import com.svetkin.optrou.repository.RefuellingPlanRepository;
import com.svetkin.optrou.repository.RefuellingRepository;
import com.svetkin.optrou.repository.RouteRepository;
import com.svetkin.optrou.repository.TripFuelStationRepository;
import com.svetkin.optrou.repository.TripRepository;
import io.jmix.core.FetchPlans;
import io.jmix.flowui.ViewNavigators;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Component(RefuellingPlanCreateService.NAME)
public class RefuellingPlanCreateService {

    public static final String NAME = "optrou_RefuellingPlanCreateService";

    private final RefuellingPlanRepository refuellingPlanRepository;
    private final RefuellingRepository refuellingRepository;

    public RefuellingPlanCreateService(RefuellingPlanRepository refuellingPlanRepository,
                                       RefuellingRepository refuellingRepository) {
        this.refuellingPlanRepository = refuellingPlanRepository;
        this.refuellingRepository = refuellingRepository;
    }

    public RefuellingPlan createRefuellingPlan(Trip trip) {
        double length = trip.getLength();

        Vehicle vehicle = trip.getVehicle();
        FuelType fuelType = vehicle.getFuelType();
        double capacity = vehicle.getCapacity();
        double remainingFuel = vehicle.getRemainingFuel();
        double fuelConsumption = vehicle.getFuelConsumption();
        double traveledDistance = 0;
        double volume;

        List<TripFuelStation> fuelStations = trip.getFuelStations().stream()
                .filter(fuelStation -> isExistsFuelType(fuelType, fuelStation))
                .toList();
        List<TripFuelStation> remainingFuelStations = getRemainingFuelStations(0, fuelType, fuelStations);
        TripFuelStation minimalPricePossibleTraveledFuelStation;
        TripFuelStation nextMinimalPricePossibleTraveledFuelStation;

        TripFuelStation basicTripFuelStation;
        double basicTripFuelStationDistance;

        RefuellingPlan refuellingPlan = refuellingPlanRepository.create();
        List<Refuelling> refuellings = new ArrayList<>();

        while (true) {
            basicTripFuelStation = remainingFuelStations.get(0);
            basicTripFuelStationDistance = basicTripFuelStation.getDistance();

            if (isPossibleTravel(traveledDistance, remainingFuel, fuelConsumption, length)) {
                return refuellingPlan;
            }

            if (isPossibleTravel(traveledDistance, remainingFuel, fuelConsumption, basicTripFuelStationDistance)) {
                volume = capacity - remainingFuel - ((basicTripFuelStationDistance - traveledDistance) / 100 * fuelConsumption);
                remainingFuel = capacity;
                traveledDistance = basicTripFuelStationDistance;

                refuellings.add(createRefuelling(volume, fuelType, basicTripFuelStation, refuellingPlan));

                remainingFuelStations = getRemainingFuelStations(traveledDistance, fuelType, fuelStations);
                continue;
            }

            minimalPricePossibleTraveledFuelStation = getMinimalPricePossibleTraveledFuelStation(
                    traveledDistance, remainingFuel, fuelConsumption, remainingFuelStations);

            if (minimalPricePossibleTraveledFuelStation == null) {
                throw new RuntimeException();
            }
            double minimalPricePossibleTraveledFuelStationDistance = minimalPricePossibleTraveledFuelStation.getDistance();

            if (isPossibleTravel(minimalPricePossibleTraveledFuelStationDistance, capacity, fuelConsumption, length)) {
                Refuelling refuelling = createRefuelling(
                        fuelAmountForLength(minimalPricePossibleTraveledFuelStationDistance, length, fuelConsumption),
                        fuelType,
                        minimalPricePossibleTraveledFuelStation,
                        refuellingPlan
                );
                refuellings.add(refuelling);
                return refuellingPlan;
            } else {
                nextMinimalPricePossibleTraveledFuelStation = getMinimalPricePossibleTraveledFuelStation(
                        minimalPricePossibleTraveledFuelStationDistance,
                        capacity,
                        fuelConsumption,
                        remainingFuelStations
                );

                if (nextMinimalPricePossibleTraveledFuelStation == null) {
                    throw new RuntimeException();
                }
            }

            remainingFuel = fuelAmountForLength(minimalPricePossibleTraveledFuelStationDistance, nextMinimalPricePossibleTraveledFuelStation.getDistance(), fuelConsumption);
            traveledDistance = minimalPricePossibleTraveledFuelStationDistance;
            remainingFuelStations = getRemainingFuelStations(traveledDistance, fuelType, fuelStations);

            Refuelling refuelling = createRefuelling(
                    fuelAmountForLength(minimalPricePossibleTraveledFuelStationDistance, nextMinimalPricePossibleTraveledFuelStation.getDistance(), fuelConsumption),
                    fuelType,
                    minimalPricePossibleTraveledFuelStation,
                    refuellingPlan
            );
            refuellings.add(refuelling);
        }
    }

    private Refuelling createRefuelling(double volume, FuelType fuelType, TripFuelStation fuelStation, RefuellingPlan refuellingPlan) {
        FuelStationPrice fuelStationPrice = getPriceForTripFuelStation(fuelType, fuelStation);

        Refuelling refuelling = refuellingRepository.create();
        refuelling.setVolume(volume);
        refuelling.setPrice(fuelStationPrice.getValue());
        refuelling.setFuelStationPrice(fuelStationPrice);
        refuelling.setRefuellingPlan(refuellingPlan);
        return refuelling;
    }

    private boolean isPossibleTravel(double traveledDistance, double remainingFuel, double fuelConsumption, double length) {
        return (traveledDistance + (remainingFuel / fuelConsumption) * 100) <= length;
    }

    private double fuelAmountForLength(double traveledDistance, double destinationDistance, double fuelConsumption) {
        return (destinationDistance - traveledDistance) / 100 * fuelConsumption;
    }

    private TripFuelStation getMinimalPricePossibleTraveledFuelStation(double traveledDistance,
                                                                       double remainingFuel,
                                                                       double fuelConsumption,
                                                                       List<TripFuelStation> fuelStations) {
        double possibleTraveledDistance = traveledDistance + remainingFuel / fuelConsumption * 100;

        for(TripFuelStation fuelStation : fuelStations) {
            if (fuelStation.getDistance() < possibleTraveledDistance) {
                return fuelStation;
            }
        };

        return null;
    }

    private List<TripFuelStation> getRemainingFuelStations(double traveledDistance, FuelType fuelType, List<TripFuelStation> fuelStations) {
        return fuelStations.stream()
                .filter(tripFuelStation -> tripFuelStation.getDistance() > traveledDistance)
                .sorted(Comparator.comparing(tripFuelStation -> getPriceForTripFuelStation(fuelType, tripFuelStation).getValue()))
                .toList();
    }

    private FuelStationPrice getPriceForTripFuelStation(FuelType fuelType, TripFuelStation tripFuelStation) {
        return tripFuelStation.getFuelStation().getPrices().stream()
                .filter(price -> Objects.equals(fuelType, price.getFuelType()))
                .findFirst()
                .get();
    }

    private boolean isExistsFuelType(FuelType fuelType, TripFuelStation fuelStation) {
        return fuelStation.getFuelStation().getPrices().stream()
                .anyMatch(price -> Objects.equals(fuelType, price.getFuelType()));
    }
}