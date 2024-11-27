import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.Node;

public class PassengerFeedbackController {

    @FXML
    private TextArea feedbackField;

    @FXML
    private Button homeBtn;

    @FXML
    private Button submitBtn;

    @FXML
    private Label submittedLabel;

    @FXML
    private ComboBox<Organization> orgDropDown;

    @FXML
    private ObservableList<Organization> orgList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        GlobalContext.getInstance().getOrgList();
        for(int i=0;i<GlobalContext.getInstance().organizations.size();i++){
            if(GlobalContext.getInstance().user.role.equals("STUDENT")){
                if(GlobalContext.getInstance().organizations.get(i).type.equals("EDU_ADMIN")){
                    orgList.add(GlobalContext.getInstance().organizations.get(i));
                }
            }
            else if(GlobalContext.getInstance().user.role.equals("BASIC")){
                if(GlobalContext.getInstance().organizations.get(i).type.equals("CORP_ADMIN")){
                    orgList.add(GlobalContext.getInstance().organizations.get(i));
                }
            }
            System.out.println(GlobalContext.getInstance().organizations.get(i).name);
        }
        orgDropDown.setItems(orgList);

        orgDropDown.setCellFactory(new Callback<ListView<Organization>, ListCell<Organization>>() {
            @Override
            public ListCell<Organization> call(ListView<Organization> param) {
                return new ListCell<Organization>() {
                    @Override
                    protected void updateItem(Organization item, boolean empty) {
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
        orgDropDown.setButtonCell(new ListCell<Organization>() {
            @Override
            protected void updateItem(Organization item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

    }


    @FXML
    void homeBtnClicked(ActionEvent event) {
        try {
            Parent mainRoot = FXMLLoader.load(getClass().getResource("PassengerHomePage.fxml"));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            Scene mainScene = new Scene(mainRoot);

            stage.setScene(mainScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void submitBtnClicked(ActionEvent event) {
        if(feedbackField.getText().length() > 10){
        String url = "http://localhost:9090/submit/user/feedback";
        Map<String,String> map = new HashMap<String,String>();
            map.put("userId", String.valueOf(GlobalContext.getInstance().user.id));
            map.put("orgid", orgDropDown.getValue().name);
            map.put("content", "(USER) "+feedbackField.getText());
            ObjectMapper objectMapper = new ObjectMapper();
            String json = "";
            try {
                json = objectMapper.writeValueAsString(map);  // Convert Map to JSON string
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                    GlobalContext.getInstance().request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Authorization","Bearer " + GlobalContext.getInstance().user.jwt)
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                        .build();
                
                    GlobalContext.getInstance().response = GlobalContext.getInstance().client.send(GlobalContext.getInstance().request, HttpResponse.BodyHandlers.ofString());
                    System.out.println(GlobalContext.getInstance().response.body());
                } catch (Exception e) {
                    e.printStackTrace();
                }
        
        feedbackField.clear();
        submittedLabel.setText("Your feedback has been submitted");
            }

    }

}
