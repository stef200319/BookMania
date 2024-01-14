package nl.tudelft.sem.template.example.model;

import java.net.URI;
import java.util.Objects;

import com.fasterxml.jackson.annotation.*;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;
import javax.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.tudelft.sem.template.example.userUtilities.UserProfile;
import nl.tudelft.sem.template.example.userUtilities.UserStatus;

@Data
@Entity
@AllArgsConstructor
public class User {

    @Id
    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    @OneToOne
    private UserStatus userStatus;

    @OneToOne
    private UserProfile userProfile;

    /**
     * User role (Regular or Author or Admin)
     */
    public enum UserRoleEnum {
        REGULAR("Regular"),

        ADMIN("Admin"),

        AUTHOR("Author");

        private String value;

        UserRoleEnum(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static UserRoleEnum fromValue(String value) {
            for (UserRoleEnum b : UserRoleEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }
    }


    @Valid
    @ElementCollection
    private List<String> followers;

    @Valid
    @ElementCollection
    private List<String> following;

    /**
     * Get username
     * @return username
     */

    public User() {
        this.userStatus = new UserStatus();
        this.userProfile = new UserProfile();
    }

    @Schema(name = "username", example = "theUser", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Get firstName
     * @return firstName
     */

    @Schema(name = "firstName", example = "John", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("firstName")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Get lastName
     * @return lastName
     */

    @Schema(name = "lastName", example = "James", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("lastName")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Get email
     * @return email
     */

    @Schema(name = "email", example = "john@email.com", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Get password
     * @return password
     */

    @Schema(name = "password", example = "12345", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public User addFollowersItem(User followersItem) {
        if (this.followers == null) {
            this.followers = new ArrayList<>();
        }
        this.followers.add(followersItem.getUsername());
        return this;
    }

    /**
     * Get followers
     * @return followers
     */
    @Valid
    @Schema(name = "followers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("followers")
    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers;
    }

    public User addFollowingItem(User followingItem) {
        if (this.following == null) {
            this.following = new ArrayList<>();
        }
        this.following.add(followingItem.getUsername());
        return this;
    }

    /**
     * Get following
     * @return following
     */
    @Valid
    @Schema(name = "following", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("following")
    public List<String> getFollowing() {
        return following;
    }

    public void setFollowing(List<String> following) {
        this.following = following;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(userStatus, user.userStatus) && Objects.equals(userProfile, user.userProfile) && Objects.equals(followers, user.followers) && Objects.equals(following, user.following);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, firstName, lastName, email, password, userStatus, userProfile, followers, following);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", userStatus=" + userStatus +
                ", userProfile=" + userProfile +
                ", followers=" + followers +
                ", following=" + following +
                '}';
    }
}


