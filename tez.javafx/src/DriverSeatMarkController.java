import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class DriverSeatMarkController {

    @FXML
    private Button homeBtn;

    @FXML
    private Button logoutBtn;

    private List<Button> seats = new ArrayList<>();

    @FXML
    private Button seatBtn1;

    @FXML
    private Button seatBtn10;

    @FXML
    private Button seatBtn11;

    @FXML
    private Button seatBtn12;

    @FXML
    private Button seatBtn13;

    @FXML
    private Button seatBtn14;

    @FXML
    private Button seatBtn15;

    @FXML
    private Button seatBtn16;

    @FXML
    private Button seatBtn17;

    @FXML
    private Button seatBtn18;

    @FXML
    private Button seatBtn19;

    @FXML
    private Button seatBtn2;

    @FXML
    private Button seatBtn20;

    @FXML
    private Button seatBtn3;

    @FXML
    private Button seatBtn4;

    @FXML
    private Button seatBtn5;

    @FXML
    private Button seatBtn6;

    @FXML
    private Button seatBtn7;

    @FXML
    private Button seatBtn8;

    @FXML
    private Button seatBtn9;

    @FXML
    private Button updateBtn;

    @FXML
    public void initialize() {
        // Add all seat buttons to the list
        seats = Arrays.asList(seatBtn1, seatBtn2, seatBtn3, seatBtn4, seatBtn5, seatBtn6,
                seatBtn7, seatBtn8, seatBtn9, seatBtn10, seatBtn11, seatBtn12,
                seatBtn13, seatBtn14, seatBtn15, seatBtn16, seatBtn17, seatBtn18,
                seatBtn19, seatBtn20);

        // Loop through each button and set the action handler
        for (Button seat : seats) {
            seat.setOnAction(this::seatBtnClicked);
        }
        for(Button seat : seats){
            System.out.println(seat.getId().substring( 7)); //specifies the seat? use the fetched bus to get which bus the seat belongs to.
        }
    }

    @FXML
    void seatBtnClicked(ActionEvent event) {
        // Identify which button was clicked
        Button clickedButton = (Button) event.getSource();

        // Toggle the button color (class)
        if (clickedButton.getStyleClass().contains("toggled-button")) {
            clickedButton.getStyleClass().remove("toggled-button");
            clickedButton.getStyleClass().add("default-button");
        } else {
            clickedButton.getStyleClass().remove("default-button");
            clickedButton.getStyleClass().add("toggled-button");
        }
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

    @FXML
    void updateBtnClicked(ActionEvent event) {
            //push an update for all seats that are present.
            String id;
            long value;
            String url;
            //ObjectMapper objectMapper = new ObjectMapper();
            for(Button seat : seats){
                Button temp = seat;
                id = temp.getId().substring(7);
                value = Long.parseLong(id);
                if(temp.getStyleClass().contains("toggled-button")){
                     url = "http://localhost:9090/driver/seat/status/" +GlobalContext.getInstance().bus.bus.id + "/occupied/" + value;
                }
                else {
                    //the seat is still active
                    url = "http://localhost:9090/driver/seat/status/" + GlobalContext.getInstance().bus.bus.id + "/available/" + value;
                }

                try {
                    GlobalContext.getInstance().request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .method("PATCH", HttpRequest.BodyPublishers.noBody())
                    .header("Authorization", "Bearer " + GlobalContext.getInstance().user.jwt)
                    .header("Content-Type", "application/json")
                    .build();

                    GlobalContext.getInstance().response = GlobalContext.getInstance().client.send(GlobalContext.getInstance().request, HttpResponse.BodyHandlers.ofString());
                    System.out.println("Response Code: " + GlobalContext.getInstance().response.statusCode());
                    System.out.println("Response Body: " + GlobalContext.getInstance().response.body());

                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
    }

}
