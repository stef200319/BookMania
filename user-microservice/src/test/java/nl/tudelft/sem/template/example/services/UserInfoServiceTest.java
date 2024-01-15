package nl.tudelft.sem.template.example.services;

import nl.tudelft.sem.template.example.database.UserInfoRepository;
import nl.tudelft.sem.template.example.userUtilities.UserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.junit.jupiter.api.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserInfoServiceTest {
    private UserInfoRepository repo;
    private UserInfoService infoService;

    @BeforeEach
    void setUp() {
        repo = Mockito.mock(UserInfoRepository.class);
        infoService = new UserInfoService(repo);
    }

    @Test
    public void createTest() {
        UserInfo ui = new UserInfo();
        ui.setUsername("a");

        UserInfo res = infoService.createUserInfo(ui);

        assertEquals(ui, res);
        Mockito.verify(repo, Mockito.times(1)).saveAndFlush(ui);
    }

    @Test
    public void editTest() {
        UserInfo ui = new UserInfo("a", "fname", "lname", "email", "pass");

        UserInfo old = new UserInfo();
        old.setUsername("a");

        Mockito.when(repo.findById("a")).thenReturn(Optional.of(old));

        UserInfo res = infoService.editUserInfo(ui);

        assertEquals(ui, res);
        Mockito.verify(repo, Mockito.times(1)).saveAndFlush(ui);
    }

    @Test
    public void getTest() {
        UserInfo ui = new UserInfo();
        ui.setUsername("a");

        Mockito.when(repo.findById("a")).thenReturn(Optional.of(ui));

        UserInfo res = infoService.getUserInfo(ui.getUsername());

        assertEquals(ui, res);
    }

    @Test
    public void deleteTest() {
        UserInfo ui = new UserInfo();
        ui.setUsername("a");

        Mockito.when(repo.findById("a")).thenReturn(Optional.of(ui));

        UserInfo res = infoService.deleteUserInfo(ui.getUsername());

        assertEquals(ui, res);
        Mockito.verify(repo, Mockito.times(1)).deleteById(ui.getUsername());
    }
}
