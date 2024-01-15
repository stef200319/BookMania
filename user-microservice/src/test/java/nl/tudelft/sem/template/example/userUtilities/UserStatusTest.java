package nl.tudelft.sem.template.example.userUtilities;

import nl.tudelft.sem.template.example.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserStatusTest {
    @Test
    public void sameObjectEquals() {
        UserStatus u = new UserStatus("username", true, true, false, User.UserRoleEnum.REGULAR);
        assertEquals(true, u.equals(u));
    }

    @Test
    public void nullEquals() {
        UserStatus u = new UserStatus("username", true, true, false, User.UserRoleEnum.REGULAR);
        assertEquals(false, u.equals(null));
    }

    @Test
    public void goodEquals() {
        UserStatus u = new UserStatus("username", true, true, false, User.UserRoleEnum.REGULAR);
        UserStatus u1 = new UserStatus("username", true, true, false, User.UserRoleEnum.REGULAR);
        assertEquals(true, u.equals(u1));
    }
    @Test
    public void badEquals() {
        UserStatus u = new UserStatus("username", true, true, false, User.UserRoleEnum.REGULAR);
        UserStatus u1 = new UserStatus("usernamee", true, true, false, User.UserRoleEnum.REGULAR);
        assertEquals(false, u.equals(u1));
    }


    @Test
    public void hashTest() {
        UserStatus u = new UserStatus("username", true, true, false, User.UserRoleEnum.REGULAR);
        UserStatus u1 = new UserStatus("username", true, true, false, User.UserRoleEnum.REGULAR);
        assertEquals(u.hashCode(), u1.hashCode());
    }

    @Test
    public void badHashTest() {
        UserStatus u = new UserStatus("username", true, true, false, User.UserRoleEnum.REGULAR);
        UserStatus u1 = new UserStatus("usernamee", true, true, false, User.UserRoleEnum.REGULAR);
        assertNotEquals(u.hashCode(), u1.hashCode());
    }

    @Test
    public void toStringTest() {
        UserStatus u = new UserStatus("username", true, true, false, User.UserRoleEnum.REGULAR);
        assertEquals("UserStatus{username='username', isActive=true, isLoggedIn=true, isBanned=false, userRole=Regular}", u.toString());
    }
}

