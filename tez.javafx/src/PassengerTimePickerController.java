import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class PassengerTimePickerController implements Initializable {

    @FXML
    private Button confirmBtn;

    @FXML
    private Button homeBtn;

    @FXML
    private ComboBox<String> timeDropDown;

    @FXML
    void confirmBtnClicked(ActionEvent event) {
        timeDropDown.getValue();
        //boolean schoolOrg = true;

        try {
            if (GlobalContext.getInstance().user.role.equals("STUDENT")) {
                Parent mainRoot = FXMLLoader.load(getClass().getResource("StudentBusTracker.fxml"));

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                Scene mainScene = new Scene(mainRoot);

                stage.setScene(mainScene);
                stage.show();
            } else {
                Parent mainRoot = FXMLLoader.load(getClass().getResource("PassengerBusTracker.fxml"));

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                Scene mainScene = new Scene(mainRoot);

                stage.setScene(mainScene);
                stage.show();
            }
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
        timeDropDown.setItems(FXCollections.observableArrayList("TIME 1", "TIME 2", "TIME 3"));
    }

}
