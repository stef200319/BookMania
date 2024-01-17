package nl.tudelft.sem.template.example.userUtilities;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    @Id
    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserInfo userInfo = (UserInfo) o;
        return Objects.equals(username, userInfo.username)
                && Objects.equals(firstName, userInfo.firstName)
                && Objects.equals(lastName, userInfo.lastName)
                && Objects.equals(email, userInfo.email)
                && Objects.equals(password, userInfo.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, firstName, lastName, email, password);
    }

    @Override
    public String toString() {
        return "UserInfo{"
                + "username='" + username + '\''
                + ", firstName='" + firstName + '\''
                + ", lastName='" + lastName + '\''
                + ", email='" + email + '\''
                + ", password='" + password + '\''
                + '}';
    }
}
