import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DriverReportController {

    @FXML
    private Button homeBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private TextField reportField;

    @FXML
    private Button sendBtn;

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
    void sendBtnClicked(ActionEvent event) {
        addFeedBack();
        reportField.clear();
    }

    public void addFeedBack(){
        String url = "http://localhost:9090/submit/feedback";
        Map<String,String> map = new HashMap<String,String>();
            map.put("userId", String.valueOf(GlobalContext.getInstance().user.id));
            map.put("orgid", String.valueOf(GlobalContext.getInstance().user.orgid));
            map.put("content", "(DRIVER) "+reportField.getText());
            ObjectMapper objectMapper = new ObjectMapper();
            String json = "";
            try {
                json = objectMapper.writeValueAsString(map);  // Convert Map to JSON string
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                    GlobalContext.getInstance().request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Authorization","Bearer " + GlobalContext.getInstance().user.jwt)
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                        .build();
                
                    GlobalContext.getInstance().response = GlobalContext.getInstance().client.send(GlobalContext.getInstance().request, HttpResponse.BodyHandlers.ofString());
                    System.out.println(GlobalContext.getInstance().response.body());
                } catch (Exception e) {
                    e.printStackTrace();
                }


    }

}
