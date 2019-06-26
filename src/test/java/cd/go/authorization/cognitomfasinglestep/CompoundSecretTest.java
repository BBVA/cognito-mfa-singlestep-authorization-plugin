package cd.go.authorization.cognitomfasinglestep;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CompoundSecretTest {
    @Test
    public void shouldSplitPasswordAndTOTP() throws Exception {
        CompoundSecret secret = new CompoundSecret("pass123456");
        assertThat(secret.getPassword(), is("pass"));
        assertThat(secret.getTOTP(), is("123456"));
    }

    @Test
    public void shouldSplitEmptyPasswordAndTOTP() throws Exception {
        CompoundSecret secret = new CompoundSecret("123456");
        assertThat(secret.getPassword(), is(""));
        assertThat(secret.getTOTP(), is("123456"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsOnEmptySecret() throws Exception {
        CompoundSecret secret = new CompoundSecret("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsOnTooShortSecret() throws Exception {
        CompoundSecret secret = new CompoundSecret("23456");
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsOnInvalidTOTPPart() throws Exception {
        CompoundSecret secret = new CompoundSecret("pass12XX56");
    }
}
