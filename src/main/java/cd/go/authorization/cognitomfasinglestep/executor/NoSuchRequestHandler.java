package cd.go.authorization.cognitomfasinglestep.executor;

public class NoSuchRequestHandler extends RuntimeException {
    public NoSuchRequestHandler(String message) {
        super(message);
    }
}
