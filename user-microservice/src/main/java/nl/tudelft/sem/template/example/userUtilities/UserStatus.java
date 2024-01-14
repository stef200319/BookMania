package nl.tudelft.sem.template.example.userUtilities;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import nl.tudelft.sem.template.example.model.User;
import org.springframework.context.annotation.Bean;

import javax.persistence.Entity;

@AllArgsConstructor
public class UserStatus {

    private Boolean isActive;

    private Boolean isLoggedIn;

    private Boolean isBanned;

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

}
