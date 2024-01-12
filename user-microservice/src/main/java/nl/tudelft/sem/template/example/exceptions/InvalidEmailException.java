package nl.tudelft.sem.template.example.exceptions;

public class InvalidEmailException extends Exception{
    public InvalidEmailException(String message) {
        super(message);
    }
}
