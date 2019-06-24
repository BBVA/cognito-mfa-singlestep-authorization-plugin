package cd.go.authorization.cognitomfasinglestep.annotation;

public interface Metadata {
    boolean isRequired();

    boolean isSecure();

    FieldType getType();
}
