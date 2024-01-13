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

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
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

    private Boolean isLoggedIn;

    private Boolean isBanned;

    private String bio;

    private String location;

    private String profilePicture;

    private String favoriteBook;

    @Valid
    @ElementCollection
    private List<String> favoriteGenres;

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

    /**
     * Whether the user is logged in the system
     * @return isLoggedIn
     */

    @Schema(name = "isLoggedIn", example = "true", description = "Whether the user account is activated (not deactivated)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("isLoggedIn")
    public Boolean getIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(Boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
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
            Objects.equals(this.isLoggedIn, user.isLoggedIn) &&
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
        return Objects.hash(username, firstName, lastName, email, password, userRole, isLoggedIn, isActive, isBanned, bio, location, profilePicture, favoriteBook, favoriteGenres, followers, following);
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
        sb.append("    isLoggedIn: ").append(toIndentedString(isLoggedIn)).append("\n");
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


