package nl.tudelft.sem.template.example.exceptions;

/*
    This username doesn't exist in the database, contains non-alfanumeric characters.
 */
public class InvalidUsernameException extends Exception {
    public InvalidUsernameException(String message) {
        super(message);
    }
}
