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

public class AdminBusManagementController {

    @FXML
    private Button newBtn;

    @FXML
    private TextField numPlateField;

    @FXML
    private TextField registrationYear;

    @FXML
    private Button removeBtn;

    @FXML
    private Button saveBtn;

    @FXML
    private Button updateBtn;

    @FXML
    private TableView<BusDTO> busTable;

    @FXML
    private TableColumn<BusDTO, String> licNumColumn;

    @FXML
    private TableColumn<BusDTO, String> modelColumn;

    private ObservableList<BusDTO> busList = FXCollections.observableArrayList();

    private BusDTO selectedBus;

    @SuppressWarnings("unused")
    @FXML
    public void initialize() {
        licNumColumn.setCellValueFactory(new PropertyValueFactory<>("licNum"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));

        
        busTable.setItems(busList);

        busTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedBus = newValue;

                numPlateField.setText(selectedBus.licNum);
                registrationYear.setText(selectedBus.model);
            }
        });
        getBuses();
    }

    @FXML
    void newBtnClicked(ActionEvent event) {
        numPlateField.clear();
        registrationYear.clear();
        selectedBus = null;
    }

    @FXML
    void removeBtnClicked(ActionEvent event) {
        if (selectedBus != null) {
            busList.remove(selectedBus);
            selectedBus = null;
            numPlateField.clear();
            registrationYear.clear();
        }
    }

    @FXML
    void saveBtnClicked(ActionEvent event) {
        // Get the input data
        String numberPlate = numPlateField.getText();
        String yearOfRegistration = registrationYear.getText();
        GlobalContext.getInstance().bus = new getBusDTO();
        GlobalContext.getInstance().bus.bus = new BusDTO();
        Map<String,String> data = new HashMap<String,String>();
        data.put("model", yearOfRegistration);
        data.put("licNum", numberPlate);
        data.put("capacity", "20");
        data.put("orgID", String.valueOf(GlobalContext.getInstance().user.orgid));
        GlobalContext.getInstance().bus.bus.model = yearOfRegistration;
        GlobalContext.getInstance().bus.bus.licNum = numberPlate;
        GlobalContext.getInstance().bus.bus.capacity = 20;
        GlobalContext.getInstance().bus.bus.orgID = GlobalContext.getInstance().user.orgid;
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        try {
            json = mapper.writeValueAsString(data);  // Convert Map to JSON string
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            GlobalContext.getInstance().request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:9090/admin/bus/add"))
                .header("Authorization", "Bearer "+ GlobalContext.getInstance().user.jwt)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                .build();
    
            GlobalContext.getInstance().client.sendAsync(GlobalContext.getInstance().request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(responseBody -> {
                    Platform.runLater(() -> {
                        System.out.println("Response Body: " + responseBody);
                        //if (!numberPlate.isEmpty() && !yearOfRegistration.isEmpty()) {
                        //    busList.add(GlobalContext.getInstance().bus.bus);
                        //}
                            busList.clear();
                            getBuses();
                            //busList.add(GlobalContext.getInstance().bus.bus);
                        //}
                        //busList.add(GlobalContext.getInstance().bus.bus);
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
        
        numPlateField.clear();
        registrationYear.clear();
    }

    @FXML
    void updateBtnClicked(ActionEvent event) {
        if (selectedBus != null) {
            selectedBus.setNumberPlate(numPlateField.getText());
            //selectedBus.setYearOfRegistration(registrationYear.getText());
            busTable.refresh();
        }
    }

    @FXML
    void routerBtnClicked(ActionEvent event) {
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

    public void getBuses(){
        //fetch the current users organization bus list.
        String url = "http://localhost:9090/admin/bus/get/" + GlobalContext.getInstance().user.orgid;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
        // Create HTTP request
        GlobalContext.getInstance().request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization","Bearer "+GlobalContext.getInstance().user.jwt)
                .header("Content-Type", "application/json")
                .GET()
                .build();

        // Send synchronous request and get the response
        GlobalContext.getInstance().response = GlobalContext.getInstance().client.send(GlobalContext.getInstance().request, HttpResponse.BodyHandlers.ofString());

        if (GlobalContext.getInstance().response.statusCode() == 200) {  // Check if the response is successful
            // Deserialize JSON response to a list of UserDTO objects
            GlobalContext.getInstance().buses = objectMapper.readValue(GlobalContext.getInstance().response.body(), new TypeReference<List<getBusDTO>>(){});

            // Process the list (update UI, etc.)
            Platform.runLater(() -> {
                // Add the fetched admins to the observable list or update UI components
                for(int i=0;i<GlobalContext.getInstance().buses.size();i++){
                        busList.add(GlobalContext.getInstance().buses.get(i).bus);
                    
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
