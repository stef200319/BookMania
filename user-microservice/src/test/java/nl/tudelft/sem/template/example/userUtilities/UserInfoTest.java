package nl.tudelft.sem.template.example.userUtilities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserInfoTest {
    @Test
    public void sameObjectEquals() {
        UserInfo u = new UserInfo("username", "fname", "lname", "email", "pass");
        assertEquals(true, u.equals(u));
    }

    @Test
    public void nullEquals() {
        UserInfo u = new UserInfo("username", "fname", "lname", "email", "pass");
        assertEquals(false, u.equals(null));
    }

    @Test
    public void goodEquals() {
        UserInfo u = new UserInfo("username", "fname", "lname", "email", "pass");
        UserInfo u1 = new UserInfo("username", "fname", "lname", "email", "pass");
        assertEquals(true, u.equals(u1));
    }
    @Test
    public void badEquals() {
        UserInfo u = new UserInfo("username", "fname", "lname", "email", "pass");
        UserInfo u1 = new UserInfo("usernamee", "fname", "lname", "email", "pass");
        assertEquals(false, u.equals(u1));
    }


    @Test
    public void hashTest() {
        UserInfo u = new UserInfo("username", "fname", "lname", "email", "pass");
        UserInfo u1 = new UserInfo("username", "fname", "lname", "email", "pass");
        assertEquals(u.hashCode(), u1.hashCode());
    }

    @Test
    public void badHashTest() {
        UserInfo u = new UserInfo("username", "fname", "lname", "email", "pass");
        UserInfo u1 = new UserInfo("usernamee", "fname", "lname", "email", "pass");
        assertNotEquals(u.hashCode(), u1.hashCode());
    }

    @Test
    public void toStringTest() {
        UserInfo u = new UserInfo("username", "fname", "lname", "email", "pass");
        assertEquals("UserInfo{username='username', firstName='fname', lastName='lname', email='email', password='pass'}", u.toString());
    }
}
