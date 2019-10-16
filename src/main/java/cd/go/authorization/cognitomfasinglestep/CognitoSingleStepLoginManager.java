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

import cd.go.authorization.cognitomfasinglestep.exception.InvalidCognitoUserCredentialsException;
import cd.go.authorization.cognitomfasinglestep.exception.InvalidCognitoUserStateException;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;

public class CognitoSingleStepLoginManager {
    final private AWSCognitoIdentityProvider cognito;
    final private String cognitoClientId;

    public CognitoSingleStepLoginManager(AWSCognitoIdentityProvider client, String clientId) {
        cognito = client;
        cognitoClientId = clientId;
    }

    public GetUserResult login(String user, String password, String totp) {
        try {
            InitiateAuthResult auth = startAuth(user, password);
            if (!auth.getChallengeName().equals("SOFTWARE_TOKEN_MFA")) {
                throw new InvalidCognitoUserStateException("Invalid challenge type: " + auth.getChallengeName());
            }
            RespondToAuthChallengeResult login = finishAuth(auth.getSession(), user, totp);
            if (login.getChallengeName() != null) {
                throw new InvalidCognitoUserStateException("Unexpected challenge: " + auth.getChallengeName());
            }

            GetUserRequest userRequest = new GetUserRequest();
            userRequest.setAccessToken(login.getAuthenticationResult().getAccessToken());
            return cognito.getUser(userRequest);

        } catch (InvalidCognitoUserCredentialsException e) {
            // TODO: Remove this catch and return null on each step or add here some log messages if allowed
            return null;
        }
    }

    private InitiateAuthResult startAuth(String user, String password) {
        InitiateAuthRequest authRequest = new InitiateAuthRequest();
        authRequest.setAuthFlow("USER_PASSWORD_AUTH");
        authRequest.setClientId(cognitoClientId);
        authRequest.addAuthParametersEntry("USERNAME", user);
        authRequest.addAuthParametersEntry("PASSWORD", password);

        try {
            return cognito.initiateAuth(authRequest);
        } catch (UserNotFoundException | NotAuthorizedException e) {
            throw new InvalidCognitoUserCredentialsException("Invalid user or password");
        }
    }

    private RespondToAuthChallengeResult finishAuth(String session, String user, String totp) {
        RespondToAuthChallengeRequest challengeRequest = new RespondToAuthChallengeRequest();
        challengeRequest.setChallengeName(ChallengeNameType.SOFTWARE_TOKEN_MFA);
        challengeRequest.setSession(session);
        challengeRequest.setClientId(cognitoClientId);
        challengeRequest.addChallengeResponsesEntry("USERNAME", user);
        challengeRequest.addChallengeResponsesEntry("SOFTWARE_TOKEN_MFA_CODE", totp);

        try {
            return cognito.respondToAuthChallenge(challengeRequest);
        } catch (CodeMismatchException e) {
            throw new InvalidCognitoUserCredentialsException("Invalid TOTP");
        }

    }
}
