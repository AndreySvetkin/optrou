package com.svetkin.optrou.listener;

import com.svetkin.optrou.entity.Route;
import io.jmix.core.event.EntitySavingEvent;
import io.jmix.data.Sequence;
import io.jmix.data.Sequences;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component("optrou_RouteEventListener")
public class RouteEventListener {

    private final Sequences sequences;

    public RouteEventListener(Sequences sequences) {
        this.sequences = sequences;
    }

    @EventListener
    public void onRouteSaving(final EntitySavingEvent<Route> event) {
        Route route = event.getEntity();
        if (route.getNumber() != null) {
            route.setNumber(sequences.createNextValue(Sequence.withName("route")));
        }
    }
}