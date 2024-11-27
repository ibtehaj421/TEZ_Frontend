import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

public class AdminAllocateController {

    @FXML
    private Button addBtn;

    @FXML
    private Button busBtn;

    @FXML
    private ComboBox<BusDTO> busDropDown; // Corrected to Bus class

    @FXML
    private Button confirmBtn;

    @FXML
    private TableColumn<RouteDTO, String> destinationColumn;

    @FXML
    private Button driverBtn;

    @FXML
    private ComboBox<UserDTO> driverDropDown; // UserDTO for Driver

    @FXML
    private Button homeBtn;

    @FXML
    private Button newBtn;

    @FXML
    private TableColumn<RouteDTO, String> originColumn;

    @FXML
    private Button removeBtn;

    @FXML
    private TableColumn<RouteDTO, String> routeColumn;

    @FXML
    private ComboBox<RouteDTO> routeDropDown; // RouteDTO

    @FXML
    private TableView<RouteDTO> routeTable;

    @FXML
    private Button updateBtn;

    // ObservableLists for dummy data
    private ObservableList<BusDTO> busList = FXCollections.observableArrayList(); // Corrected to Bus
    private ObservableList<UserDTO> driverList = FXCollections.observableArrayList(); // UserDTO for Driver
    private ObservableList<RouteDTO> routeList = FXCollections.observableArrayList();
    private ObservableList<RouteDTO> tableData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        originColumn.setCellValueFactory(new PropertyValueFactory<>("origin"));
        destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
        routeColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        //use the global context attributes to assing values into the drop downs.
        for(int i=0;i<GlobalContext.getInstance().buses.size();i++){
            busList.add(GlobalContext.getInstance().buses.get(i).bus);
        }
        busDropDown.setItems(busList);

        for(int i =0;i<GlobalContext.getInstance().drivers.size();i++){
            driverList.add(GlobalContext.getInstance().drivers.get(i));
        }
        driverDropDown.setItems(driverList);

        for(int i =0;i<GlobalContext.getInstance().routes.size();i++){
            routeList.add(GlobalContext.getInstance().routes.get(i));
        }
        routeDropDown.setItems(routeList);

        routeTable.setItems(tableData);

        // Custom Cell Factory for Driver ComboBox (displaying only driver name)
        driverDropDown.setCellFactory(new Callback<ListView<UserDTO>, ListCell<UserDTO>>() {
            @Override
            public ListCell<UserDTO> call(ListView<UserDTO> param) {
                return new ListCell<UserDTO>() {
                    @Override
                    protected void updateItem(UserDTO item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.getName()); // Displaying only the driver's name
                        }
                    }
                };
            }
        });

        driverDropDown.setButtonCell(new ListCell<UserDTO>() {
            @Override
            protected void updateItem(UserDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        // Custom Cell Factory for Bus ComboBox (displaying only bus number plate)
        busDropDown.setCellFactory(new Callback<ListView<BusDTO>, ListCell<BusDTO>>() {
            @Override
            public ListCell<BusDTO> call(ListView<BusDTO> param) {
                return new ListCell<BusDTO>() {
                    @Override
                    protected void updateItem(BusDTO item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.getNumberPlate()); // Displaying only the bus number plate
                        }
                    }
                };
            }
        });

        busDropDown.setButtonCell(new ListCell<BusDTO>() {
            @Override
            protected void updateItem(BusDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getNumberPlate());
                }
            }
        });

        // Custom Cell Factory for Route ComboBox (displaying origin - destination)
        routeDropDown.setCellFactory(new Callback<ListView<RouteDTO>, ListCell<RouteDTO>>() {
            @Override
            public ListCell<RouteDTO> call(ListView<RouteDTO> param) {
                return new ListCell<RouteDTO>() {
                    @Override
                    protected void updateItem(RouteDTO item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.getOrigin() + " - " + item.getDestination()); // Displaying origin -
                                                                                       // destination
                        }
                    }
                };
            }
        });

        routeDropDown.setButtonCell(new ListCell<RouteDTO>() {
            @Override
            protected void updateItem(RouteDTO item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getOrigin() + " - " + item.getDestination());
                }
            }
        });
    }

    @FXML
    void newBtnClicked(ActionEvent event) {
        busDropDown.setValue(null);
        driverDropDown.setValue(null);
        routeDropDown.setValue(null);
    }

    @FXML
    void addBtnClicked(ActionEvent event) {
        RouteDTO selectedRoute = routeDropDown.getSelectionModel().getSelectedItem();
        BusDTO selectedBus = busDropDown.getSelectionModel().getSelectedItem();
        UserDTO selectedDriver = driverDropDown.getSelectionModel().getSelectedItem();
        System.out.println(selectedRoute);
        System.out.println(selectedBus);
        System.out.println(selectedDriver);
        if (selectedRoute != null && selectedBus != null && selectedDriver != null) {
            // Create strings for each field
            updateBusDriver(selectedBus.id, selectedDriver.id);
            updateBusRoute(selectedBus.id, selectedRoute.id);
            String routeDetails = selectedRoute.getOrigin() + " - " + selectedRoute.getDestination(); // Route: Start -
                                                                                                      // End
            String busDetails = selectedBus.getNumberPlate(); // Bus: Number plate
            String driverDetails = selectedDriver.getName(); // Driver: Name

            // Create a new RouteDTO object to hold the correct data for the table
            RouteDTO routeWithBusAndDriver = new RouteDTO();

            // Set the correct values into each property
            routeWithBusAndDriver.origin = driverDetails; // Driver's name goes into 'origin' column
            routeWithBusAndDriver.destination = busDetails; // Bus's number plate goes into 'destination' column
            routeWithBusAndDriver.description = routeDetails; // Route details go into 'description' column

            // Add the new entry to the table
            tableData.add(routeWithBusAndDriver);
            routeTable.setItems(tableData); // Refresh the table

            // Remove selected items from ComboBoxes to prevent re-selection
            // busDropDown.getItems().remove(selectedBus);
            // driverDropDown.getItems().remove(selectedDriver);
            // routeDropDown.getItems().remove(selectedRoute);
        }
    }

    @FXML
    void confirmBtnClicked(ActionEvent event) {
        tableData.clear();
        routeTable.refresh();

        System.out.println("Notifications sent to drivers!");
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

    @FXML
    private void removeBtnClicked(ActionEvent event) {
        RouteDTO selectedItem = routeTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            tableData.remove(selectedItem);
        }
    }

    public void updateBusDriver(long bus,long driver){
        try {
            GlobalContext.getInstance().request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:9090/admin/bus/driver/" +bus+"/"+driver ))
                    .header("Authorization", "Bearer " + GlobalContext.getInstance().user.jwt)
                    .header("Content-Type", "application/json")
                    .PUT(BodyPublishers.noBody())
                    .build();

            // Send request and handle the response
            GlobalContext.getInstance().response = GlobalContext.getInstance().client.send(GlobalContext.getInstance().request, HttpResponse.BodyHandlers.ofString());

            // Print the response
            System.out.println("Response status code: " +GlobalContext.getInstance().response.statusCode());
            System.out.println("Response body: " + GlobalContext.getInstance().response.body());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void updateBusRoute(long bus,long route){
        try {
            GlobalContext.getInstance().request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:9090/admin/bus/route/" +bus+"/"+route ))
                    .header("Authorization", "Bearer " + GlobalContext.getInstance().user.jwt)
                    .header("Content-Type", "application/json")
                    .PUT(BodyPublishers.noBody())
                    .build();

            // Send request and handle the response
            GlobalContext.getInstance().response = GlobalContext.getInstance().client.send(GlobalContext.getInstance().request, HttpResponse.BodyHandlers.ofString());

            // Print the response
            System.out.println("Response status code: " +GlobalContext.getInstance().response.statusCode());
            System.out.println("Response body: " + GlobalContext.getInstance().response.body());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
