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
import io.jmix.mapsflowui.kit.component.model.layer.Layer;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

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

    private <T> DataVectorSource<T> createDataVectorSource(InstanceContainer<T> dc, String property) {
        return new DataVectorSource<T>()
                .withItems(new ContainerDataVectorSourceItems<>(dc, property));
    }
}