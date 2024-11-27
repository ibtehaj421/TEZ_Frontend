import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.application.Platform;

import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class GlobalContext {
    public UserDTO user; //will cater for all types of users as it stores all required information..
    public List<UserDTO> listUsers = new ArrayList<UserDTO>();
    public getBusDTO bus;
    public List<getBusDTO> buses = new ArrayList<getBusDTO>();
    public Organization org; 
    public List<Organization> organizations = new ArrayList<Organization>();
    public static GlobalContext appContext;
    public HttpClient client;
    public HttpRequest request;
    public HttpResponse<String> response;
    public UserDTO driver;
    public List<UserDTO> drivers = new ArrayList<UserDTO>();
    public List<RouteDTO> routes = new ArrayList<RouteDTO>();
    public RouteDTO route;
    public List<FeedbackDTO> feedbacks = new ArrayList<FeedbackDTO>(); //for the organization to access.
    public FeedbackDTO feedback; //for submitting
    public PaymentDTO payment;
    public PassDTO pass;
    // public List<busTicketDTO> tickets = new ArrayList<BusTicketDTO>();
    // public busTicketDTO ticket;


    private GlobalContext(){
         client = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(10)) // Set a timeout
        .version(HttpClient.Version.HTTP_2) // Set HTTP version
        .build();
    }

    public static GlobalContext getInstance(){
            if(appContext == null){
                appContext = new GlobalContext();
            }
                return appContext;
    }

    public long getUserID(String email){
        long value = 0;
        try{
        request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:9090/user/id/"+email))
            .header("Authorization","Bearer "+GlobalContext.getInstance().user.jwt)
            .header("Content-Type", "application/json")
            .GET()
            .build();


        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() == 200){
                value = Long.parseLong(response.body());
        }
        } catch (Exception e){
                e.printStackTrace();
        }

        return value;
    }
    public long getAdminUserID(String email){
        long value = 0;
        try{
        request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:9090/org/user/"+email))
            .header("Content-Type", "application/json")
            .GET()
            .build();


        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() == 200){
                value = Long.parseLong(response.body());
        }
        } catch (Exception e){
                e.printStackTrace();
        }

        return value;
    }

    public void getBuses(){
        //populate the buses list
        try {
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

    public void getDrivers(){
        //populate the drivers list
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
            drivers = objectMapper.readValue(GlobalContext.getInstance().response.body(), new TypeReference<List<UserDTO>>(){});

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

    public void getRoutes(){
        //populate the routes list
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
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void getOrgList(){
        String url = "http://localhost:9090/fetch/all";
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
            GlobalContext.getInstance().organizations = objectMapper.readValue(GlobalContext.getInstance().response.body(), new TypeReference<List<Organization>>(){});
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getRoutesUnderOrg(){
        String url = "http://localhost:9090/public/route/get/"+org.name;
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
                    GlobalContext.getInstance().routes = objectMapper.readValue(GlobalContext.getInstance().response.body(), new TypeReference<List<RouteDTO>>(){});
                }
        } catch(Exception e){
            System.err.println(e.getMessage());
        }
    }

    public void getPass(){
        String url = "http://localhost:9090/user/pass/get/"+user.id;
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
                    GlobalContext.getInstance().pass = objectMapper.readValue(GlobalContext.getInstance().response.body(), new TypeReference<PassDTO>(){});
                }
        } catch(Exception e){
            System.err.println(e.getMessage());
        }
    }
}
