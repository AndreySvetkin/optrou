package com.svetkin.optrou.view.refuellingplan;

import com.svetkin.optrou.entity.RefuellingPlan;
import com.svetkin.optrou.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.DialogMode;
import io.jmix.flowui.view.LookupComponent;
import io.jmix.flowui.view.StandardListView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;


@Route(value = "refuellingPlans", layout = MainView.class)
@ViewController(id = "optrou_RefuellingPlan.list")
@ViewDescriptor(path = "refuelling-plan-list-view.xml")
@LookupComponent("refuellingPlansDataGrid")
@DialogMode(width = "64em")
public class RefuellingPlanListView extends StandardListView<RefuellingPlan> {
}