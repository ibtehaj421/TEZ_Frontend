import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class DriverHomePageController {

    @FXML
    private Button allocationBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button reportBtn;

    @FXML
    private Button seatBtn;

    @FXML
    public void initialize(){
        getBus();
        getRoute();
    }

    @FXML
    void allocationBtnClicked(ActionEvent event) {
        try {
            Parent mainRoot = FXMLLoader.load(getClass().getResource("./DriverAllocate.fxml"));

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

    @FXML
    void reportBtnClicked(ActionEvent event) {
        try {
            Parent mainRoot = FXMLLoader.load(getClass().getResource("DriverReport.fxml"));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene mainScene = new Scene(mainRoot);

            stage.setScene(mainScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void seatBtnClicked(ActionEvent event) {
        try {
            Parent mainRoot = FXMLLoader.load(getClass().getResource("DriverSeatMark.fxml"));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene mainScene = new Scene(mainRoot);

            stage.setScene(mainScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void getRoute(){
        String url = "http://localhost:9090/driver/bus/"+GlobalContext.getInstance().user.id;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            GlobalContext.getInstance().request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization","Bearer "+GlobalContext.getInstance().user.jwt)
                .header("Content-Type", "application/json")
                .GET()
                .build();

                GlobalContext.getInstance().response = GlobalContext.getInstance().client.send(GlobalContext.getInstance().request, HttpResponse.BodyHandlers.ofString());
                if (GlobalContext.getInstance().response.statusCode() == 200) {  // Check if the response is successful
            // Deserialize JSON response to a list of UserDTO objects
            GlobalContext.getInstance().bus = objectMapper.readValue(GlobalContext.getInstance().response.body(), getBusDTO.class);
                    }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    void getBus(){
        String url = "http://localhost:9090/driver/route/"+GlobalContext.getInstance().user.id;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            GlobalContext.getInstance().request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization","Bearer "+GlobalContext.getInstance().user.jwt)
                .header("Content-Type", "application/json")
                .GET()
                .build();
                GlobalContext.getInstance().response = GlobalContext.getInstance().client.send(GlobalContext.getInstance().request, HttpResponse.BodyHandlers.ofString());

                if (GlobalContext.getInstance().response.statusCode() == 200) {  // Check if the response is successful
                    // Deserialize JSON response to a list of UserDTO objects
                    GlobalContext.getInstance().route = objectMapper.readValue(GlobalContext.getInstance().response.body(), RouteDTO.class);
                    }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
