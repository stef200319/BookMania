package nl.tudelft.sem.template.example.exceptions;

/*
    User doesn't exist, isn't logged in, is banned, is inactive, is already logged in.
 */
public class InvalidUserException extends Exception {
    public InvalidUserException(String message) {
        super(message);
    }
}
