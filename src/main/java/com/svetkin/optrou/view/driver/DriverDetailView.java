package com.svetkin.optrou.view.driver;

import com.svetkin.optrou.entity.Driver;
import com.svetkin.optrou.entity.DriverLicenceCategoryRelation;
import com.svetkin.optrou.repository.DriverLicenceCategoryRelationRepository;
import com.svetkin.optrou.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.Notifications;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.model.CollectionPropertyContainer;
import io.jmix.flowui.model.DataContext;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.Subscribe;
import io.jmix.flowui.view.ViewComponent;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

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
    private DataContext dataContext;

    @Subscribe("licenseCategoriesDataGrid.create")
    public void onLicenseCategoriesDataGridCreate(final ActionPerformedEvent event) {
        DriverLicenceCategoryRelation licenceCategory = driverLicenceCategoryRelationRepository.create();
        licenceCategory = dataContext.merge(licenceCategory);
        licenceCategory.setDriver(getEditedEntity());
        licenseCategoriesDc.getMutableItems().add(licenceCategory);
    }

    @Subscribe("saveAction")
    public void onSaveAction(final ActionPerformedEvent event) {
        List<DriverLicenceCategoryRelation> licenceCategories = getEditedEntity().getLicenseCategories();

        if (CollectionUtils.isEmpty(licenceCategories)) {
            notifications.create("Заполните хотя бы одну водительскую категорию")
                    .build()
                    .open();

            return;
        }

        boolean hasEmptyLicenseCategory = licenceCategories.stream()
                .anyMatch(licenseCategory -> licenseCategory.getLicenseCategory() == null);
        if (hasEmptyLicenseCategory) {
            notifications.create("Заполните водительскую категорию")
                    .build()
                    .open();

            return;
        }

        closeWithSave();
    }

}