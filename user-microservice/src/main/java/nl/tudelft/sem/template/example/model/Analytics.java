package nl.tudelft.sem.template.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Analytics {

    @Id
    private String userUsername;

    private Long reviewsNumber;

    private Long commentsNumber;

    private String lastLoginDate;

    private Long followersNumber;

    private Long followingNumber;

    /**
     * Constructs a new Analytics instance with default values besides username.
     *
     * @param userUsername The username of the user.
     */
    public Analytics(String userUsername) {
        this.userUsername = userUsername;
        this.reviewsNumber = 0L;
        this.commentsNumber = 0L;
        this.lastLoginDate = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now());
        this.followersNumber = 0L;
        this.followingNumber = 0L;
    }
    
    /**
     * Constructs a new Analytics instance with default values besides username and lastLoginDate.
     *
     * @param userUsername   The username of the user.
     * @param lastLoginDate  The date and time of the user's last login.
     */
    public Analytics(String userUsername, String lastLoginDate) {
        this.userUsername = userUsername;
        this.reviewsNumber = 0L;
        this.commentsNumber = 0L;
        this.lastLoginDate = lastLoginDate;
        this.followersNumber = 0L;
        this.followingNumber = 0L;
    }


    /**
     * Get userUsername.
     *
     * @return userUsername
     */

    @Schema(name = "userUsername", example = "theUser", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("userUsername")
    public String getUserUsername() {
        return userUsername;
    }

    /**
     * Set username.
     *
     * @param userUsername username of the user
     */
    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }


    /**
     * Get reviewsNumber.
     *
     * @return reviewsNumber
     */

    @Schema(name = "reviewsNumber", example = "7", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("reviewsNumber")
    public Long getReviewsNumber() {
        return reviewsNumber;
    }

    /**
     * Set user's reviews number.
     *
     * @param reviewsNumber review number that will be set
     */
    public void setReviewsNumber(Long reviewsNumber) {
        this.reviewsNumber = reviewsNumber;
    }


    /**
     * Get commentsNumber.
     *
     * @return commentsNumber
     */

    @Schema(name = "commentsNumber", example = "2", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("commentsNumber")
    public Long getCommentsNumber() {
        return commentsNumber;
    }

    /**
     * Set user's commentsNumber.
     *
     * @param commentsNumber comment number that will be set
     */
    public void setCommentsNumber(Long commentsNumber) {
        this.commentsNumber = commentsNumber;
    }


    /**
     * Get lastLoginDate.
     *
     * @return lastLoginDate
     */

    @Schema(name = "lastLoginDate", example = "12.02.2020", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("lastLoginDate")
    public String getLastLoginDate() {
        return lastLoginDate;
    }

    /**
     * Set user's lastLoginDate.
     *
     * @param lastLoginDate last login date of the user
     */
    public void setLastLoginDate(String lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }


    /**
     * Get followersNumber.
     *
     * @return followersNumber
     */

    @Schema(name = "followersNumber", example = "21", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("followersNumber")
    public Long getFollowersNumber() {
        return followersNumber;
    }

    /**
     * Set user's followers number.
     *
     * @param followersNumber followers number that will be set
     */
    public void setFollowersNumber(Long followersNumber) {
        this.followersNumber = followersNumber;
    }


    /**
     * Get followingNumber.
     *
     * @return followingNumber
     */

    @Schema(name = "followingNumber", example = "13", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("followingNumber")
    public Long getFollowingNumber() {
        return followingNumber;
    }

    /**
     * Set user's following number.
     *
     * @param followingNumber following number that will be set
     */
    public void setFollowingNumber(Long followingNumber) {
        this.followingNumber = followingNumber;
    }

    /**
     * Checks if this Analytics}object is equal to another object.
     *
     * @param o The object to compare.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Analytics analytics = (Analytics) o;
        return Objects.equals(this.userUsername, analytics.userUsername)
                && Objects.equals(this.reviewsNumber, analytics.reviewsNumber)
                && Objects.equals(this.commentsNumber, analytics.commentsNumber)
                && Objects.equals(this.lastLoginDate, analytics.lastLoginDate)
                && Objects.equals(this.followersNumber, analytics.followersNumber)
                && Objects.equals(this.followingNumber, analytics.followingNumber);
    }

    /**
     * Generates a hash code for this Analytics object.
     *
     * @return The hash code value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(userUsername, reviewsNumber, commentsNumber, lastLoginDate, followersNumber, followingNumber);
    }

    /**
     * Returns a string representation of this Analytics object.
     *
     * @return A string representation.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class Analytics {\n");
        sb.append("    userUsername: ").append(toIndentedString(userUsername)).append("\n");
        sb.append("    reviewsNumber: ").append(toIndentedString(reviewsNumber)).append("\n");
        sb.append("    commentsNumber: ").append(toIndentedString(commentsNumber)).append("\n");
        sb.append("    lastLoginDate: ").append(toIndentedString(lastLoginDate)).append("\n");
        sb.append("    followersNumber: ").append(toIndentedString(followersNumber)).append("\n");
        sb.append("    followingNumber: ").append(toIndentedString(followingNumber)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    public String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}