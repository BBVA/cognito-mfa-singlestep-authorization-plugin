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

import cd.go.authorization.cognitomfasinglestep.exception.InvalidCognitoUserStateException;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class CognitoSingleStepLoginManagerTest {
    @Test
    public void loginShouldReturnNullWhenUserNotFound() throws Exception {
        AWSCognitoIdentityProvider cognito = mock(AWSCognitoIdentityProvider.class);  // TODO: Learn about mockito annotations
        when(cognito.initiateAuth(any())).thenThrow(UserNotFoundException.class);

        CognitoSingleStepLoginManager client = new CognitoSingleStepLoginManager(cognito, "clientid");

        assertNull(client.login("USER", "PASS", "OTP"));
    }

    @Test
    public void loginShouldReturnNullWhenPasswordIsIncorrect() throws Exception {
        AWSCognitoIdentityProvider cognito = mock(AWSCognitoIdentityProvider.class);
        when(cognito.initiateAuth(any())).thenThrow(NotAuthorizedException.class);

        CognitoSingleStepLoginManager client = new CognitoSingleStepLoginManager(cognito, "clientid");

        assertNull(client.login("USER", "PASS", "OTP"));
    }

    @Test(expected = InvalidCognitoUserStateException.class)
    public void loginShouldThrowInvalidUserStateIfNotConfigured() throws Exception {
        AWSCognitoIdentityProvider cognito = mock(AWSCognitoIdentityProvider.class);
        InitiateAuthResult auth = mock(InitiateAuthResult.class);
        when(cognito.initiateAuth(any())).thenReturn(auth);
        when(auth.getChallengeName()).thenReturn("MFA_SETUP");

        CognitoSingleStepLoginManager client = new CognitoSingleStepLoginManager(cognito, "clientid");

        client.login("USER", "PASS", "OTP");
    }

    @Test
    public void loginShouldReturnNullWhenTOTPIsIncorrect() throws Exception {
        AWSCognitoIdentityProvider cognito = mock(AWSCognitoIdentityProvider.class);
        InitiateAuthResult auth = mock(InitiateAuthResult.class);
        when(cognito.initiateAuth(any())).thenReturn(auth);
        when(auth.getChallengeName()).thenReturn("SOFTWARE_TOKEN_MFA");
        when(cognito.respondToAuthChallenge(any())).thenThrow(CodeMismatchException.class);

        CognitoSingleStepLoginManager client = new CognitoSingleStepLoginManager(cognito, "clientid");

        assertNull(client.login("USER", "PASS", "OTP"));
    }


    @Test
    public void loginShouldReturnCognitoUserRequest() throws Exception {
        AWSCognitoIdentityProvider cognito = mock(AWSCognitoIdentityProvider.class);
        InitiateAuthResult auth = mock(InitiateAuthResult.class);
        RespondToAuthChallengeResult login = mock(RespondToAuthChallengeResult.class, RETURNS_DEEP_STUBS);
        GetUserResult user = new GetUserResult();

        when(cognito.initiateAuth(any())).thenReturn(auth);
        when(auth.getChallengeName()).thenReturn("SOFTWARE_TOKEN_MFA");
        when(cognito.respondToAuthChallenge(any())).thenReturn(login);
        when(login.getAuthenticationResult().getAccessToken()).thenReturn("TOKEN");
        when(cognito.getUser(any())).thenReturn(user);

        CognitoSingleStepLoginManager client = new CognitoSingleStepLoginManager(cognito, "clientid");

        assertSame(client.login("USER", "PASS", "OTP"), user);
    }

}
