package nl.tudelft.sem.template.example.services;

import nl.tudelft.sem.template.example.database.UserInfoRepository;
import nl.tudelft.sem.template.example.database.UserProfileRepository;
import nl.tudelft.sem.template.example.userUtilities.UserInfo;
import nl.tudelft.sem.template.example.userUtilities.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.junit.jupiter.api.*;

import java.util.LinkedList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserProfileServiceTest {
    private UserProfileRepository repo;
    private UserProfileService profileService;

    @BeforeEach
    void setUp() {
        repo = Mockito.mock(UserProfileRepository.class);
        profileService = new UserProfileService(repo);
    }

    @Test
    public void createTest() {
        UserProfile ui = new UserProfile();
        ui.setUsername("a");

        UserProfile res = profileService.createUserProfile(ui);

        assertEquals(ui, res);
        Mockito.verify(repo, Mockito.times(1)).saveAndFlush(ui);
    }

    @Test
    public void editTest() {
        UserProfile ui = new UserProfile("a", "bio", "loc", "profile", "book", new LinkedList<>());


        UserProfile old = new UserProfile();
        old.setUsername("a");

        Mockito.when(repo.findById("a")).thenReturn(Optional.of(old));

        UserProfile res = profileService.editUserProfile(ui);

        assertEquals(ui, res);
        Mockito.verify(repo, Mockito.times(1)).saveAndFlush(ui);
    }

    @Test
    public void getTest() {
        UserProfile ui = new UserProfile();
        ui.setUsername("a");

        Mockito.when(repo.findById("a")).thenReturn(Optional.of(ui));

        UserProfile res = profileService.getUserProfile(ui.getUsername());

        assertEquals(ui, res);
    }

    @Test
    public void deleteTest() {
        UserProfile ui = new UserProfile();
        ui.setUsername("a");

        Mockito.when(repo.findById("a")).thenReturn(Optional.of(ui));

        UserProfile res = profileService.deleteUserProfile(ui.getUsername());

        assertEquals(ui, res);
        Mockito.verify(repo, Mockito.times(1)).deleteById(ui.getUsername());
    }
}

