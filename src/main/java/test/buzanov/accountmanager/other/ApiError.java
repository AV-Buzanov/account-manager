package test.buzanov.accountmanager.other;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

/**
 * Объект для удобного представления возникающих на сервере исключений для клиента.
 * @author Aleksey Buzanov
 */

@Data
public class ApiError {

    private HttpStatus status;

    private String message;

    private List<String> errors;

    public ApiError(HttpStatus status, String message, List<String> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ApiError(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        errors = Arrays.asList(error);
    }
}
