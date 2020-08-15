package com.rabo.customer.statement.exception;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.rabo.customer.statement.model.FailedResponse;
import com.rabo.customer.statement.util.ReportConstants;

import lombok.extern.slf4j.Slf4j;

/**
 * Exceptions are handled when statement report generation fails
 * 
 * @author Sivaraj
 *
 */
@ControllerAdvice
@Slf4j
public class ReportExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(MultipartException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ResponseEntity<FailedResponse> handleControllerException(HttpServletRequest request, Throwable ex) {
		log.error("MultipartException occured..", ex);
		return buildErrorMessage(ex, request, HttpStatus.BAD_REQUEST, null);
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	public ResponseEntity<FailedResponse> handleMaxSizeException(HttpServletRequest request, Throwable ex) {
		log.error("MultipartException occured..", ex);
		return buildErrorMessage(ex, request, HttpStatus.BAD_REQUEST, ReportConstants.MAX_FILE_SIZE_EXCEEDED);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public final ResponseEntity<FailedResponse> handleAllExceptions(HttpServletRequest request, Throwable ex) {
		log.error("Exception occured..", ex);
		return buildErrorMessage(ex, request, HttpStatus.INTERNAL_SERVER_ERROR, ReportConstants.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public final ResponseEntity<FailedResponse> handleRuntimeExceptions(HttpServletRequest request, Throwable ex) {
		log.error("RuntimeException occured..", ex);
		return buildErrorMessage(ex, request, HttpStatus.INTERNAL_SERVER_ERROR, ReportConstants.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ApplicationException.class)
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public final ResponseEntity<FailedResponse> handleApplicationExceptions(HttpServletRequest request, Throwable ex) {
		log.error("ApplicationException occured..", ex);
		return buildErrorMessage(ex, request, HttpStatus.INTERNAL_SERVER_ERROR, null);
	}

	private ResponseEntity<FailedResponse> buildErrorMessage(Throwable ex, HttpServletRequest request,
			HttpStatus status, String errorMessage) {
		FailedResponse error = FailedResponse.builder()
				.errorMessage(Objects.isNull(errorMessage) ? ex.getMessage() : errorMessage).errorMessageType("error")
				.success(false).statusCode(String.valueOf(status.value())).build();
		return new ResponseEntity<FailedResponse>(error, status);
	}

}
