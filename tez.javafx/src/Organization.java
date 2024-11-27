
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Organization {
    public long id;
    public String name;
    public String type;

    public String getName(){
        return name;
    }
}
