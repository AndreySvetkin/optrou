package com.svetkin.optrou.view.tripreport;

import com.svetkin.optrou.entity.Trip;
import com.svetkin.optrou.entity.TripFuelStation;
import com.svetkin.optrou.entity.TripPoint;
import com.svetkin.optrou.entity.TripReport;
import com.svetkin.optrou.view.main.MainView;
import com.svetkin.optrou.view.mapfragment.MapFragment;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.model.CollectionPropertyContainer;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.model.InstancePropertyContainer;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import io.jmix.mapsflowui.component.model.layer.VectorLayer;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineString;

@Route(value = "tripReports/:id", layout = MainView.class)
@ViewController(id = "optrou_TripReport.detail")
@ViewDescriptor(path = "trip-report-detail-view.xml")
@EditedEntityContainer("tripReportDc")
public class TripReportDetailView extends StandardDetailView<TripReport> {

    @ViewComponent
    private MapFragment mapFragment;
    @ViewComponent
    private CollectionPropertyContainer<TripFuelStation> tripFuelStationsDc;
    @ViewComponent
    private CollectionPropertyContainer<TripPoint> controlPointsDc;
    @ViewComponent
    private InstanceContainer<TripReport> tripReportDc;

    @Subscribe
    public void onInit(final InitEvent event) {
        VectorLayer routeVectorLayer = mapFragment.addVectorLayerWithDataVectorSource(tripReportDc, "line");
        VectorLayer factRouteVectorLayer = mapFragment.addVectorLayerWithDataVectorSource(tripReportDc, "factLine");
        VectorLayer controlPointsVector = mapFragment.addVectorLayerWithDataVectorSource(controlPointsDc, "location");
        VectorLayer fuelStationsVectorLayer = mapFragment.addVectorLayerWithDataVectorSource(tripFuelStationsDc, "fuelStation.location");

        mapFragment.setLineStringStyleProvider(routeVectorLayer);
        mapFragment.setControlPointStyleProvider(factRouteVectorLayer);
        mapFragment.setControlPointStyleProvider(controlPointsVector);
        mapFragment.setFuelStationStyleProvider(fuelStationsVectorLayer);
    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        LineString routeLine = getEditedEntity().getLine();
        if (routeLine != null) {
            setMapCenterByLine(routeLine);
        }
    }

    private void setMapCenterByLine(LineString line) {
        mapFragment.setCenter(new Coordinate(line.getCoordinateN(0)));
        mapFragment.setZoom(10.0);
    }
}