import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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
public class OrgAdminManagement {

    @FXML
    private TableColumn<UserDTO, String> adminNameColumn;

    @FXML
    private TextField adminNameField;

    @FXML
    private TableView<UserDTO> adminTable;

    @FXML
    private TableColumn<UserDTO, String> emailColumn;

    @FXML
    private TextField emailField;

    @FXML
    private Button homeBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    private Button newBtn;

    @FXML
    private TableColumn<UserDTO, String> passwordColumn;

    @FXML
    private TextField passwordField;

    @FXML
    private Button removeBtn;

    @FXML
    private Button saveBtn;

    @FXML
    private Button updateBtn;

    private ObservableList<UserDTO> adminList = FXCollections.observableArrayList(); // Holds the list of admins
    private UserDTO selectedAdmin; // The currently selected admin

    @SuppressWarnings("unused")
    @FXML
    public void initialize() {
        // Set up the TableView columns
        adminNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        getAdmins();
        // Populate the TableView with data
        adminTable.setItems(adminList);

        // Listen for row selection in the table
        adminTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedAdmin = newSelection;
                populateFieldsWithSelectedAdmin();
            }
        });
        
    }

    // Populate the input fields with the selected admin's data
    private void populateFieldsWithSelectedAdmin() {
        if (selectedAdmin != null) {
            adminNameField.setText(selectedAdmin.getName());
            emailField.setText(selectedAdmin.getEmail());
            passwordField.setText(selectedAdmin.getPassword());
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
    void newBtnClicked(ActionEvent event) {
        adminNameField.clear();
        emailField.clear();
        passwordField.clear();
        selectedAdmin = null;
    }

    @FXML
    void removeBtnClicked(ActionEvent event) {
        UserDTO selected = adminTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            adminList.remove(selected);
            adminTable.refresh();
        }
    }

    @FXML
    void saveBtnClicked(ActionEvent event) {

        System.out.println("INSIDE THE SAVE BUTTON");
        UserDTO newAdmin = new UserDTO();
        addAdmin(newAdmin);
        System.out.println("INSIDE THE SAVE BUTTON");
        newBtnClicked(null);
    }

    @FXML
    void updateBtnClicked(ActionEvent event) {
        selectedAdmin.name = adminNameField.getText();
        selectedAdmin.email = emailField.getText();
        selectedAdmin.password = passwordField.getText();
        adminTable.refresh();
    }
    private void addAdmin(UserDTO newAdmin){
        newAdmin.name = adminNameField.getText();
        newAdmin.email = emailField.getText();
        newAdmin.password = passwordField.getText();
    
        Map<String, String> data = new HashMap<>();
        data.put("name", newAdmin.name);
        data.put("email", newAdmin.email);
        data.put("password", newAdmin.password);
        data.put("orgName", GlobalContext.getInstance().org.name);
    
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "";
        try {
            json = objectMapper.writeValueAsString(data);  // Convert Map to JSON string
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9090/auth/register/admin"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();
    
            GlobalContext.getInstance().client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(responseBody -> {
                    Platform.runLater(() -> {
                        System.out.println("Response Body: " + responseBody);
                        newAdmin.id = GlobalContext.getInstance().getAdminUserID(newAdmin.email);
                        adminList.add(newAdmin);  // Ensure adminList is a thread-safe collection if accessed by multiple threads
                    });
                })
                .exceptionally(e -> {
                    Platform.runLater(() -> {
                        System.out.println("Request Failed: " + e.getMessage());
                    });
                    return null;  // Ensure a return value for exceptionally()
                });
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getAdmins(){
        String url = "http://localhost:9090/org/admins/" + GlobalContext.getInstance().org.name;
    ObjectMapper objectMapper = new ObjectMapper();

    try {
        // Create HTTP request
        GlobalContext.getInstance().request = HttpRequest.newBuilder()
                .uri(URI.create(url))
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
                    adminList.add(admin);
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
