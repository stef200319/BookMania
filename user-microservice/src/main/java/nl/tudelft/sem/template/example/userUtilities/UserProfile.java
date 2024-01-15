package nl.tudelft.sem.template.example.userUtilities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    @Id
    private String username;

    private String bio;

    private String location;

    private String profilePicture;

    private String favoriteBook;

    @Valid
    @ElementCollection
    private List<String> favoriteGenres;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return Objects.equals(username, that.username) && Objects.equals(bio, that.bio) && Objects.equals(location, that.location) && Objects.equals(profilePicture, that.profilePicture) && Objects.equals(favoriteBook, that.favoriteBook) && Objects.equals(favoriteGenres, that.favoriteGenres);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, bio, location, profilePicture, favoriteBook, favoriteGenres);
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "username='" + username + '\'' +
                ", bio='" + bio + '\'' +
                ", location='" + location + '\'' +
                ", profilePicture='" + profilePicture + '\'' +
                ", favoriteBook='" + favoriteBook + '\'' +
                ", favoriteGenres=" + favoriteGenres +
                '}';
    }
}
