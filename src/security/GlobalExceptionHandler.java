package security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * Xử lý JWT hết hạn
     *
     * Lưu ý:
     * Exception này chỉ bắt được
     * nếu JWT exception xảy ra trong Controller/Service
     */
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> handleExpiredJwt(
            ExpiredJwtException ex
    ){

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        new ErrorResponse(
                                401,
                                "JWT_EXPIRED",
                                "Token has expired"
                        )
                );
    }




    /**
     * JWT lỗi format,
     * signature sai,
     * token corrupt
     */
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<?> handleJwtException(
            JwtException ex
    ){

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(
                        new ErrorResponse(
                                401,
                                "JWT_INVALID",
                                "Invalid JWT token"
                        )
                );
    }



    /**
     * Exception chung
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(
            Exception ex
    ){

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        new ErrorResponse(
                                500,
                                "SERVER_ERROR",
                                ex.getMessage()
                        )
                );
    }



}