package cd.go.authorization.cognitomfasinglestep.executor;

import cd.go.authorization.cognitomfasinglestep.Authenticator;
import org.junit.Ignore;
import org.junit.Test;

public class UserAuthenticationExecutorTest {
    @Test
    @Ignore
    public void shouldAuthenticate() throws Exception {
        new UserAuthenticationExecutor(null, new Authenticator()).execute();
    }
}