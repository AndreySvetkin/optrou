package com.svetkin.optrou.view.refuellingplan;

import com.svetkin.optrou.entity.Refuelling;
import com.svetkin.optrou.entity.RefuellingPlan;
import com.svetkin.optrou.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.core.LoadContext;
import io.jmix.flowui.view.DialogMode;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.Install;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.Target;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

import java.util.List;

@ViewController(id = "optrou_RefuellingPlan.detail")
@ViewDescriptor(path = "refuelling-plan-detail-view.xml")
@EditedEntityContainer("refuellingPlanDc")
@DialogMode(width = "50em")
public class RefuellingPlanDetailView extends StandardDetailView<RefuellingPlan> {

    @Install(to = "refuellingsDl", target = Target.DATA_LOADER)
    private List<Refuelling> refuellingsDlLoadDelegate(final LoadContext loadContext) {
        return getEditedEntity().getRefuellings();
    }
}