
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {
    // data transfer object for user after login.
    public long id;
    public String name;
    public String email;
    public String password;
    public String role; // the admins will get their roles from the organization admin.
    public int level; // organization id sent back
    public long orgid;
    public String jwt;
    public String orgName;
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "ID: "+id+"Username: " + name + ", Password: " + password + "email: " + email + ", Level: " + level + "role: " + role + "org: " + orgid +"level: "+level +"jwt-token: " + jwt;
    }
}
