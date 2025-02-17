package it.giocode.cv_managment.exception.exception_class;

public class AlreadyExistsException extends RuntimeException {

    public AlreadyExistsException(String field, String value) {
        super(String.format("%s '%s' already exists. Please insert a valid value", field, value));
    }
}
