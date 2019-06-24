package cd.go.authorization.cognitomfasinglestep;

import cd.go.authorization.cognitomfasinglestep.model.AuthConfig;
import cd.go.authorization.cognitomfasinglestep.model.AuthenticationResponse;
import cd.go.authorization.cognitomfasinglestep.model.Credentials;
import cd.go.authorization.cognitomfasinglestep.model.User;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;

import static cd.go.authorization.cognitomfasinglestep.CognitoMFASingleStepPlugin.LOG;
import cd.go.authorization.cognitomfasinglestep.exception.InvalidCognitoUserStateException;

import java.util.List;

public class Authenticator {
    public AuthenticationResponse authenticate(Credentials credentials, List<AuthConfig> authConfigs) {
        final String clientId = authConfigs.get(0).getConfiguration().getClientId();
        final String passwordField = credentials.getPassword();
        final String password = passwordField.substring(0, passwordField.length() - 6);
        final String totp = passwordField.substring(passwordField.length() - 6);

        LOG.info("User:" + credentials.getUsername() + " Password:" + password + " TOTP:" + totp);
        try {
            AWSCognitoIdentityProvider cognito = AWSCognitoIdentityProviderClientBuilder.defaultClient();

            InitiateAuthRequest authRequest = new InitiateAuthRequest();
            authRequest.setAuthFlow("USER_PASSWORD_AUTH");
            authRequest.setClientId(clientId);
            authRequest.addAuthParametersEntry("USERNAME", credentials.getUsername());
            authRequest.addAuthParametersEntry("PASSWORD", password);

            InitiateAuthResult auth = cognito.initiateAuth(authRequest);
            if (auth.getChallengeName().equals("SOFTWARE_TOKEN_MFA")) {
                RespondToAuthChallengeRequest challengeRequest = new RespondToAuthChallengeRequest();
                challengeRequest.setChallengeName(ChallengeNameType.SOFTWARE_TOKEN_MFA);
                challengeRequest.setSession(auth.getSession());
                challengeRequest.setClientId(clientId);
                challengeRequest.addChallengeResponsesEntry("USERNAME", credentials.getUsername());
                challengeRequest.addChallengeResponsesEntry("SOFTWARE_TOKEN_MFA_CODE", totp);
                RespondToAuthChallengeResult login = cognito.respondToAuthChallenge(challengeRequest);

                GetUserRequest userRequest = new GetUserRequest();
                userRequest.setAccessToken(login.getAuthenticationResult().getAccessToken());
                GetUserResult cognitouser = cognito.getUser(userRequest);


                String username = cognitouser.getUsername();
                String displayName = null;
                String emailId = null;

                for (AttributeType attr : cognitouser.getUserAttributes()){
                    switch (attr.getName()) {
                        case "email":
                            emailId = attr.getValue();
                            break;
                        case "preferred_username":
                            displayName = attr.getValue();
                            break;
                    }

                }
                User user = new User(username, displayName, emailId);
                AuthConfig config = new AuthConfig();
                return new AuthenticationResponse(user, config);

            } else {
                // TODO: Log invalid user state
                throw new InvalidCognitoUserStateException("Invalid challenge: " + auth.getChallengeName());
            }
        } catch(UserNotFoundException e) {
            // Nothing to do
            throw e;
        } catch(NotAuthorizedException e) {
            // Username or password is incorrect.
            // TODO: Fake TOTP delay request
            throw e;
        } catch(CodeMismatchException e) {
            // TOTP is incorrect.
            throw e;
        } catch(Throwable e) {
            LOG.error("AWS Error", e);
        }

        return null;
    }

}