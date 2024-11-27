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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Callback;

public class busPassController {

    @FXML
    private Circle activeTab;

    @FXML
    private Label dateLabel;

    @FXML
    private Button genPassBtn;

    @FXML
    private Button homeBtn;

    @FXML
    private Label nameLabel;

    @FXML
    private ComboBox<Organization> orgDropDown;

    @FXML
    private ObservableList<Organization> orgList = FXCollections.observableArrayList();

    @FXML
    private Label orgLabel;

    @FXML
    private Button payBtn;

    @FXML
    private Button logoutBtn;

    @FXML
    public void initialize() {
        
        initSet();
        
    }

    @FXML
    void initSet(){
        fillOrgs();
        payBtn.setOpacity(0.0);
        payBtn.setDisable(true);
        GlobalContext.getInstance().getPass();
        boolean active = true;
        if (GlobalContext.getInstance().pass != null) {
            try {
            genPassBtn.setOpacity(0.0);
            orgDropDown.setOpacity(0.0);

            activeTab.setFill(Color.LIGHTGREEN);
            nameLabel.setText(GlobalContext.getInstance().user.name);
            orgLabel.setText(GlobalContext.getInstance().pass.orgId); //now stores the org name
            dateLabel.setText(GlobalContext.getInstance().pass.issued);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            if (GlobalContext.getInstance().pass.status.equals("pending")) {
                activeTab.setFill(Color.RED);
                payBtn.setOpacity(1.0);
                payBtn.setDisable(active);
            }
        } else {
            activeTab.setFill(Color.RED);
            nameLabel.setText("XXXX");
            orgLabel.setText("XXXXXXXX");
            dateLabel.setText("XXXXXXX");
            genPassBtn.setDisable(false);
            orgDropDown.setDisable(false);
        }
    }
    @FXML
    void genPassBtn(ActionEvent event) {
        //will come to this soon.
        String url = "http://localhost:9090/user/pass/add/"+GlobalContext.getInstance().user.id;
        
        try {
            
            Map<String,String> map = new HashMap<String,String>();
            map.put("name", orgDropDown.getValue().name);
            ObjectMapper objectMapper = new ObjectMapper();
            String json = "";
            try {
                json = objectMapper.writeValueAsString(map);  // Convert Map to JSON string
            } catch (Exception e) {
                e.printStackTrace();
            }
                    GlobalContext.getInstance().request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Authorization","Bearer " + GlobalContext.getInstance().user.jwt)
                        .header("Content-Type", "application/json")
                        .POST(HttpRequest.BodyPublishers.ofString(json))
                        .build();
                
                    GlobalContext.getInstance().response = GlobalContext.getInstance().client.send(GlobalContext.getInstance().request, HttpResponse.BodyHandlers.ofString());
                    if(GlobalContext.getInstance().response.body().contains("Successfully")){
                        initSet();
                    }
                    System.out.println(GlobalContext.getInstance().response.body());
                } catch (Exception e) {
                    e.printStackTrace();
                }
        
    }

    @FXML
    void homeBtnClicked(ActionEvent event) {
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

    @FXML
    void logoutBtnClicked(ActionEvent event) {
        try {
            Parent signUpRoot = FXMLLoader.load(getClass().getResource("MainScene.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene signUpScene = new Scene(signUpRoot);
            stage.setScene(signUpScene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void payBtnClicked(ActionEvent event) {

    }

    @FXML
    void fillOrgs(){
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

}
