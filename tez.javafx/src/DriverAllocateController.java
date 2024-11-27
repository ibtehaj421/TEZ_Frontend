import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class DriverAllocateController {

    @FXML
    private Button homeBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private Label numPlateLabel;

    @FXML
    private Label regYearLabel;

    @FXML
    private Label routeLabel;

    // Dummy Bus and RouteDTO objects
    private BusDTO dummyBus;
    private RouteDTO dummyRoute;

    @FXML
    public void initialize() {
        // Create a dummy Bus object
        

        // Set the labels with values from the dummy objects
        // numPlateLabel.setText(dummyBus.getNumberPlate());
        // regYearLabel.setText(dummyBus.getModel());
        // routeLabel.setText(
        //         dummyRoute.getOrigin() + " - " + dummyRoute.getDestination());
        routeLabel.setText(GlobalContext.getInstance().route.origin + " - " + GlobalContext.getInstance().route.destination);
        numPlateLabel.setText(GlobalContext.getInstance().bus.bus.licNum);
    }

    @FXML
    void homeBtnClicked(ActionEvent event) {
        try {
            Parent mainRoot = FXMLLoader.load(getClass().getResource("DriverHomePage.fxml"));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene mainScene = new Scene(mainRoot);

            stage.setScene(mainScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void logoutBtnClicked(ActionEvent event) {
        try {
            Parent mainRoot = FXMLLoader.load(getClass().getResource("MainScene.fxml"));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene mainScene = new Scene(mainRoot);

            stage.setScene(mainScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
