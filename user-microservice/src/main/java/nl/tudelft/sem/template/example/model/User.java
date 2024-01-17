package nl.tudelft.sem.template.example.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import nl.tudelft.sem.template.example.userUtilities.UserInfo;
import nl.tudelft.sem.template.example.userUtilities.UserProfile;
import nl.tudelft.sem.template.example.userUtilities.UserStatus;

@Data
@Entity
@AllArgsConstructor
public class User {

    @Id
    private String username;

    @OneToOne
    private UserInfo userInfo;

    @OneToOne
    private UserStatus userStatus;

    @OneToOne
    private UserProfile userProfile;

    /**
     * User role (Regular or Author or Admin).
     */
    public enum UserRoleEnum {
        REGULAR("Regular"),

        ADMIN("Admin"),

        AUTHOR("Author");

        private String value;

        /**
         * Creates a UserRoleEnum.
         *
         * @param value Either "Regular", "Admin", or "Author"
         */
        UserRoleEnum(String value) {
            this.value = value;
        }

        /**
         * Get the value of the enum.
         *
         * @return The value of the enum.
         */
        @JsonValue
        public String getValue() {
            return value;
        }

        /**
         * Returns the enum as a string.
         *
         * @return The enum as a string.
         */
        @Override
        public String toString() {
            return String.valueOf(value);
        }

        /**
         * Creates a UserRoleEnum from a value string.
         *
         * @param value The value string.
         * @return The UserRoleEnum.
         */
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
     * Constructs a new User object with default user status, user profile, and user information.
     *
     * <p>
     * This constructor initializes a new User instance with default values for user status, user profile,
     * and user information. It ensures that these components are initialized and ready for further customization.
     * </p>
     */

    public User() {
        this.userStatus = new UserStatus();
        this.userProfile = new UserProfile();
        this.userInfo = new UserInfo();
    }

    @Schema(name = "username", example = "theUser", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    //    /**
    //     * Get firstName
    //     * @return firstName
    //     */
    //
    //    @Schema(name = "firstName", example = "John", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    //    @JsonProperty("firstName")
    //    public String getFirstName() {
    //        return firstName;
    //    }
    //
    //    public void setFirstName(String firstName) {
    //        this.firstName = firstName;
    //    }
    //
    //    /**
    //     * Get lastName
    //     * @return lastName
    //     */
    //
    //    @Schema(name = "lastName", example = "James", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    //    @JsonProperty("lastName")
    //    public String getLastName() {
    //        return lastName;
    //    }
    //
    //    public void setLastName(String lastName) {
    //        this.lastName = lastName;
    //    }
    //
    //    /**
    //     * Get email
    //     * @return email
    //     */
    //
    //    @Schema(name = "email", example = "john@email.com", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    //    @JsonProperty("email")
    //    public String getEmail() {
    //        return email;
    //    }
    //
    //    public void setEmail(String email) {
    //        this.email = email;
    //    }
    //
    //    /**
    //     * Get password
    //     * @return password
    //     */
    //
    //    @Schema(name = "password", example = "12345", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    //    @JsonProperty("password")
    //    public String getPassword() {
    //        return password;
    //    }
    //
    //    public void setPassword(String password) {
    //        this.password = password;
    //    }

    /**
     * Adds a user to the list of followers for the current user.
     *
     * <p>
     * This method adds the specified user to the list of followers for the current user.
     * If the followers list is null, it is initialized as a new ArrayList. The modified
     * user object is returned to support method chaining.
     * </p>
     *
     * @param followersItem The user to be added to the list of followers.
     * @return The modified user object with the updated list of followers.
     */

    public User addFollowersItem(User followersItem) {
        if (this.followers == null) {
            this.followers = new ArrayList<>();
        }
        this.followers.add(followersItem.getUsername());
        return this;
    }

    /**
     * Get followers.
     *
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

    /**
     * Adds a user to the list of users being followed by the current user.
     *
     * <p>
     * This method adds the specified user to the list of users being followed by the current user.
     * If the following list is null, it is initialized as a new ArrayList. The modified user object
     * is returned to support method chaining.
     * </p>
     *
     * @param followingItem The user to be added to the list of users being followed.
     * @return The modified user object with the updated list of users being followed.
     */

    public User addFollowingItem(User followingItem) {
        if (this.following == null) {
            this.following = new ArrayList<>();
        }
        this.following.add(followingItem.getUsername());
        return this;
    }

    /**
     * Get following.
     *
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(username, user.username)
                && Objects.equals(userInfo, user.userInfo)
                && Objects.equals(userStatus, user.userStatus)
                && Objects.equals(userProfile, user.userProfile)
                && Objects.equals(followers, user.followers)
                && Objects.equals(following, user.following);
    }

    /**
     * Return the hashCode of the user.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(username, userInfo, userStatus, userProfile, followers, following);
    }

    /**
     * Returns the user as a string.
     *
     * @return The user string.
     */
    @Override
    public String toString() {
        return "User{"
                + "username='" + username + '\''
                + ", userInfo=" + userInfo
                + ", userStatus=" + userStatus
                + ", userProfile=" + userProfile
                + ", followers=" + followers
                + ", following=" + following
                + '}';
    }
}


