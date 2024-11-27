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
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

public class PassengerBusTrackerController {

    @FXML
    private TableView<BusDTO> busTable;

    @FXML
    private Button homeBtn;

    @FXML
    private TableColumn<BusDTO, String> licNumColumn;

    @FXML
    private TableColumn<BusDTO, String> modelColumn;

    @FXML
    private TableColumn<BusDTO, Void> reserveSeatColumn;

    @FXML
    ObservableList<BusDTO> busList = FXCollections.observableArrayList();
    @FXML
    void homeBtnClicked(ActionEvent event) {
        // Home button action
    }

    @FXML
    public void initialize() {
        // Add dummy data to the table
        getCurrenOrgBus();
        for(int i=0;i<GlobalContext.getInstance().buses.size();i++){
            busList.add(GlobalContext.getInstance().buses.get(i).bus);
        }
        busTable.setItems(busList);

        // Set cell value factories for the columns
        licNumColumn.setCellValueFactory(new PropertyValueFactory<>("licNum"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));

        // Add buttons to the reserveSeatColumn (Reserve Seat button)
        reserveSeatColumn.setCellFactory(new Callback<TableColumn<BusDTO, Void>, TableCell<BusDTO, Void>>() {
            @SuppressWarnings("unused")
            @Override
            public TableCell<BusDTO, Void> call(TableColumn<BusDTO, Void> param) {
                return new TableCell<BusDTO, Void>() {
                    private final Button reserveSeatBtn = new Button("Reserve Seat");

                    {
                        reserveSeatBtn.setOnAction(event -> reserveSeatBtnClicked(getTableRow().getItem()));
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(reserveSeatBtn);
                        }
                    }
                };
            }
        });
    }

    // Method for the reserve seat button
    public void reserveSeatBtnClicked(BusDTO bus) {
        System.out.println("Reserving a seat for bus: " + bus.getLicNum() + "BusID: " + bus.id);
        try {
            //http request to reserve a seat for the bus.
            String url = "http://localhost:9090/user/seat/reserve";
            
            Map<String,String> map = new HashMap<String,String>();
            map.put("userId", String.valueOf(GlobalContext.getInstance().user.id));
            map.put("busid", String.valueOf(bus.id));
            System.out.println(bus.id);
            ObjectMapper objectMapper = new ObjectMapper();
            String json = "";
            try {
                json = objectMapper.writeValueAsString(map);  // Convert Map to JSON string
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

            //create the request.
            GlobalContext.getInstance().request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Authorization","Bearer " +GlobalContext.getInstance().user.jwt)
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                        .build();
            
            //send the request
            GlobalContext.getInstance().response = GlobalContext.getInstance().client.send(GlobalContext.getInstance().request, HttpResponse.BodyHandlers.ofString());

            if(GlobalContext.getInstance().response.body().contains("Success")){
                System.out.println("Seat reserved successfully");
            }
            else {
                System.out.println("Failed to reserve seat. Status code: " + GlobalContext.getInstance().response.statusCode());
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    void getCurrenOrgBus(){
        try {
            String url = "http://localhost:9090/public/bus/route/" + GlobalContext.getInstance().route.id;
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

        } else {
            System.out.println("Request failed with status code: " + GlobalContext.getInstance().response.statusCode());
        }

    } catch (Exception e) {
        e.printStackTrace();
        Platform.runLater(() -> {
            System.out.println("Request Failed: " + e.getMessage());
        });
    }
        } catch (Exception e) {
                e.printStackTrace();
        }
    }
}
