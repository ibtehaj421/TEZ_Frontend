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

public class PassengerRoutePickerController implements Initializable {

    @FXML
    private Button confirmBtn;

    @FXML
    private Button homeBtn;

    @FXML
    private ComboBox<RouteDTO> routeDropDown;

    @FXML
        private ObservableList<RouteDTO> routeList = FXCollections.observableArrayList();
 

    @FXML
    void confirmBtnClicked(ActionEvent event) {
        GlobalContext.getInstance().route = routeDropDown.getValue();
        try {
            Parent mainRoot = FXMLLoader.load(getClass().getResource("PassengerTimePicker.fxml"));

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
        //routeDropDown.setItems(FXCollections.observableArrayList("ROUTE 1", "ROUTE 2", "ROUTE 3"));
       GlobalContext.getInstance().getRoutesUnderOrg();
        for(int i=0;i<GlobalContext.getInstance().routes.size();i++){
            routeList.add(GlobalContext.getInstance().routes.get(i));
        }
        routeDropDown.setItems(routeList);
        routeDropDown.setCellFactory(new Callback<ListView<RouteDTO>, ListCell<RouteDTO>>() {
            @Override
            public ListCell<RouteDTO> call(ListView<RouteDTO> param) {
                return new ListCell<RouteDTO>() {
                    @Override
                    protected void updateItem(RouteDTO item, boolean empty) {
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
        routeDropDown.setButtonCell(new ListCell<RouteDTO>() {
            @Override
            protected void updateItem(RouteDTO item, boolean empty) {
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
