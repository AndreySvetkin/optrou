package com.svetkin.optrou.view.driver;

import com.svetkin.optrou.entity.Driver;
import com.svetkin.optrou.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.DialogMode;
import io.jmix.flowui.view.LookupComponent;
import io.jmix.flowui.view.StandardListView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


@Route(value = "drivers", layout = MainView.class)
@ViewController(id = "optrou_Driver.list")
@ViewDescriptor(path = "driver-list-view.xml")
@LookupComponent("driversDataGrid")
@DialogMode(width = "64em")
public class DriverListView extends StandardListView<Driver> {
}