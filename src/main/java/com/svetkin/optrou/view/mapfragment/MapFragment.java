package com.svetkin.optrou.view.mapfragment;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.model.InstanceContainer;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.mapsflowui.component.GeoMap;
import io.jmix.mapsflowui.component.data.ContainerDataVectorSourceItems;
import io.jmix.mapsflowui.component.model.layer.VectorLayer;
import io.jmix.mapsflowui.component.model.source.ClusterSource;
import io.jmix.mapsflowui.component.model.source.DataVectorSource;
import io.jmix.mapsflowui.kit.component.model.style.Style;
import io.jmix.mapsflowui.kit.component.model.style.image.Anchor;
import io.jmix.mapsflowui.kit.component.model.style.image.IconOrigin;
import io.jmix.mapsflowui.kit.component.model.style.image.IconStyle;
import io.jmix.mapsflowui.kit.component.model.style.stroke.Stroke;
import org.locationtech.jts.geom.Coordinate;

@FragmentDescriptor("map-fragment.xml")
public class MapFragment extends Fragment<VerticalLayout> {

    private static final double DEFAULT_CENTER_LAT = 55.7522;
    private static final double DEFAULT_CENTER_LON = 37.6156;

    @ViewComponent
    private GeoMap map;

    @Subscribe
    public void onReady(final ReadyEvent event) {
        map.setCenter(new Coordinate(DEFAULT_CENTER_LON, DEFAULT_CENTER_LAT));
        map.setZoom(7.0);
    }

    public GeoMap getMap() {
        return map;
    }

    public <T> VectorLayer addVectorLayerWithDataVectorSource(InstanceContainer<T> dc, String property) {
        DataVectorSource<T> dataVectorSource = createDataVectorSource(dc, property);
        VectorLayer vectorLayer = new VectorLayer()
                .withSource(dataVectorSource);
        map.addLayer(vectorLayer);
        return vectorLayer;
    }

    public <T> VectorLayer addVectorLayerWithClusterDataVectorSource(InstanceContainer<T> dc, String property) {
        DataVectorSource<T> dataVectorSource = createDataVectorSource(dc, property);
        VectorLayer vectorLayer = new VectorLayer()
                .withSource(new ClusterSource()
                        .withVectorSource(dataVectorSource));
        map.addLayer(vectorLayer);
        return vectorLayer;
    }

    public void setCenter(Coordinate center) {
        map.setCenter(center);
    }

    public void setZoom(double zoom) {
        map.setZoom(zoom);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void setControlPointStyleProvider(VectorLayer vectorLayer) {
        DataVectorSource dataVectorSource = vectorLayer.getSource();
        dataVectorSource.setStyleProvider(location ->
            new Style()
                    .withImage(new IconStyle()
                            .withSrc("icons/nav.png")
                            .withScale(0.07d)
                            .withAnchor(new Anchor(0.49, 0.12))
                            .withAnchorOrigin(IconOrigin.BOTTOM_LEFT)
                            .withColor("#12E327")));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void setVehiclesStyleProvider(VectorLayer vectorLayer) {
        DataVectorSource dataVectorSource = vectorLayer.getSource();
        dataVectorSource.setStyleProvider(location ->
                new Style()
                        .withImage(new IconStyle()
                                .withSrc("icons/veh.png")
                                .withScale(0.07d)
                                .withAnchor(new Anchor(0.49, 0.12))
                                .withAnchorOrigin(IconOrigin.BOTTOM_LEFT)));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void setFuelStationStyleProvider(VectorLayer vectorLayer) {
        DataVectorSource dataVectorSource = vectorLayer.getSource();
        dataVectorSource.setStyleProvider(location ->
                new Style()
                        .withImage(new IconStyle()
                                .withSrc("icons/gas.png")
                                .withScale(0.07d)
                                .withAnchor(new Anchor(0.49, 0.12))
                                .withAnchorOrigin(IconOrigin.BOTTOM_LEFT)));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void setLineStringStyleProvider(VectorLayer vectorLayer) {
        DataVectorSource dataVectorSource = vectorLayer.getSource();
        dataVectorSource.setStyleProvider(location ->
                new Style()
                        .withStroke(new Stroke()
                                .withColor("#000000")
                                .withWidth(2.0d)));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void setFactLineStringStyleProvider(VectorLayer vectorLayer) {
        DataVectorSource dataVectorSource = vectorLayer.getSource();
        dataVectorSource.setStyleProvider(location ->
                new Style()
                        .withStroke(new Stroke()
                                .withColor("#EB2DCB")
                                .withWidth(2.0d)));
    }

    private <T> DataVectorSource<T> createDataVectorSource(InstanceContainer<T> dc, String property) {
        return new DataVectorSource<T>()
                .withItems(new ContainerDataVectorSourceItems<>(dc, property));
    }
}