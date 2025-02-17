package it.giocode.cv_managment.exception.exception_class;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String entity, String field, String value) {
        super(String.format("%s not found with %s '%s'", entity, field, value));
    }
}
