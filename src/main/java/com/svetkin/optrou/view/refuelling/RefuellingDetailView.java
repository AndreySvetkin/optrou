package com.svetkin.optrou.view.refuelling;

import com.svetkin.optrou.entity.Refuelling;
import com.svetkin.optrou.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "refuellings/:id", layout = MainView.class)
@ViewController(id = "optrou_Refuelling.detail")
@ViewDescriptor(path = "refuelling-detail-view.xml")
@EditedEntityContainer("refuellingDc")
public class RefuellingDetailView extends StandardDetailView<Refuelling> {
}