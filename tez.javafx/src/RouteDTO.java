import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RouteDTO {
    public long id;
    public String name;
    public String origin;
    public String destination;
    public String description;
    public List<BusDTO> buses;
    public String url;

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Origin: " + origin + ", Destination: " + destination;
    }
    public String getName(){
        return origin + " to " + destination;
    }
}