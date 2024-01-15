package nl.tudelft.sem.template.example.services;

import nl.tudelft.sem.template.example.database.UserInfoRepository;
import nl.tudelft.sem.template.example.database.UserStatusRepository;
import nl.tudelft.sem.template.example.model.User;
import nl.tudelft.sem.template.example.userUtilities.UserInfo;
import nl.tudelft.sem.template.example.userUtilities.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.junit.jupiter.api.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserStatusServiceTest {
    private UserStatusRepository repo;
    private UserStatusService statusService;

    @BeforeEach
    void setUp() {
        repo = Mockito.mock(UserStatusRepository.class);
        statusService = new UserStatusService(repo);
    }

    @Test
    public void createTest() {
        UserStatus ui = new UserStatus();
        ui.setUsername("a");

        UserStatus res = statusService.createUserStatus(ui);

        assertEquals(ui, res);
        Mockito.verify(repo, Mockito.times(1)).saveAndFlush(ui);
    }

    @Test
    public void editTest() {
        UserStatus ui = new UserStatus("a", true, true, false, User.UserRoleEnum.REGULAR);

        UserStatus old = new UserStatus();
        old.setUsername("a");

        Mockito.when(repo.findById("a")).thenReturn(Optional.of(old));

        UserStatus res = statusService.editUserStatus(ui);

        assertEquals(ui, res);
        Mockito.verify(repo, Mockito.times(1)).saveAndFlush(ui);
    }

    @Test
    public void getTest() {
        UserStatus ui = new UserStatus();
        ui.setUsername("a");

        Mockito.when(repo.findById("a")).thenReturn(Optional.of(ui));

        UserStatus res = statusService.getUserStatus(ui.getUsername());

        assertEquals(ui, res);
    }

    @Test
    public void deleteTest() {
        UserStatus ui = new UserStatus();
        ui.setUsername("a");

        Mockito.when(repo.findById("a")).thenReturn(Optional.of(ui));

        UserStatus res = statusService.deleteUserStatus(ui.getUsername());

        assertEquals(ui, res);
        Mockito.verify(repo, Mockito.times(1)).deleteById(ui.getUsername());
    }
}

