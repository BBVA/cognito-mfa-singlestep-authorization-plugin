package cd.go.authorization.cognitomfasinglestep;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CompoundSecretSplitterTest {
    @Test
    public void shouldSplitPasswordAndTOTP() throws Exception {
        CompoundSecretSplitter secret = new CompoundSecretSplitter("pass123456");
        assertThat(secret.getPassword(), is("pass"));
        assertThat(secret.getTOTP(), is("123456"));
    }

    @Test
    public void shouldSplitEmptyPasswordAndTOTP() throws Exception {
        CompoundSecretSplitter secret = new CompoundSecretSplitter("123456");
        assertThat(secret.getPassword(), is(""));
        assertThat(secret.getTOTP(), is("123456"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsOnEmptySecret() throws Exception {
        CompoundSecretSplitter secret = new CompoundSecretSplitter("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsOnTooShortSecret() throws Exception {
        CompoundSecretSplitter secret = new CompoundSecretSplitter("23456");
    }

    @Test(expected = IllegalArgumentException.class)
    public void throwsOnInvalidTOTPPart() throws Exception {
        CompoundSecretSplitter secret = new CompoundSecretSplitter("pass12XX56");
    }
}
