package it.giocode.cv_managment.exception.exception_class;

public class IncorrectPasswordsException extends RuntimeException {

    public IncorrectPasswordsException() {
        super("Incorrect password. Please try again");
    }
}
