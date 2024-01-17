package nl.tudelft.sem.template.example.userUtilities;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nl.tudelft.sem.template.example.model.User;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserStatus {

    @Id
    private String username;

    private Boolean isActive;

    private Boolean isLoggedIn;

    private Boolean isBanned;

    private User.UserRoleEnum userRole;

    @Override
    public String toString() {
        return "UserStatus{"
                + "username='" + username + '\''
                + ", isActive=" + isActive
                + ", isLoggedIn=" + isLoggedIn
                + ", isBanned=" + isBanned
                + ", userRole=" + userRole
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserStatus that = (UserStatus) o;
        return Objects.equals(username, that.username)
                && Objects.equals(isActive, that.isActive)
                && Objects.equals(isLoggedIn, that.isLoggedIn)
                && Objects.equals(isBanned, that.isBanned)
                && userRole == that.userRole;
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, isActive, isLoggedIn, isBanned, userRole);
    }
}
