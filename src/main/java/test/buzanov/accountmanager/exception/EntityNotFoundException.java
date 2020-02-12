package test.buzanov.accountmanager.exception;

public class EntityNotFoundException extends Exception {
    public EntityNotFoundException(String entity){
        super(entity+" не найден.");
    }
}
