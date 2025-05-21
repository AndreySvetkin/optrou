package com.svetkin.optrou.listener;

import com.svetkin.optrou.entity.Trip;
import io.jmix.core.event.EntitySavingEvent;
import io.jmix.data.Sequence;
import io.jmix.data.Sequences;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component("optrou_TripEventListener")
public class TripEventListener {

    private final Sequences sequences;

    public TripEventListener(Sequences sequences) {
        this.sequences = sequences;
    }

    @EventListener
    public void onTripSaving(final EntitySavingEvent<Trip> event) {
        Trip trip = event.getEntity();
        if (trip.getNumber() == null) {
            trip.setNumber(sequences.createNextValue(Sequence.withName("trip")));
        }
    }
}