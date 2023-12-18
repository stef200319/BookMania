package nl.tudelft.sem.template.example.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Entity
public class User {

    @Id
    private String username;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

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

    private UserRoleEnum userRole;

    private Boolean isActive;

    private Boolean isBanned;

    private String bio;

    private String location;

    private String profilePicture;

    private String favoriteBook;

    @Valid
    private List<String> favoriteGenres;

    @Valid
    private List<@Valid User> followers;

    @Valid
    private List<@Valid User> following;

    public User username(String username) {
        this.username = username;
        return this;
    }

    /**
     * Get username
     * @return username
     */

    @Schema(name = "username", example = "theUser", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User firstName(String firstName) {
        this.firstName = firstName;
        return this;
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

    public User lastName(String lastName) {
        this.lastName = lastName;
        return this;
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

    public User email(String email) {
        this.email = email;
        return this;
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

    public User password(String password) {
        this.password = password;
        return this;
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

    public User userRole(UserRoleEnum userRole) {
        this.userRole = userRole;
        return this;
    }

    /**
     * User role (Regular or Author or Admin)
     * @return userRole
     */

    @Schema(name = "userRole", example = "Regular", description = "User role (Regular or Author or Admin)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("userRole")
    public UserRoleEnum getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRoleEnum userRole) {
        this.userRole = userRole;
    }

    public User isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    /**
     * Whether the user account is activated (not deactivated)
     * @return isActive
     */

    @Schema(name = "isActive", example = "true", description = "Whether the user account is activated (not deactivated)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("isActive")
    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public User isBanned(Boolean isBanned) {
        this.isBanned = isBanned;
        return this;
    }

    /**
     * Whether the user account is banned
     * @return isBanned
     */

    @Schema(name = "isBanned", example = "true", description = "Whether the user account is banned", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("isBanned")
    public Boolean getIsBanned() {
        return isBanned;
    }

    public void setIsBanned(Boolean isBanned) {
        this.isBanned = isBanned;
    }

    public User bio(String bio) {
        this.bio = bio;
        return this;
    }

    /**
     * User Bio
     * @return bio
     */

    @Schema(name = "bio", example = "Hey I am a person", description = "User Bio", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("bio")
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public User location(String location) {
        this.location = location;
        return this;
    }

    /**
     * Simple string that vaguely describes where the user is
     * @return location
     */

    @Schema(name = "location", example = "The Netherlands", description = "Simple string that vaguely describes where the user is", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("location")
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public User profilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
        return this;
    }

    /**
     * URL of an image
     * @return profilePicture
     */

    @Schema(name = "profilePicture", example = "https://server.net/image.jpg", description = "URL of an image", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("profilePicture")
    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public User favoriteBook(String favoriteBook) {
        this.favoriteBook = favoriteBook;
        return this;
    }

    /**
     * Name of favorite book
     * @return favoriteBook
     */

    @Schema(name = "favoriteBook", example = "Harry Potter", description = "Name of favorite book", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("favoriteBook")
    public String getFavoriteBook() {
        return favoriteBook;
    }

    public void setFavoriteBook(String favoriteBook) {
        this.favoriteBook = favoriteBook;
    }

    public User favoriteGenres(List<String> favoriteGenres) {
        this.favoriteGenres = favoriteGenres;
        return this;
    }

    public User addFavoriteGenresItem(String favoriteGenresItem) {
        if (this.favoriteGenres == null) {
            this.favoriteGenres = new ArrayList<>();
        }
        this.favoriteGenres.add(favoriteGenresItem);
        return this;
    }

    /**
     * Get favoriteGenres
     * @return favoriteGenres
     */

    @Schema(name = "favoriteGenres", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("favoriteGenres")
    public List<String> getFavoriteGenres() {
        return favoriteGenres;
    }

    public void setFavoriteGenres(List<String> favoriteGenres) {
        this.favoriteGenres = favoriteGenres;
    }

    public User followers(List<@Valid User> followers) {
        this.followers = followers;
        return this;
    }

    public User addFollowersItem(User followersItem) {
        if (this.followers == null) {
            this.followers = new ArrayList<>();
        }
        this.followers.add(followersItem);
        return this;
    }

    /**
     * Get followers
     * @return followers
     */
    @Valid
    @Schema(name = "followers", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("followers")
    public List<@Valid User> getFollowers() {
        return followers;
    }

    public void setFollowers(List<@Valid User> followers) {
        this.followers = followers;
    }

    public User following(List<@Valid User> following) {
        this.following = following;
        return this;
    }

    public User addFollowingItem(User followingItem) {
        if (this.following == null) {
            this.following = new ArrayList<>();
        }
        this.following.add(followingItem);
        return this;
    }

    /**
     * Get following
     * @return following
     */
    @Valid
    @Schema(name = "following", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("following")
    public List<@Valid User> getFollowing() {
        return following;
    }

    public void setFollowing(List<@Valid User> following) {
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
        return Objects.equals(this.username, user.username) &&
            Objects.equals(this.firstName, user.firstName) &&
            Objects.equals(this.lastName, user.lastName) &&
            Objects.equals(this.email, user.email) &&
            Objects.equals(this.password, user.password) &&
            Objects.equals(this.userRole, user.userRole) &&
            Objects.equals(this.isActive, user.isActive) &&
            Objects.equals(this.isBanned, user.isBanned) &&
            Objects.equals(this.bio, user.bio) &&
            Objects.equals(this.location, user.location) &&
            Objects.equals(this.profilePicture, user.profilePicture) &&
            Objects.equals(this.favoriteBook, user.favoriteBook) &&
            Objects.equals(this.favoriteGenres, user.favoriteGenres) &&
            Objects.equals(this.followers, user.followers) &&
            Objects.equals(this.following, user.following);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, firstName, lastName, email, password, userRole, isActive, isBanned, bio, location, profilePicture, favoriteBook, favoriteGenres, followers, following);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class User {\n");
        sb.append("    username: ").append(toIndentedString(username)).append("\n");
        sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
        sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
        sb.append("    email: ").append(toIndentedString(email)).append("\n");
        sb.append("    password: ").append(toIndentedString(password)).append("\n");
        sb.append("    userRole: ").append(toIndentedString(userRole)).append("\n");
        sb.append("    isActive: ").append(toIndentedString(isActive)).append("\n");
        sb.append("    isBanned: ").append(toIndentedString(isBanned)).append("\n");
        sb.append("    bio: ").append(toIndentedString(bio)).append("\n");
        sb.append("    location: ").append(toIndentedString(location)).append("\n");
        sb.append("    profilePicture: ").append(toIndentedString(profilePicture)).append("\n");
        sb.append("    favoriteBook: ").append(toIndentedString(favoriteBook)).append("\n");
        sb.append("    favoriteGenres: ").append(toIndentedString(favoriteGenres)).append("\n");
        sb.append("    followers: ").append(toIndentedString(followers)).append("\n");
        sb.append("    following: ").append(toIndentedString(following)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}


