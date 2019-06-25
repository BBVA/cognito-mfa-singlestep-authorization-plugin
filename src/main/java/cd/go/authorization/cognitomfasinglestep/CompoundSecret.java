package cd.go.authorization.cognitomfasinglestep;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class CompoundSecret {
    private final Pattern format = Pattern.compile("^(.*)(\\d{6})$", Pattern.DOTALL);
    private final String password;
    private final String totp;

    public CompoundSecret(String secret) {
        Matcher matcher = format.matcher(secret);
        if (matcher.matches()) {
            password = matcher.group(1);
            totp = matcher.group(2);
        } else {
            throw new IllegalArgumentException("Invalid secret format");
        }
    }

    public String getPassword() {
        return password;
    }

    public String getTOTP() {
        return totp;
    }
}
