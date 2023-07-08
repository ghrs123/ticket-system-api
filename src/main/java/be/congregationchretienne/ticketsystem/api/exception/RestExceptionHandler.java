package be.congregationchretienne.ticketsystem.api.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import be.congregationchretienne.ticketsystem.api.error.Problem;
import be.congregationchretienne.ticketsystem.api.error.ProblemDetail;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

public class RestExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Object> handleNotFoundException(
      NotFoundException exception, WebRequest request) {

    var problem =
        new Problem.ProblemBuilder()
            .eventId(exception.getEventId())
            .title("Resource not found.")
            .message(exception.getMessage())
            .status(NOT_FOUND)
            .build();

    return new ResponseEntity<>(problem, NOT_FOUND);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Problem> handleIllegalArgumentException(
      IllegalArgumentException exception, WebRequest request) {

    var problemDetail = new ProblemDetail.Builder().reason(exception.getMessage()).build();

    var problem =
        new Problem.ProblemBuilder()
            .eventId(exception.getEventId())
            .title("Resource not found.")
            .message(exception.getMessage())
            .status(NOT_FOUND)
            .build();

    return new ResponseEntity<>(problem, BAD_REQUEST);
  }

  // ========== 400
  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<Problem> handleBadRequestException(
      BadRequestException exception, WebRequest request) {

    var problemDetail = new ProblemDetail.Builder().reason(exception.getMessage()).build();

    var problem =
        new Problem.ProblemBuilder()
            .eventId(exception.getEventId())
            .title("Resource not found.")
            .message(exception.getMessage())
            .status(BAD_REQUEST)
            .build();

    return new ResponseEntity<>(problem, BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    return super.handleHttpRequestMethodNotSupported(ex, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
      HttpMediaTypeNotSupportedException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {

    return super.handleHttpMediaTypeNotSupported(ex, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
      HttpMediaTypeNotAcceptableException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {

    return super.handleHttpMediaTypeNotAcceptable(ex, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleMissingPathVariable(
      MissingPathVariableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

    return super.handleMissingPathVariable(ex, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      MissingServletRequestParameterException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {

    return super.handleMissingServletRequestParameter(ex, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleServletRequestBindingException(
      ServletRequestBindingException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {

    return super.handleServletRequestBindingException(ex, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleConversionNotSupported(
      ConversionNotSupportedException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    return super.handleConversionNotSupported(ex, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleTypeMismatch(
      TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    return super.handleTypeMismatch(ex, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    return super.handleHttpMessageNotReadable(ex, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotWritable(
      HttpMessageNotWritableException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    return super.handleHttpMessageNotWritable(ex, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    return super.handleMethodArgumentNotValid(ex, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestPart(
      MissingServletRequestPartException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {
    return super.handleMissingServletRequestPart(ex, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleBindException(
      BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    return super.handleBindException(ex, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleNoHandlerFoundException(
      NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    return super.handleNoHandlerFoundException(ex, headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleAsyncRequestTimeoutException(
      AsyncRequestTimeoutException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest webRequest) {
    return super.handleAsyncRequestTimeoutException(ex, headers, status, webRequest);
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
    return super.handleExceptionInternal(ex, body, headers, status, request);
  }
}
