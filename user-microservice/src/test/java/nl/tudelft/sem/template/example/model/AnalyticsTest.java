package nl.tudelft.sem.template.example.model;

import nl.tudelft.sem.template.example.model.Analytics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AnalyticsTest {

    private Analytics a1;
    private Analytics a2;
    @BeforeEach
    void setUp() {
        a1 = new Analytics().userUsername("u1").commentsNumber(6L).followersNumber(10L).followingNumber(16L).lastLoginDate("2023-01-01").reviewsNumber(10L);
        a2 = new Analytics().userUsername("u2").commentsNumber(10L).followersNumber(5L).followingNumber(200L).lastLoginDate("2024-01-01").reviewsNumber(0L);
    }

    @Test
    void equalityTest() {
        assertEquals(a1, a1);
    }

    @Test
    void ineqTest() {
        assertNotEquals(a1, a2);
    }

    @Test
    void constructorTestOne() {
        Analytics ca1 = new Analytics("u1", "now");
        Analytics ca2 = new Analytics("u2", "now");
        assertNotEquals(ca1, ca2);
    }

    @Test
    void constructorTestTwo() {
        Analytics ca1 = new Analytics("u1", "now");
        assertEquals(ca1, ca1);
    }

    @Test
    void usernameGetterTest() {
        assertEquals("u1", a1.getUserUsername());
    }

    @Test
    void commentsNumberGetterTest() {
        assertEquals(6L, a1.getCommentsNumber());
    }

    @Test
    void lastLastLoginDateGetterTest() {
        assertEquals("2023-01-01", a1.getLastLoginDate());
    }

    @Test
    void followersNumberGetterTest() {
        assertEquals(10L, a1.getFollowersNumber());
    }

    @Test
    void followingNumberGetterTest() {
        assertEquals(16L, a1.getFollowingNumber());
    }

    @Test
    void reviewsNumberGetterTest() {
        assertEquals(10L, a1.getReviewsNumber());
    }

    @Test
    void usernameSetterTest() {
        assertEquals("u1", a1.getUserUsername());
        a1.setUserUsername("u3");
        assertEquals("u3", a1.getUserUsername());
    }

    @Test
    void commentsNumberSetterTest() {
        assertEquals(6L, a1.getCommentsNumber());
        a1.setCommentsNumber(5L);
        assertEquals(5L, a1.getCommentsNumber());
    }

    @Test
    void lastLastLoginDateSetterTest() {
        assertEquals("2023-01-01", a1.getLastLoginDate());
        a1.setLastLoginDate("2023-01-02");
        assertEquals("2023-01-02", a1.getLastLoginDate());
    }

    @Test
    void followersNumberSetterTest() {
        assertEquals(10L, a1.getFollowersNumber());
        a1.setFollowersNumber(20L);
        assertEquals(20L, a1.getFollowersNumber());
    }

    @Test
    void followingNumberSetterTest() {
        assertEquals(16L, a1.getFollowingNumber());
        a1.setFollowingNumber(1L);
        assertEquals(1L, a1.getFollowingNumber());
    }

    @Test
    void reviewsNumberSetterTest() {
        assertEquals(10L, a1.getReviewsNumber());
        a1.setReviewsNumber(0L);
        assertEquals(0L, a1.getReviewsNumber());
    }

    @Test
    void equalsTest() {
        assertEquals(true, a1.equals(a1));
    }

    @Test
    void notEqualsTest() {
        assertEquals(false, a1.equals(a2));
    }

    @Test
    void hashTest() {
        assertEquals(-439839698, a1.hashCode());
    }

    @Test
    void toStringTest() {
        assertEquals("class Analytics {\n" +
                "    userUsername: u1\n" +
                "    reviewsNumber: 10\n" +
                "    commentsNumber: 6\n" +
                "    lastLoginDate: 2023-01-01\n" +
                "    followersNumber: 10\n" +
                "    followingNumber: 16\n" +
                "}", a1.toString());
    }

    @Test
    void toIndentedStringTest() {
        assertEquals("class Analytics {\n" +
                "        userUsername: u1\n" +
                "        reviewsNumber: 10\n" +
                "        commentsNumber: 6\n" +
                "        lastLoginDate: 2023-01-01\n" +
                "        followersNumber: 10\n" +
                "        followingNumber: 16\n" +
                "    }", a1.toIndentedString(a1));
    }

    @Test
    void emptyAnalyticsTest() {
        Analytics a3 = new Analytics();
        assertNotNull(a3);
    }
}
