package infoshare.client.content.content.views;

import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.*;
import infoshare.app.facade.ContentFacade;
import infoshare.client.content.MainLayout;
import infoshare.client.content.content.ContentMenu;
import infoshare.client.content.content.forms.PublishForm;
import infoshare.client.content.content.models.ContentModel;
import infoshare.client.content.content.tables.PublishTable;
import infoshare.domain.content.EditedContent;
import infoshare.domain.content.PublishedContent;
import infoshare.filterSearch.PublishedContentFilter;
import infoshare.services.Content.EditedContentService;
import infoshare.services.Content.PublishedContentService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by hashcode on 2015/06/24.
 */
public class PublishView extends VerticalLayout implements Button.ClickListener,
        Property.ValueChangeListener {
    @Autowired
    private PublishedContentService publishedContentService = ContentFacade.publishedContentService;
    private EditedContentService editedContentService = ContentFacade.editedContentService;

    private final MainLayout main;
    private final PublishTable table;
    private final PublishForm form;
    private final Window popUp ;
    private PublishedContentFilter publishedContentFilter = new PublishedContentFilter();
    public PublishView( MainLayout mainApp) {

        this.main = mainApp;
        this.table = new PublishTable(main);
        this.form = new PublishForm();
        this.popUp = modelWindow();
        setSpacing(true);
        setSizeFull();
        addComponent(publishedContentFilter.field);
        addComponent(table);
        addListeners();
    }
    private void refreshContacts(String stringFilter ) {
        try {
            table.removeAllItems();
            publishedContentFilter.findAll(stringFilter)
                    .stream()
                    .filter(cont ->cont.getStatus().equalsIgnoreCase("Published"))
                    .collect(Collectors.toList())
                    .stream()
                    .filter(cont ->cont.getState().equalsIgnoreCase("active"))
                    .collect(Collectors.toList())
                    .forEach(table::loadTable);
        }catch (Exception e){
        }
    }
    @Override
    public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
        final Property property = valueChangeEvent.getProperty();

    }
    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        final Button source = clickEvent.getButton();
        if (source ==form.popUpCloseBtn){
            popUp.setModal(false);
            UI.getCurrent().removeWindow(popUp);
            table.setValue(null);
            getHome();
        }else if (source==form.putToEdit){
            saveEditedForm(form.binder);
        }
    }
    private Window modelWindow(){
        final Window popup = new Window();
        popup.setWidth(80.0f,Unit.PERCENTAGE);
        popup.setClosable(false);
        popup.setResizable(false);
        popup.setContent(form);
        return popup;
    }
    private void saveEditedForm(FieldGroup binder) {
        try {
            binder.commit();
            try {
                editedContentService.save(getNewEntity(publishedContentService.update(getUpdateEntity(table.getValue().toString()))));
                popUp.setModal(false);
                table.setValue(null);
                UI.getCurrent().removeWindow(popUp);
                getHome();
                Notification.show("Record EDITED!", Notification.Type.HUMANIZED_MESSAGE);
            }catch (Exception e){
                Notification.show(e.getMessage()+"Please select the Category",Notification.Type.HUMANIZED_MESSAGE);
            }
        } catch (FieldGroup.CommitException e) {
            Notification.show("Fill in all Fields!!", Notification.Type.HUMANIZED_MESSAGE);
            getHome();
        }
    }
    private PublishedContent getUpdateEntity(String val ) {
        final PublishedContent bean = publishedContentService.findById(val);
        final PublishedContent editedContent = new PublishedContent
                .Builder(bean.getTitle()).copy(bean)
                .status("Edited")
                .build();
        return editedContent;
    }
    private EditedContent getNewEntity(PublishedContent bean) {
        Map<String,String> editedVals = new HashMap<>();
        editedVals.put("content",bean.getContent());
        editedVals.put("category",bean.getCategory());
        editedVals.put("creator",bean.getCreator());
        editedVals.put("contentType",bean.getContentType());
        editedVals.put("status",bean.getStatus());
        editedVals.put("source",bean.getStatus());
        final EditedContent editedContent = EditedContentFacory.getEditedContent(editedVals,new Date());
        return editedContent;
    }
    private void ViewContentButton(){
        try {
            final PublishedContent publishedContent = publishedContentService.findById(table.getValue().toString());
            final ContentModel bean = getModel(publishedContent);
            form.binder.setItemDataSource(new BeanItem<>(bean));
            UI.getCurrent().addWindow(popUp);
            popUp.setModal(true);
        }catch (Exception e){
            Notification.show("Select the content you wanna view",
                    Notification.Type.HUMANIZED_MESSAGE);
        }
    }
    private ContentModel getModel(PublishedContent val) {
        final ContentModel model = new ContentModel();
        final PublishedContent publishedContent = publishedContentService.findById(val.getId());
        model.setTitle(publishedContent.getTitle());
        model.setCategory(publishedContent.getCategory());
        model.setCreator(publishedContent.getCreator());
        model.setContent(publishedContent.getContent());
        model.setContentType(publishedContent.getContentType());
        model.setSource(publishedContent.getSource());
        return model;
    }
    private void getHome() {
        main.content.setSecondComponent(new ContentMenu(main, "PUBLISHER"));
    }
    public void addListeners(){
        form.popUpCloseBtn.addClickListener(this);
        form.putToEdit.addClickListener(this);
        table.addValueChangeListener(this);
        table.addItemClickListener(item ->{
            boolean flag = true;
            if(item.isDoubleClick()){
                if(flag) {
                    ViewContentButton();
                    flag = false;
                }
            }
        });
        publishedContentFilter.field.addTextChangeListener((FieldEvents.TextChangeListener) textChangeEvent -> refreshContacts(textChangeEvent.getText()));
    }

}
