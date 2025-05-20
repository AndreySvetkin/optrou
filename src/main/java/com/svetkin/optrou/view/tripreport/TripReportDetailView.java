package com.svetkin.optrou.view.tripreport;

import com.svetkin.optrou.entity.Trip;
import com.svetkin.optrou.entity.TripFuelStation;
import com.svetkin.optrou.entity.TripPoint;
import com.svetkin.optrou.entity.TripReport;
import com.svetkin.optrou.view.main.MainView;
import com.svetkin.optrou.view.mapfragment.MapFragment;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.action.view.DetailSaveCloseAction;
import io.jmix.flowui.model.CollectionPropertyContainer;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.model.InstanceContainer;
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
    @ViewComponent
    private DataContext dataContext;
    @ViewComponent
    private DetailSaveCloseAction<Object> saveAction;

    public void setTripReport(TripReport tripReport) {
        dataContext.clear();
        tripReport = dataContext.merge(tripReport);
        tripReportDc.setItem(tripReport);
    }

    @Subscribe
    public void onInit(final InitEvent event) {
        VectorLayer routeVectorLayer = mapFragment.addVectorLayerWithDataVectorSource(tripReportDc, "line");
        VectorLayer factRouteVectorLayer = mapFragment.addVectorLayerWithDataVectorSource(tripReportDc, "factLine");
        VectorLayer controlPointsVector = mapFragment.addVectorLayerWithDataVectorSource(controlPointsDc, "location");
        VectorLayer fuelStationsVectorLayer = mapFragment.addVectorLayerWithDataVectorSource(tripFuelStationsDc, "fuelStation.location");

        mapFragment.setLineStringStyleProvider(routeVectorLayer);
        mapFragment.setFactLineStringStyleProvider(factRouteVectorLayer);
        mapFragment.setControlPointStyleProvider(controlPointsVector);
        mapFragment.setFuelStationStyleProvider(fuelStationsVectorLayer);

        mapFragment.addSelectedGeoObjectTextProvider(routeVectorLayer.getSource(), TripReport.getLineTooltipTextProviderFunction());
        mapFragment.addSelectedGeoObjectTextProvider(factRouteVectorLayer.getSource(), TripReport.getFactLineTooltipTextProviderFunction());
        mapFragment.addSelectedGeoObjectTextProvider(controlPointsVector.getSource(), Trip.getPointTooltipTextProviderFunction());
        mapFragment.addSelectedGeoObjectTextProvider(fuelStationsVectorLayer.getSource(), Trip.getFuelStationTooltipTextProviderFunction());
    }

    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
        LineString routeLine = getEditedEntity().getLine();
        if (routeLine != null) {
            setMapCenterByLine(routeLine);
        }
    }

    @Subscribe
    public void onReady(final ReadyEvent event) {
        if (getEditedEntity().getTrip() == null) {
            closeWithDefaultAction();
        }
    }

    private void setMapCenterByLine(LineString line) {
        mapFragment.setCenter(new Coordinate(line.getCoordinateN(0)));
        mapFragment.setZoom(10.0);
    }
}