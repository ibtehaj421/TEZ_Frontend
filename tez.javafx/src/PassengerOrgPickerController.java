import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;

public class PassengerOrgPickerController implements Initializable {

    @FXML
    private Button confirmBtn;

    @FXML
    private Button homeBtn;

    @FXML
    private ComboBox<Organization> orgDropDown ;

    @FXML
    private ObservableList<Organization> orgList = FXCollections.observableArrayList();

    @FXML
    void confirmBtnClicked(ActionEvent event) {
        
        try {
            GlobalContext.getInstance().org = orgDropDown.getValue();
            System.out.println(GlobalContext.getInstance().org.name);
            Parent mainRoot = FXMLLoader.load(getClass().getResource("PassengerRoutePicker.fxml"));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene mainScene = new Scene(mainRoot);

            stage.setScene(mainScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void homeBtnClicked(ActionEvent event) {
        try {
            Parent mainRoot = FXMLLoader.load(getClass().getResource("PassengerHomePage.fxml"));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene mainScene = new Scene(mainRoot);

            stage.setScene(mainScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //org list is filled with organizations list from global context.
        GlobalContext.getInstance().getOrgList();
        for(int i=0;i<GlobalContext.getInstance().organizations.size();i++){
            if(GlobalContext.getInstance().user.role.equals("STUDENT")){
                if(GlobalContext.getInstance().organizations.get(i).type.equals("EDU_ADMIN")){
                    orgList.add(GlobalContext.getInstance().organizations.get(i));
                }
            }
            else if(GlobalContext.getInstance().user.role.equals("BASIC")){
                if(GlobalContext.getInstance().organizations.get(i).type.equals("CORP_ADMIN")){
                    orgList.add(GlobalContext.getInstance().organizations.get(i));
                }
            }
            System.out.println(GlobalContext.getInstance().organizations.get(i).name);
        }
        orgDropDown.setItems(orgList);

        orgDropDown.setCellFactory(new Callback<ListView<Organization>, ListCell<Organization>>() {
            @Override
            public ListCell<Organization> call(ListView<Organization> param) {
                return new ListCell<Organization>() {
                    @Override
                    protected void updateItem(Organization item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.getName()); // Displaying only the driver's name
                        }
                    }
                };
            }
        });
        orgDropDown.setButtonCell(new ListCell<Organization>() {
            @Override
            protected void updateItem(Organization item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
    }

}
