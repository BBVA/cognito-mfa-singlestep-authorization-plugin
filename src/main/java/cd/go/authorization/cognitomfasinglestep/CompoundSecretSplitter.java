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

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class CompoundSecretSplitter {
    private final Pattern format = Pattern.compile("^(.*)(\\d{6})$", Pattern.DOTALL);
    private final String password;
    private final String totp;

    public CompoundSecretSplitter(String secret) {
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
