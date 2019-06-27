package cd.go.authorization.cognitomfasinglestep.exception;

public class InvalidCognitoUserCredentialsException extends RuntimeException {
    public InvalidCognitoUserCredentialsException(String message) {
        super(message);
    }
}
