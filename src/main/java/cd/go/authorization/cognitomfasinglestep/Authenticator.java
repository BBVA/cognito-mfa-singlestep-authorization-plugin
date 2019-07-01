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

import cd.go.authorization.cognitomfasinglestep.model.*;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;

import static cd.go.authorization.cognitomfasinglestep.CognitoMFASingleStepPlugin.LOG;

import java.util.List;

public class Authenticator {
    public AuthenticationResponse authenticate(Credentials credentials, List<AuthConfig> authConfigs) {
        final CompoundSecretSplitter secret;

        try {
            secret = new CompoundSecretSplitter(credentials.getPassword());
        } catch (IllegalArgumentException e) {
            return null;
        }

        for (AuthConfig authConfig : authConfigs) {
            Configuration config = authConfig.getConfiguration();
            try {
                AWSCognitoIdentityProvider cognito = AWSCognitoIdentityProviderClientBuilder.standard().withRegion(config.getRegionName()).build();
                CognitoSingleStepLoginManager loginManager = new CognitoSingleStepLoginManager(cognito, config.getClientId());
                GetUserResult cognitouser = loginManager.login(credentials.getUsername(), secret.getPassword(), secret.getTOTP());
                if (cognitouser == null) continue;
                return new AuthenticationResponse(new User(cognitouser), new AuthConfig());
            } catch (Throwable e) {
                LOG.error("Unexpected error on user login", e);
            }
        }
        return null;
    }
}
