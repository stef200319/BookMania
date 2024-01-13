package nl.tudelft.sem.template.example.userHandlers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import nl.tudelft.sem.template.example.database.UserRepository;
import nl.tudelft.sem.template.example.exceptions.InvalidEmailException;
import nl.tudelft.sem.template.example.exceptions.InvalidUserException;
import nl.tudelft.sem.template.example.exceptions.InvalidUsernameException;
import nl.tudelft.sem.template.example.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserIsFollowedValidatorTest {
    private UserIsFollowedValidator userIsFollowedValidator;
    private UserRepository userRepositoryMock;
    private User followedUserMock;

    @BeforeEach
    public void setUp() {
        userRepositoryMock = mock(UserRepository.class);
        followedUserMock = mock(User.class);
        userIsFollowedValidator = new UserIsFollowedValidator(userRepositoryMock, followedUserMock);
    }

    @Test
    public void testHandleUserFollowsFollowedUser() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        User user = mock(User.class);
        List<User> followingList = new ArrayList<>();
        followingList.add(followedUserMock);

        when(user.getFollowing()).thenReturn(followingList);
        List<User> fols = new ArrayList<>();
        fols.add(user);
        when(followedUserMock.getFollowers()).thenReturn(fols);

        assertTrue(userIsFollowedValidator.handle(user));
        verify(userRepositoryMock, times(0)).save(any(User.class));
    }

    @Test
    public void testHandleUserDoesNotFollowFollowedUser() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        User user = mock(User.class);
        List<User> followingList = new ArrayList<>();

        when(user.getFollowing()).thenReturn(followingList);
        when(followedUserMock.getFollowers()).thenReturn(new ArrayList<>());

        assertThrows(InvalidUserException.class, () -> userIsFollowedValidator.handle(user));
        verify(userRepositoryMock, times(0)).save(any(User.class));
    }

    @Test
    public void testHandleFollowedUserDoesNotHaveFollowers() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        User user = mock(User.class);
        List<User> followingList = new ArrayList<>();
        followingList.add(followedUserMock);

        when(user.getFollowing()).thenReturn(followingList);
        when(followedUserMock.getFollowers()).thenReturn(null);

        assertThrows(InvalidUserException.class, () -> userIsFollowedValidator.handle(user));
        verify(userRepositoryMock, times(0)).save(any(User.class));
    }

    @Test
    public void testHandleFollowedUserDoesNotHaveUserAsFollower() throws InvalidUsernameException, InvalidUserException, InvalidEmailException {
        User user = mock(User.class);
        List<User> followingList = new ArrayList<>();
        followingList.add(followedUserMock);

        when(user.getFollowing()).thenReturn(followingList);
        when(followedUserMock.getFollowers()).thenReturn(new ArrayList<>());

        assertThrows(InvalidUserException.class, () -> userIsFollowedValidator.handle(user));
        verify(userRepositoryMock, times(0)).save(any(User.class));
    }
}
