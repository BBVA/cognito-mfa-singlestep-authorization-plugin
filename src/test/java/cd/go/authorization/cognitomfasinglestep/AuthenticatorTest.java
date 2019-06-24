package cd.go.authorization.cognitomfasinglestep;

import org.junit.Test;

public class AuthenticatorTest {

    @Test
    public void shouldAuthenticate() throws Exception {
        new Authenticator().authenticate(null, null);
    }
}