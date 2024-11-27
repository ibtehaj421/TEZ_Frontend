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

public class AdminRouteManagementController {

    @FXML
    private Button newBtn;

    @FXML
    private Button removeBtn;

    @FXML
    private Button saveBtn;

    @FXML
    private Button updateBtn;

    @FXML
    private TextField originField;

    @FXML
    private TextField destinationField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TableView<RouteDTO> routeTable;

    @FXML
    private TableColumn<RouteDTO, String> originColumn;

    @FXML
    private TableColumn<RouteDTO, String> destinationColumn;

    @FXML
    private TableColumn<RouteDTO, String> descriptionColumn;

    private ObservableList<RouteDTO> routeData = FXCollections.observableArrayList();

    private RouteDTO selectedRoute;

    @SuppressWarnings("unused")
    @FXML
    public void initialize() {
        // Set up the table columns
        originColumn.setCellValueFactory(new PropertyValueFactory<>("origin"));
        destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Create dummy routes
        

        // Add the routes to the observable list
       
        getRoutes();
        // Populate the TableView with data
        routeTable.setItems(routeData);

        routeTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedRoute = newValue;

                // Populate the input fields with the selected route's details
                originField.setText(selectedRoute.getOrigin());
                destinationField.setText(selectedRoute.getDestination());
                descriptionField.setText(selectedRoute.getDescription());
            }
        });
        
    }

    @FXML
    void newBtnClicked(ActionEvent event) {
        // Clear input fields for new route entry
        originField.clear();
        destinationField.clear();
        descriptionField.clear();
        selectedRoute = null; // Deselect any selected route
    }

    @FXML
    void removeBtnClicked(ActionEvent event) {
        if (selectedRoute != null) {
            routeData.remove(selectedRoute); // Remove the selected route from the list
            selectedRoute = null; // Clear the selection
            originField.clear(); // Clear input fields
            destinationField.clear();
            descriptionField.clear();
        }
    }

    @FXML
    void saveBtnClicked(ActionEvent event) {
        // Get the input data
        String origin = originField.getText();
        String destination = destinationField.getText();
        String description = descriptionField.getText();
        if (origin.isEmpty() || destination.isEmpty()  || description.isEmpty()) {
            return; //
        }
       String url = "http://localhost:9090/admin/route/add";
       Map<String,String> data = new HashMap<String,String>();
        data.put("name", "This is a route");
        data.put("origin", origin);
        data.put("destination", destination);
        data.put("description", description);
        data.put("url","Will be added later");
       ObjectMapper mapper = new ObjectMapper();
       String json = "";
       try {
           json = mapper.writeValueAsString(data);  // Convert Map to JSON string
       } catch (Exception e) {
           e.printStackTrace();
       }
        try {
            GlobalContext.getInstance().request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization","Bearer "+GlobalContext.getInstance().user.jwt)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();

                GlobalContext.getInstance().client.sendAsync(GlobalContext.getInstance().request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(responseBody -> {
                    Platform.runLater(() -> {
                        System.out.println("Response Body: " + responseBody);
                            
                            routeData.clear();
                            getRoutes();
                           
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

        
        originField.clear();
        destinationField.clear();
        descriptionField.clear();
    }

    @FXML
    void updateBtnClicked(ActionEvent event) {
        if (selectedRoute != null) {
            // Update the selected route details
            selectedRoute.origin = originField.getText();
            selectedRoute.destination = destinationField.getText();
            selectedRoute.description = descriptionField.getText();
            routeTable.refresh(); // Refresh the table to reflect updates
        }
    }

    @FXML
    void busBtnClicked(ActionEvent event) {
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
    void driverBtnClicked(ActionEvent event) {
        try {
            Parent mainRoot = FXMLLoader.load(getClass().getResource("/AdminDriverManagement.fxml"));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene mainScene = new Scene(mainRoot);

            stage.setScene(mainScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void homeBtnClicked(ActionEvent event) {
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

    void getRoutes(){
        System.out.println("THIS IS THE GET ROUTES METHID");
        String url = "http://localhost:9090/public/route/get";
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
            GlobalContext.getInstance().routes = objectMapper.readValue(GlobalContext.getInstance().response.body(), new TypeReference<List<RouteDTO>>(){});

            // Process the list (update UI, etc.)
            Platform.runLater(() -> {
                // Add the fetched admins to the observable list or update UI components
                for(int i=0;i<GlobalContext.getInstance().buses.size();i++){
                       routeData.add(GlobalContext.getInstance().routes.get(i));
                    
                }
            });
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
