package gguip1.community.global.exception;

import gguip1.community.global.response.ApiResponse;
import gguip1.community.global.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.naming.SizeLimitExceededException;
import java.net.BindException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<ApiResponse<Void>> handleErrorException(ErrorException e){
        ErrorCode errorCode = e.getErrorCode();
        HttpStatus status = errorCode.getStatus();

        if (status.is5xxServerError()) {
            log.error("Server error: {} ({})", errorCode.getMessage(), errorCode.getCode(), e);
        } else {
            // 4xx 는 한 줄만 (스택트레이스 X)
            log.info("Client error: {} ({})", errorCode.getMessage(), errorCode.getCode());
        }

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.error(errorCode.getCode(), new ErrorResponse(errorCode.getMessage())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                "유효하지 않은 입력 값입니다."
        );

        log.info("Validation error: {}", e.getMessage());

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("validation_error", errorResponse));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<Void>> handleMaxSizeException(MaxUploadSizeExceededException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                "파일 크기가 최대 허용 크기(5MB)를 초과했습니다. 5MB 이하의 파일만 업로드할 수 있습니다."
        );

        log.info("File size exceeded: {}", e.getMessage());

        return ResponseEntity
                .status(413)
                .body(ApiResponse.error("file_size_exceeded", errorResponse));
    }

    @ExceptionHandler(SizeLimitExceededException.class)
    public ResponseEntity<ApiResponse<Void>> handleSizeLimitExceededException(SizeLimitExceededException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                "요청 전체 크기가 최대 허용 크기(25MB)를 초과했습니다. 여러 파일의 총 용량을 25MB 이하로 줄여주세요."
        );

        log.info("Request size exceeded: {}", e.getMessage());

        return ResponseEntity
                .status(413)
                .body(ApiResponse.error("request_size_exceeded", errorResponse));
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<ApiResponse<Void>> handleFileSizeLimitExceededException(FileSizeLimitExceededException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                "파일 크기가 최대 허용 크기(5MB)를 초과했습니다. 5MB 이하의 파일만 업로드할 수 있습니다."
        );

        log.info("File size exceeded (apache): {}", e.getMessage());

        return ResponseEntity
                .status(413)
                .body(ApiResponse.error("file_size_exceeded", errorResponse));
    }
}
