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

public class MainSceneController {

    @FXML
    private Button LoginBtn;

    @FXML
    private Button SignupBtn;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button orgBtn;

    @FXML
    void callSignUpPage(ActionEvent event) {
        try {
            // Load the SignUpScene.fxml
            Parent signUpRoot = FXMLLoader.load(getClass().getResource("SignUpScene.fxml"));

            // Get the current stage from the event
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Create a new scene with the loaded FXML root
            Scene signUpScene = new Scene(signUpRoot);

            // Set the new scene on the stage
            stage.setScene(signUpScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void orgBtnClicked(ActionEvent event) {
        try {
            Parent mainRoot = FXMLLoader.load(getClass().getResource("OrgLogin.fxml"));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene mainScene = new Scene(mainRoot);

            stage.setScene(mainScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void verifyLogin(ActionEvent event) {

        String em = emailField.getText();
        String p = passwordField.getText();
        //request for login
        //GlobalContext.getInstance().user = new UserDTO();
        try {
            GlobalContext.getInstance();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        Map<String,String> map = new HashMap<String,String>();
            map.put("email", em);
            map.put("password", p);
            ObjectMapper objectMapper = new ObjectMapper();
            String json = "";
            try {
                json = objectMapper.writeValueAsString(map);  // Convert Map to JSON string
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
                try {
                    
                    GlobalContext.getInstance().request = HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:9090/login"))
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                        .build();
                    //GlobalContext.getInstance().user.name = " ";
                    GlobalContext.getInstance().response = GlobalContext.getInstance().client.send(GlobalContext.getInstance().request, HttpResponse.BodyHandlers.ofString());
                    GlobalContext.getInstance().user = objectMapper.readValue(GlobalContext.getInstance().response.body(), UserDTO.class);
                    System.out.println(GlobalContext.getInstance().org.name);
                    if(GlobalContext.getInstance().user == null){
                        //invalid user
                        return;
                    }
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
        System.out.println(GlobalContext.getInstance().user);
        if (GlobalContext.getInstance().user.role.equals("CORP_ADMIN") || GlobalContext.getInstance().user.role.equals("EDU_ADMIN")) // admin logged int
        {
            try {
                Parent signUpRoot = FXMLLoader.load(getClass().getResource("AdminHomePage.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene signUpScene = new Scene(signUpRoot);
                stage.setScene(signUpScene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (GlobalContext.getInstance().user.role.equals("DRIVER")) // admin logged int
        {
            try {
                Parent signUpRoot = FXMLLoader.load(getClass().getResource("DriverHomePage.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene signUpScene = new Scene(signUpRoot);
                stage.setScene(signUpScene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(GlobalContext.getInstance().user.role.equals("BASIC") || GlobalContext.getInstance().user.role.equals("STUDENT")){
            try {
                Parent signUpRoot = FXMLLoader.load(getClass().getResource("PassengerHomePage.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene signUpScene = new Scene(signUpRoot);
                stage.setScene(signUpScene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
