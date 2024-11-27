
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PassDTO {
    public long id;
    public String userId;
    public String orgId;
    public String issued;
    public String status;
}
