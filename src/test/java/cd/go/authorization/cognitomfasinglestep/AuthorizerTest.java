package cd.go.authorization.cognitomfasinglestep;

import org.junit.Test;

public class AuthorizerTest {
    @Test
    public void shouldAuthorize() throws Exception {
        new Authorizer().authorize(null, null, null);
    }
}