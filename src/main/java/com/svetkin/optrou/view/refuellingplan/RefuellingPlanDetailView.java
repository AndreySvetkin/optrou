package com.svetkin.optrou.view.refuellingplan;

import com.svetkin.optrou.entity.RefuellingPlan;
import io.jmix.flowui.view.DialogMode;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@ViewController(id = "optrou_RefuellingPlan.detail")
@ViewDescriptor(path = "refuelling-plan-detail-view.xml")
@EditedEntityContainer("refuellingPlanDc")
@DialogMode(width = "50em")
public class RefuellingPlanDetailView extends StandardDetailView<RefuellingPlan> {
}