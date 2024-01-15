package nl.tudelft.sem.template.example.userUtilities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserProfileTest {
    @Test
    public void sameObjectEquals() {
        UserProfile u = new UserProfile("username", "bio", "loc", "profile", "book", new LinkedList<>());
        assertEquals(true, u.equals(u));
    }

    @Test
    public void nullEquals() {
        UserProfile u = new UserProfile("username", "bio", "loc", "profile", "book", new LinkedList<>());
        assertEquals(false, u.equals(null));
    }

    @Test
    public void goodEquals() {
        UserProfile u = new UserProfile("username", "bio", "loc", "profile", "book", new LinkedList<>());
        UserProfile u1 = new UserProfile("username", "bio", "loc", "profile", "book", new LinkedList<>());
        assertEquals(true, u.equals(u1));
    }
    @Test
    public void badEquals() {
        UserProfile u = new UserProfile("username", "bio", "loc", "profile", "book", new LinkedList<>());
        UserProfile u1 = new UserProfile("username", "bioe", "loc", "profile", "book", new LinkedList<>());
        assertEquals(false, u.equals(u1));
    }


    @Test
    public void hashTest() {
        UserProfile u = new UserProfile("username", "bio", "loc", "profile", "book", new LinkedList<>());
        UserProfile u1 = new UserProfile("username", "bio", "loc", "profile", "book", new LinkedList<>());
        assertEquals(u.hashCode(), u1.hashCode());
    }

    @Test
    public void badHashTest() {
        UserProfile u = new UserProfile("username", "bio", "loc", "profile", "book", new LinkedList<>());
        UserProfile u1 = new UserProfile("usernamee", "bio", "loc", "profile", "book", new LinkedList<>());
        assertNotEquals(u.hashCode(), u1.hashCode());
    }

    @Test
    public void toStringTest() {
        UserProfile u = new UserProfile("username", "bio", "loc", "profile", "book", new LinkedList<>());
        assertEquals("UserProfile{username='username', bio='bio', location='loc', profilePicture='profile', favoriteBook='book', favoriteGenres=[]}", u.toString());
    }
}
