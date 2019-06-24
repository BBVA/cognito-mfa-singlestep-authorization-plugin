package cd.go.authorization.cognitomfasinglestep.exception;

public class InvalidCognitoUserStateException extends RuntimeException {
    public InvalidCognitoUserStateException(String message) {
        super(message);
    }
}
