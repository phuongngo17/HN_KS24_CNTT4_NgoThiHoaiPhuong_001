package security;

public record ErrorResponse(

        int status,

        String code,

        String message

){

}