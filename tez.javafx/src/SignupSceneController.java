import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SignupSceneController implements Initializable {

    @FXML
    private ComboBox<String> accountType;

    @FXML
    private TextField emailField;

    @FXML
    private Button loginBtn;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signupBtn;

    @FXML
    private TextField usernameField;

    @FXML
    void loginBtnClicked(ActionEvent event) {
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
    void signupBtnClicked(ActionEvent event) {
            GlobalContext.getInstance().user = new UserDTO();
            GlobalContext.getInstance().user.name = usernameField.getText();
            GlobalContext.getInstance().user.email = emailField.getText();
            GlobalContext.getInstance().user.password = passwordField.getText();
            GlobalContext.getInstance().user.role = accountType.getValue();
        Map<String,String> data = new HashMap<String,String>();
        data.put("name", GlobalContext.getInstance().user.name);
        data.put("email", GlobalContext.getInstance().user.email);
        data.put("password", GlobalContext.getInstance().user.password);
        data.put("role", GlobalContext.getInstance().user.role);

        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(data);  // Convert Map to JSON string
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{
    
    GlobalContext.getInstance().request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:9090/auth/register/user"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json,StandardCharsets.UTF_8))
            .build();


            GlobalContext.getInstance().response = GlobalContext.getInstance().client.send(GlobalContext.getInstance().request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Code: " + GlobalContext.getInstance().response.statusCode());
            System.out.println("Response Body: " + GlobalContext.getInstance().response.body());
    } catch (Exception e) {
        e.printStackTrace();
        }

        
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        accountType.setItems(FXCollections.observableArrayList("Passenger [BASIC]", "Passenger [STUDENT]"));
    }

}
