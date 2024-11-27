public class Notifications {
    private int id;
    private String response;

    // Constructor
    public Notifications(int id, String response) {
        this.id = id;
        this.response = response;
    }

    // Getter for response
    public String getResponse() {
        return response;
    }

    // Optional getter for id, if needed
    public int getId() {
        return id;
    }
}
