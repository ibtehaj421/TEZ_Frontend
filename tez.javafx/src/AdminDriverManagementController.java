import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AdminDriverManagementController {

    @FXML
    private Button busBtn;

    @FXML
    private TableColumn<UserDTO, String> emailColumn;

    @FXML
    private TextField emailField;

    @FXML
    private Button homeBtn;

    @FXML
    private TableColumn<UserDTO, Integer> levelColumn;

    @FXML
    private TextField levelField;

    @FXML
    private TableColumn<UserDTO, String> nameColumn;

    @FXML
    private Button newBtn;

    @FXML
    private TableColumn<UserDTO, String> passwordColumn;

    @FXML
    private TextField passwordField;

    @FXML
    private Button removeBtn;

    @FXML
    private Button routeBtn;

    @FXML
    private TableView<UserDTO> routeTable;

    @FXML
    private Button saveBtn;

    @FXML
    private TextField startField;

    @FXML
    private Button updateBtn;

    private ObservableList<UserDTO> driverData = FXCollections.observableArrayList();
    private UserDTO selectedDriver;

    @SuppressWarnings("unused")
    @FXML
    public void initialize() {
        // Set up the table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        levelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));

        // Create dummy driver data
        

        // Add the drivers to the observable list
        
        getDrivers();
        // Populate the TableView with data
        routeTable.setItems(driverData);

        routeTable.getSelectionModel().selectedItemProperty().addListener((observable,
                oldValue, newValue) -> {
            if (newValue != null) {
                selectedDriver = newValue;

                // Populate the input fields with the selected driver's details
                emailField.setText(selectedDriver.email);
                levelField.setText(String.valueOf(selectedDriver.level));
                startField.setText(selectedDriver.name);
                passwordField.setText(selectedDriver.password);
            }
        });
        
    }

    @FXML
    void busBtnClicked(ActionEvent event) {
        // Implement switching to Bus Management scene
        try {
            Parent mainRoot = FXMLLoader.load(getClass().getResource("AdminBusManagement.fxml"));

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
            Parent mainRoot = FXMLLoader.load(getClass().getResource("AdminHomePage.fxml"));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene mainScene = new Scene(mainRoot);

            stage.setScene(mainScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void newBtnClicked(ActionEvent event) {
        // Clear input fields for new driver entry
        emailField.clear();
        levelField.clear();
        startField.clear();
        passwordField.clear();
        selectedDriver = null; // Deselect any selected driver
    }

    @FXML
    void removeBtnClicked(ActionEvent event) {
        if (selectedDriver != null) {
            driverData.remove(selectedDriver); // Remove the selected driver from the list
            selectedDriver = null; // Clear the selection
            emailField.clear(); // Clear input fields
            levelField.clear();
            startField.clear();
            passwordField.clear();
        }
    }

    @FXML
    void routeBtnClicked(ActionEvent event) {
        // Implement switching to Route Management scene
        try {
            Parent mainRoot = FXMLLoader.load(getClass().getResource("AdminRouteManagement.fxml"));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene mainScene = new Scene(mainRoot);

            stage.setScene(mainScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    void saveBtnClicked(ActionEvent event) {
        // Get the input data
        String email = emailField.getText();
        String levelText = levelField.getText();
        String name = startField.getText();
        String password = passwordField.getText();

        if (!email.isEmpty() && !levelText.isEmpty() && !name.isEmpty() &&
                !password.isEmpty()) {

            GlobalContext.getInstance().driver = new UserDTO();
            GlobalContext.getInstance().driver.email = email;
            GlobalContext.getInstance().driver.level = Integer.parseInt(levelText);
            GlobalContext.getInstance().driver.name = name;
            GlobalContext.getInstance().driver.password = password;
            //process http request
        Map<String,String> data = new HashMap<String,String>();
        data.put("name", GlobalContext.getInstance().driver.name);
        data.put("email", GlobalContext.getInstance().driver.email);
        data.put("password", GlobalContext.getInstance().driver.password);
        data.put("level", String.valueOf(GlobalContext.getInstance().driver.level));
            System.out.println(GlobalContext.getInstance().driver.email);
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(data);  // Convert Map to JSON string
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{
    
        GlobalContext.getInstance().request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:9090/auth/register/driver/"+GlobalContext.getInstance().user.orgid))
            .header("Authorization","Bearer "+GlobalContext.getInstance().user.jwt)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(json,StandardCharsets.UTF_8))
            .build();


            GlobalContext.getInstance().response = GlobalContext.getInstance().client.send(GlobalContext.getInstance().request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Response Code: " + GlobalContext.getInstance().response.statusCode());
            System.out.println("Response Body: " + GlobalContext.getInstance().response.body());
            if(GlobalContext.getInstance().response.body().contains("registered")){
                GlobalContext.getInstance().driver.id = GlobalContext.getInstance().getUserID(GlobalContext.getInstance().driver.email);
                GlobalContext.getInstance().drivers.add(GlobalContext.getInstance().driver);
                driverData.add(GlobalContext.getInstance().driver);
            }
    } catch (Exception e) {
        e.printStackTrace();
        }
        }
        emailField.clear();
        levelField.clear();
        startField.clear();
        passwordField.clear();
    }

    @FXML
    void updateBtnClicked(ActionEvent event) {
        if (selectedDriver != null) {
            // Update the selected driver's details
            System.out.println(selectedDriver);
            selectedDriver.email = emailField.getText();
            selectedDriver.level = Integer.parseInt(levelField.getText());
            selectedDriver.name = startField.getText();
            selectedDriver.password = passwordField.getText();
            
            routeTable.refresh(); // Refresh the table to reflect updates
        }
    }

    void getDrivers(){
        String url = "http://localhost:9090/get/drivers/" + GlobalContext.getInstance().user.orgid;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
        // Create HTTP request
        GlobalContext.getInstance().request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + GlobalContext.getInstance().user.jwt)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        // Send synchronous request and get the response
        GlobalContext.getInstance().response = GlobalContext.getInstance().client.send(GlobalContext.getInstance().request, HttpResponse.BodyHandlers.ofString());

        if (GlobalContext.getInstance().response.statusCode() == 200) {  // Check if the response is successful
            // Deserialize JSON response to a list of UserDTO objects
            List<UserDTO> admins = objectMapper.readValue(GlobalContext.getInstance().response.body(), new TypeReference<List<UserDTO>>(){});

            // Process the list (update UI, etc.)
            Platform.runLater(() -> {
                // Add the fetched admins to the observable list or update UI components
                for (UserDTO admin : admins) {
                    driverData.add(admin);
                    // Your logic to handle the list, e.g., adminListView.getItems().add(admin);
                }
            });

        } else {
            System.out.println("Request failed with status code: " + GlobalContext.getInstance().response.statusCode());
        }

    } catch (Exception e) {
        e.printStackTrace();
        Platform.runLater(() -> {
            System.out.println("Request Failed: " + e.getMessage());
        });
    }
    }
}
