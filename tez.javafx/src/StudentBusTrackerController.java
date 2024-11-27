import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import java.awt.Desktop;
import java.net.URI;

public class StudentBusTrackerController {

    @FXML
    private TableView<BusDTO> busTable;

    @FXML
    private Button homeBtn;

    @FXML
    private TableColumn<BusDTO, String> licNumColumn;

    @FXML
    private TableColumn<BusDTO, String> modelColumn;

    @FXML
    private TableColumn<BusDTO, Void> seatViewerColumn;

    @FXML
    private TableColumn<BusDTO, Void> trackColumn;

    @FXML
    ObservableList<BusDTO> busList = FXCollections.observableArrayList();
    @FXML
    void homeBtnClicked(ActionEvent event) {
        // Home button action
    }

    @FXML
    public void initialize() {
        // Add dummy data to the table
        busTable.setItems(busList);

        // Set cell value factories for the columns
        licNumColumn.setCellValueFactory(new PropertyValueFactory<>("licNum"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("model"));

        // Add buttons and hyperlinks to the respective columns

        // Seat viewer button (seatViewerColumn)
        seatViewerColumn.setCellFactory(new Callback<TableColumn<BusDTO, Void>, TableCell<BusDTO, Void>>() {
            @SuppressWarnings("unused")
            @Override
            public TableCell<BusDTO, Void> call(TableColumn<BusDTO, Void> param) {
                return new TableCell<BusDTO, Void>() {
                    private final Button seatViewerBtn = new Button("View Seats");

                    {
                        seatViewerBtn.setOnAction(event -> seatViewerBtnClicked(getTableRow().getItem()));
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(seatViewerBtn);
                        }
                    }
                };
            }
        });

        // Track column with Hyperlink button
        trackColumn.setCellFactory(new Callback<TableColumn<BusDTO, Void>, TableCell<BusDTO, Void>>() {
            @SuppressWarnings("unused")
            @Override
            public TableCell<BusDTO, Void> call(TableColumn<BusDTO, Void> param) {
                return new TableCell<BusDTO, Void>() {
                    private final Hyperlink trackLink = new Hyperlink("Track");

                    {
                        trackLink.setOnAction(event -> trackBus(getTableRow().getItem()));
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(trackLink);
                        }
                    }
                };
            }
        });
    }

    // Method for the seat viewer button
    public void seatViewerBtnClicked(BusDTO bus) {
        System.out.println("Seat viewer for bus: " + bus.getLicNum());
        // You can navigate to another screen or open a detailed view of bus seating
        // here
    }

    public void trackBus(BusDTO bus) {
        try {
            String url = "https://maps.app.goo.gl/VfzWwrGYr3Ejh1oC8"; // Using the route value as part of the URL
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
