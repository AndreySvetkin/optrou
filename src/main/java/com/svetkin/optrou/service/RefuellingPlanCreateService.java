package com.svetkin.optrou.service;

import com.svetkin.optrou.entity.FuelStation;
import com.svetkin.optrou.entity.FuelStationPrice;
import com.svetkin.optrou.entity.Refuelling;
import com.svetkin.optrou.entity.RefuellingPlan;
import com.svetkin.optrou.entity.Trip;
import com.svetkin.optrou.entity.TripFuelStation;
import com.svetkin.optrou.entity.Vehicle;
import com.svetkin.optrou.entity.dto.RefuellingPlanDto;
import com.svetkin.optrou.entity.type.FuelType;
import com.svetkin.optrou.entity.type.RefuellingPlanCreateStatus;
import com.svetkin.optrou.repository.RefuellingPlanRepository;
import com.svetkin.optrou.repository.RefuellingRepository;
import com.svetkin.optrou.repository.RouteRepository;
import com.svetkin.optrou.repository.TripFuelStationRepository;
import com.svetkin.optrou.repository.TripRepository;
import io.jmix.core.FetchPlans;
import io.jmix.core.Metadata;
import io.jmix.flowui.ViewNavigators;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Component(RefuellingPlanCreateService.NAME)
public class RefuellingPlanCreateService {

    public static final String NAME = "optrou_RefuellingPlanCreateService";

    private final RefuellingPlanRepository refuellingPlanRepository;
    private final RefuellingRepository refuellingRepository;
    private final Metadata metadata;

    public RefuellingPlanCreateService(RefuellingPlanRepository refuellingPlanRepository,
                                       RefuellingRepository refuellingRepository, Metadata metadata) {
        this.refuellingPlanRepository = refuellingPlanRepository;
        this.refuellingRepository = refuellingRepository;
        this.metadata = metadata;
    }

    public RefuellingPlanDto createRefuellingPlan(Trip trip) {
        double length = trip.getLength();

        Vehicle vehicle = trip.getVehicle();
        FuelType fuelType = vehicle.getFuelType();
        double capacity = vehicle.getCapacity();
        double remainingFuel = vehicle.getRemainingFuel();
        double fuelConsumption = vehicle.getFuelConsumption();
        double traveledDistance = 0;
        double volume;

        RefuellingPlan refuellingPlan = refuellingPlanRepository.create();
        refuellingPlan.setTrip(trip);
        refuellingPlan.setFuelType(fuelType);
        RefuellingPlanDto refuellingPlanDto = metadata.create(RefuellingPlanDto.class);
        refuellingPlanDto.setRefuellingPlan(refuellingPlan);

        List<TripFuelStation> fuelStations = trip.getFuelStations().stream()
                .filter(fuelStation -> isExistsFuelType(fuelType, fuelStation))
                .toList();
        List<TripFuelStation> remainingFuelStations = getRemainingFuelStations(0, fuelType, fuelStations);
        TripFuelStation minimalPricePossibleTraveledFuelStation;
        TripFuelStation nextMinimalPricePossibleTraveledFuelStation;

        if (CollectionUtils.isEmpty(remainingFuelStations)) {
            refuellingPlanDto.setStatus(RefuellingPlanCreateStatus.IS_EMPTY_FUEL_STATIONS_WITH_FUEL_TYPE);
            return refuellingPlanDto;
        }

        TripFuelStation basicTripFuelStation;
        double basicTripFuelStationDistance;

        List<Refuelling> refuellings = new ArrayList<>();
        refuellingPlan.setRefuellings(refuellings);

        while (true) {
            if (isPossibleTravel(traveledDistance, remainingFuel, fuelConsumption, length)) {
                refuellingPlanDto.setStatus(RefuellingPlanCreateStatus.DONE);
                return refuellingPlanDto;
            }

            if (CollectionUtils.isNotEmpty(remainingFuelStations)) {
                basicTripFuelStation = remainingFuelStations.get(0);
                basicTripFuelStationDistance = basicTripFuelStation.getDistance();
            } else {
                FuelStation lastFuelStation = CollectionUtils.isNotEmpty(refuellings)
                        ? refuellings.get(refuellings.size() - 1).getFuelStationPrice().getFuelStation()
                        : null;
                refuellingPlanDto.setLastFuelStation(lastFuelStation);
                refuellingPlanDto.setStatus(RefuellingPlanCreateStatus.NEXT_STATION_OR_END_ROUTE_FAR_AWAY);
                return refuellingPlanDto;
            }

            if (isPossibleTravel(traveledDistance, remainingFuel, fuelConsumption, basicTripFuelStationDistance)) {
                volume = capacity - (remainingFuel - fuelAmountForLength(traveledDistance, basicTripFuelStationDistance, fuelConsumption));
                remainingFuel = capacity;
                traveledDistance = basicTripFuelStationDistance;

                refuellings.add(createRefuelling(volume, fuelType, basicTripFuelStation, refuellingPlan));

                remainingFuelStations = getRemainingFuelStations(traveledDistance, fuelType, fuelStations);
                continue;
            }

            minimalPricePossibleTraveledFuelStation = getMinimalPricePossibleTraveledFuelStation(
                    traveledDistance, remainingFuel, fuelConsumption, remainingFuelStations);

            if (minimalPricePossibleTraveledFuelStation == null) {
                FuelStation lastFuelStation = CollectionUtils.isNotEmpty(refuellings)
                        ? refuellings.get(refuellings.size() - 1).getFuelStationPrice().getFuelStation()
                        : null;
                refuellingPlanDto.setLastFuelStation(lastFuelStation);
                refuellingPlanDto.setStatus(RefuellingPlanCreateStatus.NEXT_STATION_OR_END_ROUTE_FAR_AWAY);
                return refuellingPlanDto;
            }

            double minimalPricePossibleTraveledFuelStationDistance = minimalPricePossibleTraveledFuelStation.getDistance();

            nextMinimalPricePossibleTraveledFuelStation = getMinimalPricePossibleTraveledFuelStation(
                    minimalPricePossibleTraveledFuelStationDistance,
                    capacity,
                    fuelConsumption,
                    remainingFuelStations
            );

            if (nextMinimalPricePossibleTraveledFuelStation == null) {
                refuellingPlanDto.setLastFuelStation(minimalPricePossibleTraveledFuelStation.getFuelStation());
                refuellingPlanDto.setStatus(RefuellingPlanCreateStatus.NEXT_STATION_OR_END_ROUTE_FAR_AWAY);
                return refuellingPlanDto;
            }

            volume = fuelAmountForLength(minimalPricePossibleTraveledFuelStationDistance, nextMinimalPricePossibleTraveledFuelStation.getDistance(), fuelConsumption)
                    + fuelAmountForLength(traveledDistance, minimalPricePossibleTraveledFuelStationDistance, fuelConsumption)
                    - remainingFuel;
            Refuelling refuelling = createRefuelling(volume, fuelType, minimalPricePossibleTraveledFuelStation, refuellingPlan);
            refuellings.add(refuelling);

            remainingFuel = fuelAmountForLength(minimalPricePossibleTraveledFuelStationDistance, nextMinimalPricePossibleTraveledFuelStation.getDistance(), fuelConsumption);
            traveledDistance = minimalPricePossibleTraveledFuelStationDistance;
            remainingFuelStations = getRemainingFuelStations(traveledDistance, fuelType, fuelStations);
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
        return (traveledDistance + (remainingFuel / fuelConsumption) * 100) >= length;
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
            if (fuelStation.getDistance() <= possibleTraveledDistance) {
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