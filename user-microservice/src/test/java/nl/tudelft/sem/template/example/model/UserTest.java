package nl.tudelft.sem.template.example.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;


public class UserTest {

    private User user;

    @BeforeEach
    public void setUp() {
        List<String> favGenres = new LinkedList<>();
        favGenres.add("SF");

        user = new User("username", "Surname", "Name", "email", "password", User.UserRoleEnum.REGULAR, true, true, false, "bio", "location", "picture", "favoriteBook", favGenres, null, null);
    }

    @Test
    public void testEmptyConstructor() {
        User user2 = new User();
        assertNotNull(user2);
    }

    @Test
    public void testGetUsername() {
        assertEquals("username", user.getUsername());
    }

    @Test
    public void testSetUsername() {
        user.setUsername("test");
        assertEquals("test", user.getUsername());
    }

    @Test
    public void testGetFirstName() {
        assertEquals("Surname", user.getFirstName());
    }

    @Test
    public void testSetFirstName() {
        user.setFirstName("test");
        assertEquals("test", user.getFirstName());
    }

    @Test
    public void testGetLastName() {
        assertEquals("Name", user.getLastName());
    }

    @Test
    public void testSetLastName() {
        user.setLastName("test");
        assertEquals("test", user.getLastName());
    }

    @Test
    public void testGetEmail() {
        assertEquals("email", user.getEmail());
    }

    @Test
    public void testSetEmail() {
        user.setEmail("test");
        assertEquals("test", user.getEmail());
    }

    @Test
    public void testGetPassword() {
        assertEquals("password", user.getPassword());
    }

    @Test
    public void testSetPassword() {
        user.setPassword("test");
        assertEquals("test", user.getPassword());
    }

    @Test
    public void testGetRole() {
        assertEquals(User.UserRoleEnum.REGULAR, user.getUserRole());
    }

    @Test
    public void testSetRole() {
        user.setUserRole(User.UserRoleEnum.AUTHOR);
        assertEquals(User.UserRoleEnum.AUTHOR, user.getUserRole());
    }

    @Test
    public void testGetLoggedIn() {
        assertEquals(true, user.getIsLoggedIn());
    }

    @Test
    public void testSetLoggedIn() {
        user.setIsLoggedIn(false);
        assertEquals(false, user.getIsLoggedIn());
    }

    @Test
    public void testGetActive() {
        assertEquals(true, user.getIsActive());
    }

    @Test
    public void testSetActive() {
        user.setIsActive(false);
        assertEquals(false, user.getIsActive());
    }

    @Test
    public void testGetBanned() {
        assertEquals(false, user.getIsBanned());
    }

    @Test
    public void testSetBanned() {
        user.setIsBanned(true);
        assertEquals(true, user.getIsBanned());
    }

    @Test
    public void testGetBio() {
        assertEquals("bio", user.getBio());
    }

    @Test
    public void testSetBio() {
        user.setBio("test");
        assertEquals("test", user.getBio());
    }

    @Test
    public void testGetLocation() {
        assertEquals("location", user.getLocation());
    }

    @Test
    public void testSetLocation() {
        user.setLocation("test");
        assertEquals("test", user.getLocation());
    }

    @Test
    public void testGetProfilePicture() {
        assertEquals("picture", user.getProfilePicture());
    }

    @Test
    public void testSetProfilePicture() {
        user.setProfilePicture("test");
        assertEquals("test", user.getProfilePicture());
    }

    @Test
    public void testGetFavoriteBook() {
        assertEquals("favoriteBook", user.getFavoriteBook());
    }

    @Test
    public void testSetFavoriteBook() {
        user.setFavoriteBook("test");
        assertEquals("test", user.getFavoriteBook());
    }

    @Test
    public void testGetFavoriteGenres() {
        List<String> favGenres = new LinkedList<>();
        favGenres.add("SF");

        assertEquals(favGenres, user.getFavoriteGenres());
    }

    @Test
    public void testSetFavoriteGenres() {
        List<String> favGenres = new LinkedList<>();
        favGenres.add("SF");
        favGenres.add("Horror");

        user.setFavoriteGenres(favGenres);

        assertEquals(favGenres, user.getFavoriteGenres());
    }

    @Test
    public void testAddFavoriteGenresFirst() {
        User u = new User();

        u.addFavoriteGenresItem("SF");

        List<String> favGenres = new LinkedList<>();
        favGenres.add("SF");

        assertEquals(favGenres, user.getFavoriteGenres());
    }

    @Test
    public void testAddFavoriteGenresMore() {
        user.addFavoriteGenresItem("Horror");

        List<String> favGenres = new LinkedList<>();
        favGenres.add("SF");
        favGenres.add("Horror");

        assertEquals(favGenres, user.getFavoriteGenres());
    }

    @Test
    public void testGetFollowers() {
        assertNull(user.getFollowers());
    }

    @Test
    public void testSetFollowers() {
        User f = new User();

        List<User> followers = new LinkedList<>();
        followers.add(f);

        user.setFollowers(followers);

        assertEquals(followers, user.getFollowers());
    }

    @Test
    public void testAddFirstFollower() {
        User f = new User();

        List<User> followers = new LinkedList<>();
        followers.add(f);

        user.addFollowersItem(f);

        assertEquals(followers, user.getFollowers());
    }

    @Test
    public void testAddMoreFollower() {
        User f = new User();

        List<User> followers = new LinkedList<>();
        followers.add(f);

        user.setFollowers(followers);

        User f2 = new User();

        List<User> ans = new LinkedList<>();

        ans.add(f);
        ans.add(f2);

        user.addFollowersItem(f2);

        assertEquals(ans, user.getFollowers());
    }

    @Test
    public void testGetFollowing() {
        assertNull(user.getFollowing());
    }

    @Test
    public void testSetFollowing() {
        User f = new User();

        List<User> following = new LinkedList<>();
        following.add(f);

        user.setFollowing(following);

        assertEquals(following, user.getFollowing());
    }

    @Test
    public void testAddFirstFollowing() {
        User f = new User();

        List<User> following = new LinkedList<>();
        following.add(f);

        user.addFollowingItem(f);

        assertEquals(following, user.getFollowing());
    }

    @Test
    public void testAddMoreFollowing() {
        User f = new User();

        List<User> following = new LinkedList<>();
        following.add(f);

        user.setFollowing(following);

        User f2 = new User();

        List<User> ans = new LinkedList<>();

        ans.add(f);
        ans.add(f2);

        user.addFollowingItem(f2);

        assertEquals(ans, user.getFollowing());
    }

    @Test
    public void testEqualsSameObject() {
        User u1 = new User();

        assertEquals(true, u1.equals(u1));
    }

    @Test
    public void testEqualsNull() {
        User u1 = new User();

        assertEquals(false, u1.equals(null));
    }

    @Test
    public void testEqualsEqual() {
        List<String> favGenres = new LinkedList<>();
        favGenres.add("SF");

        User u1 = new User("username", "Surname", "Name", "email", "password", User.UserRoleEnum.REGULAR, true, true, false, "bio", "location", "picture", "favoriteBook", favGenres, null, null);

        assertEquals(true, u1.equals(user));
    }

    @Test
    public void testEqualsNotEqual() {
        User u1 = new User("usernameeee", "Surname", "Name", "email", "password", User.UserRoleEnum.REGULAR, true, true, false, "bio", "location", "picture", "favoriteBook", null, null, null);

        assertEquals(false, u1.equals(user));
    }

    @Test
    public void testHash() {
        List<String> favGenres = new LinkedList<>();
        favGenres.add("SF");

        User u1 = new User("username", "Surname", "Name", "email", "password", User.UserRoleEnum.REGULAR, true, true, false, "bio", "location", "picture", "favoriteBook", favGenres, null, null);

        assertEquals(u1.hashCode(), user.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("class User {\n" +
            "    username: username\n" +
            "    firstName: Surname\n" +
            "    lastName: Name\n" +
            "    email: email\n" +
            "    password: password\n" +
            "    userRole: Regular\n" +
            "    isLoggedIn: true\n" +
            "    isActive: true\n" +
            "    isBanned: false\n" +
            "    bio: bio\n" +
            "    location: location\n" +
            "    profilePicture: picture\n" +
            "    favoriteBook: favoriteBook\n" +
            "    favoriteGenres: [SF]\n" +
            "    followers: null\n" +
            "    following: null\n" +
            "}", user.toString());
    }

    @Test
    public void testRoleEnum() {
        User.UserRoleEnum role = User.UserRoleEnum.AUTHOR;

        assertEquals("Author", role.getValue());

        assertEquals(User.UserRoleEnum.AUTHOR, User.UserRoleEnum.fromValue("Author"));
    }

    @Test
    public void testRoleEnumIllegalValue() {
        assertThrows(IllegalArgumentException.class, () -> User.UserRoleEnum.fromValue("king"));
    }
}
