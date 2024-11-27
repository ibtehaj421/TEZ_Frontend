import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OrgLoginController {

    @FXML
    private Button backBtn;

    @FXML
    private Button loginBtn;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    @FXML
    void backBtnClicked(ActionEvent event) {
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
    void loginBtnClicked(ActionEvent event) {
        
        try {
            GlobalContext.getInstance().org = new Organization();
            GlobalContext.getInstance().org.name = usernameField.getText();
            //String org = "Daewoo";
            Map<String,String> map = new HashMap<String,String>();
            map.put("name", GlobalContext.getInstance().org.name);
            ObjectMapper objectMapper = new ObjectMapper();
            String json = "";
            try {
                json = objectMapper.writeValueAsString(map);  // Convert Map to JSON string
            } catch (Exception e) {
                e.printStackTrace();
            }
                try {
                    
                    GlobalContext.getInstance().request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:9090/org/login"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                        .build();
                    GlobalContext.getInstance().org.name = " ";
                    GlobalContext.getInstance().response = GlobalContext.getInstance().client.send(GlobalContext.getInstance().request, HttpResponse.BodyHandlers.ofString());
                    GlobalContext.getInstance().org = objectMapper.readValue(GlobalContext.getInstance().response.body(), Organization.class);
                    System.out.println(GlobalContext.getInstance().org.name);
                    if(GlobalContext.getInstance().org.name.equals(" ")){
                        usernameField.clear();
                        return;
                    }
                    Parent mainRoot = FXMLLoader.load(getClass().getResource("OrgAdminManagement.fxml"));

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                    Scene mainScene = new Scene(mainRoot);

                    stage.setScene(mainScene);
                    stage.show();
                } catch (Exception e) {
                    
                }
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
