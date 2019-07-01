/*
 * Copyright 2019 Banco Bilbao Vizcaya Argentaria, S.A.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
