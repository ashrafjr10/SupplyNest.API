package Supplynest.Auth.Service.exceptions;

import SupplyNest.Common.dtos.CommonResponse;
import Supplynest.Auth.Service.constants.AppConstants;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Handle custom ServiceException
    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<CommonResponse> handleServiceException(ServiceException ex, WebRequest request) {
        CommonResponse error = new CommonResponse(
                AppConstants.STATUS_BAD_REQUEST,
                ex.getMessage()
        );
        logger.error("ServiceException | {} | {}", request.getDescription(false), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Handle custom access exceptions
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<CommonResponse> handleAccessDeniedException(AccessDeniedException ex) {
        CommonResponse error = new CommonResponse(
                AppConstants.STATUS_FORBIDDEN,
                !ex.getMessage().isEmpty() ? ex.getMessage() : AppConstants.MESSAGE_ACCESS_DENIED , null
        );
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<CommonResponse> handleAuthorizationException(AuthorizationDeniedException ex) {
        CommonResponse error = new CommonResponse(
                AppConstants.STATUS_UNAUTHORIZED,
                AppConstants.MESSAGE_UNAUTHORIZED, null
        );
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String cause = ex.getMessage();
        if (cause != null && (cause.contains("CrudPermissionScopesForSchool") || cause.contains("CrudPermissionScopes"))) {
            return ResponseEntity.badRequest().body(new CommonResponse(AppConstants.STATUS_BAD_REQUEST, "Please provide a valid CRUD permission scope", null));
        }

        if (cause != null && (cause.contains("CrudOperation"))) {
            return ResponseEntity.badRequest().body(new CommonResponse(AppConstants.STATUS_BAD_REQUEST, "Please provide valid CRUD operations", null));
        }

        if (cause != null && (cause.contains("TitlesForSchool") || cause.contains("Titles"))) {
            return ResponseEntity.badRequest().body(new CommonResponse(AppConstants.STATUS_BAD_REQUEST, "Invalid role title provided", null));
        }

        return ResponseEntity.badRequest().body(new CommonResponse(AppConstants.STATUS_BAD_REQUEST, "Invalid request payload", null)
        );
    }

    // Handle validation errors (@Valid)
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Validation failed");

        CommonResponse error = new CommonResponse(
                AppConstants.STATUS_BAD_REQUEST,
                errorMessage, null
        );

        return new ResponseEntity<>(error, status);
    }


    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse> handleAllExceptions(
            Exception ex,
            WebRequest request) {

        Throwable root = ex;
        while (root.getCause() != null) {
            root = root.getCause();
        }

        logger.error("ROOT CAUSE: {}", root.getMessage(), ex);

        CommonResponse error = new CommonResponse(
                AppConstants.STATUS_INTERNAL_SERVER_ERROR,
                root.getMessage(),
                null
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
