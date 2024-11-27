import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class PassengerUpdatesController {

    @FXML
    private TableView<Notifications> updatesTable;

    @FXML
    private Label displayUpdates;

    @FXML
    private Button homeBtn;

    @FXML
    private Button removeBtn;

    @FXML
    private TableColumn<Notifications, String> updatesColumn; // Correctly refers to the "response" field in Feedback
                                                              // class

    private ObservableList<Notifications> responses = FXCollections.observableArrayList();
    private Notifications selectedNotif;

    @SuppressWarnings("unused")
    @FXML
    public void initialize() {
        // Bind TableColumn to the 'response' field in Feedback class
        updatesColumn.setCellValueFactory(new PropertyValueFactory<>("response"));

        // Adding some dummy data for testing
        responses.add(new Notifications(1,
                "IJP ROAD BLOCKED"));
        responses.add(new Notifications(1,
                "Bus (LEH-1844) travelling from Phase 4 to FAST NUCES broke down at PSO Pump and there will be a 40 minute delay"));

        updatesTable.setItems(responses);

        updatesTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedNotif = newValue;

                displayUpdates.setText(selectedNotif.getResponse());
            }
        });
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
}
