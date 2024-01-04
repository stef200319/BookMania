package nl.tudelft.sem.template.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@NoArgsConstructor
public class Analytics {

    @Id
    private String userUsername;

    private Long reviewsNumber;

    private Long commentsNumber;

    private String lastLoginDate;

    private Long followersNumber;

    private Long followingNumber;

    public Analytics userUsername(String userUsername) {
        this.userUsername = userUsername;
        return this;
    }

    /**
     * Get userUsername
     * @return userUsername
     */

    @Schema(name = "userUsername", example = "theUser", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("userUsername")
    public String getUserUsername() {
        return userUsername;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }

    public Analytics reviewsNumber(Long reviewsNumber) {
        this.reviewsNumber = reviewsNumber;
        return this;
    }

    /**
     * Get reviewsNumber
     * @return reviewsNumber
     */

    @Schema(name = "reviewsNumber", example = "7", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("reviewsNumber")
    public Long getReviewsNumber() {
        return reviewsNumber;
    }

    public void setReviewsNumber(Long reviewsNumber) {
        this.reviewsNumber = reviewsNumber;
    }

    public Analytics commentsNumber(Long commentsNumber) {
        this.commentsNumber = commentsNumber;
        return this;
    }

    /**
     * Get commentsNumber
     * @return commentsNumber
     */

    @Schema(name = "commentsNumber", example = "2", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("commentsNumber")
    public Long getCommentsNumber() {
        return commentsNumber;
    }

    public void setCommentsNumber(Long commentsNumber) {
        this.commentsNumber = commentsNumber;
    }

    public Analytics lastLoginDate(String lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
        return this;
    }

    /**
     * Get lastLoginDate
     * @return lastLoginDate
     */

    @Schema(name = "lastLoginDate", example = "12.02.2020", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("lastLoginDate")
    public String getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(String lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public Analytics followersNumber(Long followersNumber) {
        this.followersNumber = followersNumber;
        return this;
    }

    /**
     * Get followersNumber
     * @return followersNumber
     */

    @Schema(name = "followersNumber", example = "21", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("followersNumber")
    public Long getFollowersNumber() {
        return followersNumber;
    }

    public void setFollowersNumber(Long followersNumber) {
        this.followersNumber = followersNumber;
    }

    public Analytics followingNumber(Long followingNumber) {
        this.followingNumber = followingNumber;
        return this;
    }

    /**
     * Get followingNumber
     * @return followingNumber
     */

    @Schema(name = "followingNumber", example = "13", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("followingNumber")
    public Long getFollowingNumber() {
        return followingNumber;
    }

    public void setFollowingNumber(Long followingNumber) {
        this.followingNumber = followingNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Analytics analytics = (Analytics) o;
        return Objects.equals(this.userUsername, analytics.userUsername) &&
                Objects.equals(this.reviewsNumber, analytics.reviewsNumber) &&
                Objects.equals(this.commentsNumber, analytics.commentsNumber) &&
                Objects.equals(this.lastLoginDate, analytics.lastLoginDate) &&
                Objects.equals(this.followersNumber, analytics.followersNumber) &&
                Objects.equals(this.followingNumber, analytics.followingNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userUsername, reviewsNumber, commentsNumber, lastLoginDate, followersNumber, followingNumber);
    }

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
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}