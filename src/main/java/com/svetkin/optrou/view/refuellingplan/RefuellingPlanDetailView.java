package com.svetkin.optrou.view.refuellingplan;

import com.svetkin.optrou.entity.RefuellingPlan;
import com.svetkin.optrou.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "refuellingPlans/:id", layout = MainView.class)
@ViewController(id = "optrou_RefuellingPlan.detail")
@ViewDescriptor(path = "refuelling-plan-detail-view.xml")
@EditedEntityContainer("refuellingPlanDc")
public class RefuellingPlanDetailView extends StandardDetailView<RefuellingPlan> {
}