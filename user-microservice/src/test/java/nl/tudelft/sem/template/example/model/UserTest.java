package nl.tudelft.sem.template.example.model;

import nl.tudelft.sem.template.example.userUtilities.UserInfo;
import nl.tudelft.sem.template.example.userUtilities.UserProfile;
import nl.tudelft.sem.template.example.userUtilities.UserStatus;
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
        UserStatus status = new UserStatus("username", true, true, false, User.UserRoleEnum.REGULAR);
        UserProfile profile = new UserProfile("username","bio", "location", "picture", "favoriteBook", favGenres);
        UserInfo info = new UserInfo("username", "Surname", "Name", "email", "password");
        user = new User("username", info, status, profile, null, null);
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
        assertEquals("Surname", user.getUserInfo().getFirstName());
    }

    @Test
    public void testSetFirstName() {
        user.getUserInfo().setFirstName("test");
        assertEquals("test", user.getUserInfo().getFirstName());
    }

    @Test
    public void testGetLastName() {
        assertEquals("Name", user.getUserInfo().getLastName());
    }

    @Test
    public void testSetLastName() {
        user.getUserInfo().setLastName("test");
        assertEquals("test", user.getUserInfo().getLastName());
    }

    @Test
    public void testGetEmail() {
        assertEquals("email", user.getUserInfo().getEmail());
    }

    @Test
    public void testSetEmail() {
        user.getUserInfo().setEmail("test");
        assertEquals("test", user.getUserInfo().getEmail());
    }

    @Test
    public void testGetPassword() {
        assertEquals("password", user.getUserInfo().getPassword());
    }

    @Test
    public void testSetPassword() {
        user.getUserInfo().setPassword("test");
        assertEquals("test", user.getUserInfo().getPassword());
    }

    @Test
    public void testGetRole() {
        assertEquals(User.UserRoleEnum.REGULAR, user.getUserStatus().getUserRole());
    }

    @Test
    public void testSetRole() {
        user.getUserStatus().setUserRole(User.UserRoleEnum.AUTHOR);
        assertEquals(User.UserRoleEnum.AUTHOR, user.getUserStatus().getUserRole());
    }

    @Test
    public void testGetLoggedIn() {
        assertEquals(true, user.getUserStatus().getIsLoggedIn());
    }

    @Test
    public void testSetLoggedIn() {
        user.getUserStatus().setIsLoggedIn(false);
        assertEquals(false, user.getUserStatus().getIsLoggedIn());
    }

    @Test
    public void testGetActive() {
        assertEquals(true, user.getUserStatus().getIsActive());
    }

    @Test
    public void testSetActive() {
        user.getUserStatus().setIsActive(false);
        assertEquals(false, user.getUserStatus().getIsActive());
    }

    @Test
    public void testGetBanned() {
        assertEquals(false, user.getUserStatus().getIsBanned());
    }

    @Test
    public void testSetBanned() {
        user.getUserStatus().setIsBanned(true);
        assertEquals(true, user.getUserStatus().getIsBanned());
    }

    @Test
    public void testGetBio() {
        assertEquals("bio", user.getUserProfile().getBio());
    }

    @Test
    public void testSetBio() {
        user.getUserProfile().setBio("test");
        assertEquals("test", user.getUserProfile().getBio());
    }

    @Test
    public void testGetLocation() {
        assertEquals("location", user.getUserProfile().getLocation());
    }

    @Test
    public void testSetLocation() {
        user.getUserProfile().setLocation("test");
        assertEquals("test", user.getUserProfile().getLocation());
    }

    @Test
    public void testGetProfilePicture() {
        assertEquals("picture", user.getUserProfile().getProfilePicture());
    }

    @Test
    public void testSetProfilePicture() {
        user.getUserProfile().setProfilePicture("test");
        assertEquals("test", user.getUserProfile().getProfilePicture());
    }

    @Test
    public void testGetFavoriteBook() {
        assertEquals("favoriteBook", user.getUserProfile().getFavoriteBook());
    }

    @Test
    public void testSetFavoriteBook() {
        user.getUserProfile().setFavoriteBook("test");
        assertEquals("test", user.getUserProfile().getFavoriteBook());
    }

    @Test
    public void testGetFavoriteGenres() {
        List<String> favGenres = new LinkedList<>();
        favGenres.add("SF");

        assertEquals(favGenres, user.getUserProfile().getFavoriteGenres());
    }

    @Test
    public void testSetFavoriteGenres() {
        List<String> favGenres = new LinkedList<>();
        favGenres.add("SF");
        favGenres.add("Horror");

        user.getUserProfile().setFavoriteGenres(favGenres);

        assertEquals(favGenres, user.getUserProfile().getFavoriteGenres());
    }

    @Test
    public void testGetFollowers() {
        assertNull(user.getFollowers());
    }

    @Test
    public void testSetFollowers() {
        User f = new User();
        f.setUsername("a");

        List<String> followers = new LinkedList<>();
        followers.add(f.getUsername());

        user.setFollowers(followers);

        assertEquals(followers, user.getFollowers());
    }

    @Test
    public void testAddFirstFollower() {
        User f = new User();
        f.setUsername("a");

        List<String> followers = new LinkedList<>();
        followers.add(f.getUsername());

        user.addFollowersItem(f);

        assertEquals(followers, user.getFollowers());
    }

    @Test
    public void testAddMoreFollower() {
        User f = new User();
        f.setUsername("a");

        List<String> followers = new LinkedList<>();
        followers.add(f.getUsername());

        user.setFollowers(followers);

        User f2 = new User();
        f2.setUsername("b");

        List<String> ans = new LinkedList<>();

        ans.add(f.getUsername());
        ans.add(f2.getUsername());

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
        f.setUsername("a");

        List<String> following = new LinkedList<>();
        following.add(f.getUsername());

        user.setFollowing(following);

        assertEquals(following, user.getFollowing());
    }

    @Test
    public void testAddFirstFollowing() {
        User f = new User();
        f.setUsername("a");

        List<String> following = new LinkedList<>();
        following.add(f.getUsername());

        user.addFollowingItem(f);

        assertEquals(following, user.getFollowing());
    }

    @Test
    public void testAddMoreFollowing() {
        User f = new User();
        f.setUsername("a");

        List<String> following = new LinkedList<>();
        following.add(f.getUsername());

        user.setFollowing(following);

        User f2 = new User();

        List<String> ans = new LinkedList<>();

        ans.add(f.getUsername());
        ans.add(f2.getUsername());

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

        UserStatus status1 = new UserStatus("username", true, true, false, User.UserRoleEnum.REGULAR);
        UserProfile profile1 = new UserProfile("username","bio", "location", "picture", "favoriteBook", favGenres);
        UserInfo info1 = new UserInfo("username", "Surname", "Name", "email", "password");
        User user1 = new User("username", info1, status1, profile1, null, null);

        //User u1 = new User("username", "Surname", "Name", "email", "password", User.UserRoleEnum.REGULAR, true, true, false, "bio", "location", "picture", "favoriteBook", favGenres, null, null);

        assertEquals(true, user1.equals(user));
    }

    @Test
    public void testEqualsNotEqual() {
        List<String> favGenres = new LinkedList<>();
        favGenres.add("SF");

        UserStatus status1 = new UserStatus("username", true, true, false, User.UserRoleEnum.REGULAR);
        UserProfile profile1 = new UserProfile("username","bio", "location", "picture", "favoriteBook", favGenres);
        UserInfo info1 = new UserInfo("username", "Surname", "Name", "email", "password");
        User user1 = new User("username1", info1, status1, profile1, null, null);
        assertEquals(false, user1.equals(user));
    }

    @Test
    public void testHash() {
        List<String> favGenres = new LinkedList<>();
        favGenres.add("SF");

        //User u1 = new User("username", "Surname", "Name", "email", "password", User.UserRoleEnum.REGULAR, true, true, false, "bio", "location", "picture", "favoriteBook", favGenres, null, null);

        UserStatus status1 = new UserStatus("username", true, true, false, User.UserRoleEnum.REGULAR);
        UserProfile profile1 = new UserProfile("username","bio", "location", "picture", "favoriteBook", favGenres);
        UserInfo info1 = new UserInfo("username", "Surname", "Name", "email", "password");
        User user1 = new User("username", info1, status1, profile1, null, null);


        assertEquals(user1.hashCode(), user.hashCode());
    }

    @Test
    public void testToString() {
        //assertEquals("User{username='username', firstName='Surname', lastName='Name', email='email', password='password', userStatus=UserStatus{username='username', isActive=true, isLoggedIn=true, isBanned=false, userRole=Regular}, userProfile=UserProfile{username='username', bio='bio', location='location', profilePicture='picture', favoriteBook='favoriteBook', favoriteGenres=[SF]}, followers=null, following=null}> but was: <User{username='username', firstName='Surname', lastName='Name', email='email', password='password', userStatus=UserStatus{username='username', isActive=true, isLoggedIn=true, isBanned=false, userRole=Regular}, userProfile=UserProfile{username='username', bio='bio', location='location', profilePicture='picture', favoriteBook='favoriteBook', favoriteGenres=[SF]}, followers=null, following=null}", user.toString());
        assertEquals("User{username='username', userInfo=UserInfo{username='username', firstName='Surname', lastName='Name', email='email', password='password'}, userStatus=UserStatus{username='username', isActive=true, isLoggedIn=true, isBanned=false, userRole=Regular}, userProfile=UserProfile{username='username', bio='bio', location='location', profilePicture='picture', favoriteBook='favoriteBook', favoriteGenres=[SF]}, followers=null, following=null}", user.toString());
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
