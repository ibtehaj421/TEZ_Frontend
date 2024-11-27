import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AdminFeedbackController {

    @FXML
    private TableView<FeedbackDTO> responseTable;

    @FXML
    private Label displayFeedback;

    @FXML
    private Button homeBtn;

    @FXML
    private Button removeBtn;

    @FXML
    private TableColumn<FeedbackDTO, String> feedbackTable; // Correctly refers to the "response" field in Feedback class

    private ObservableList<FeedbackDTO> responses = FXCollections.observableArrayList();
    private FeedbackDTO selectedFeedback;

    @SuppressWarnings("unused")
    @FXML
    public void initialize() {
        // Bind TableColumn to the 'response' field in Feedback class
        feedbackTable.setCellValueFactory(new PropertyValueFactory<>("content"));

        // Adding some dummy data for testing
        // responses.add(new Feedback(1,
        //         "This bus service is smoother than my grandpa's dance moves at weddings. The seats are so comfy that I forgot I was commuting and thought I was on a luxury cruise... until we hit a speed bump and I remembered. Keep up the awesome work!"));
        // responses.add(new Feedback(2,
        //         "This bus takes so long, I had time to reflect on all my poor life choices, including choosing this bus. I could have walked faster, but at least I had time to finish three novels, rethink my career, and learn crochet. Thanks for that, I guess?"));
        // responses.add(new Feedback(3,
        //         "This bus service is like my ex—promises a lot, delivers nothing. I waited for what felt like a lifetime, only to be stood up by the bus. I guess I'll just start walking from now on. At least I'll get my steps in."));
        getFeedbackList();
        for(int i=0;i<GlobalContext.getInstance().feedbacks.size();i++){
            responses.add(GlobalContext.getInstance().feedbacks.get(i));
        }
        // Set the items to the table
        responseTable.setItems(responses);

        // Add listener to display feedback when row is selected
        responseTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectedFeedback = newValue;

                // Update the Label with the selected feedback's response
                displayFeedback.setText(selectedFeedback.getResponse());
            }
        });
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
    void removeBtnClicked(ActionEvent event) {
        if (selectedFeedback != null) {
            responses.remove(selectedFeedback); // Remove the selected feedback from the list
            selectedFeedback = null; // Clear the selection
            displayFeedback.setText(""); // Clear the label display
        }
    }
    public void getFeedbackList(){
        String url = "http://localhost:9090/feedback/view/"+GlobalContext.getInstance().user.orgid;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            GlobalContext.getInstance().request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization","Bearer "+GlobalContext.getInstance().user.jwt)
                .header("Content-Type", "application/json")
                .GET()
                .build();

                GlobalContext.getInstance().response = GlobalContext.getInstance().client.send(GlobalContext.getInstance().request,HttpResponse.BodyHandlers.ofString() );

                if (GlobalContext.getInstance().response.statusCode() == 200) {  // Check if the response is successful
                    // Deserialize JSON response to a list of UserDTO objects
                    GlobalContext.getInstance().feedbacks = objectMapper.readValue(GlobalContext.getInstance().response.body(), new TypeReference<List<FeedbackDTO>>(){});
                }
        } catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
}
