package com.svetkin.optrou.view.driver;

import com.svetkin.optrou.entity.Driver;
import com.svetkin.optrou.entity.DriverLicenceCategoryRelation;
import com.svetkin.optrou.repository.DriverLicenceCategoryRelationRepository;
import com.svetkin.optrou.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.action.view.DetailSaveCloseAction;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.CollectionPropertyContainer;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Route(value = "drivers/:id", layout = MainView.class)
@ViewController(id = "optrou_Driver.detail")
@ViewDescriptor(path = "driver-detail-view.xml")
@EditedEntityContainer("driverDc")
public class DriverDetailView extends StandardDetailView<Driver> {

    @Autowired
    private Notifications notifications;
    @Autowired
    private DriverLicenceCategoryRelationRepository driverLicenceCategoryRelationRepository;

    @ViewComponent
    private CollectionPropertyContainer<DriverLicenceCategoryRelation> licenseCategoriesDc;
    @ViewComponent
    private DetailSaveCloseAction<Object> saveAction;

    @Subscribe("licenseCategoriesDataGrid.create")
    public void onLicenseCategoriesDataGridCreate(final ActionPerformedEvent event) {
        DriverLicenceCategoryRelation relation = driverLicenceCategoryRelationRepository.create();
        relation.setDriver(getEditedEntity());
        licenseCategoriesDc.getMutableItems().add(relation);
    }

    @Subscribe("saveAction")
    public void onSaveAction(final ActionPerformedEvent event) {
        boolean hasEmptyLicenseCategory = getEditedEntity().getLicenseCategories().stream()
                .anyMatch(licenseCategory -> licenseCategory.getLicenseCategory() == null);
        if (hasEmptyLicenseCategory) {
            notifications.create("Заполните водительскую категорию")
                    .build()
                    .open();
            return;
        }

        saveAction.execute();
    }

}