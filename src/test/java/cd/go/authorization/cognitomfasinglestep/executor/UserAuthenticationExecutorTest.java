package cd.go.authorization.cognitomfasinglestep.executor;

import cd.go.authorization.cognitomfasinglestep.Authenticator;
import cd.go.authorization.cognitomfasinglestep.Authorizer;
import org.junit.Test;

public class UserAuthenticationExecutorTest {
    @Test
    public void shouldAuthenticate() throws Exception {
        new UserAuthenticationExecutor(null, new Authenticator(), new Authorizer()).execute();
    }
}