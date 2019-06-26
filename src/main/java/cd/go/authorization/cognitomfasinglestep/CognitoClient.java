package cd.go.authorization.cognitomfasinglestep;

import cd.go.authorization.cognitomfasinglestep.exception.InvalidCognitoUserStateException;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;

public class CognitoClient {
    final private AWSCognitoIdentityProvider cognito;
    final private String cognitoClientId;
    public CognitoClient(AWSCognitoIdentityProvider client, String clientId) {
        cognito = client;
        cognitoClientId = clientId;
    }

    public GetUserResult login(String user, String password, String totp) {

        InitiateAuthRequest authRequest = new InitiateAuthRequest();
        authRequest.setAuthFlow("USER_PASSWORD_AUTH");
        authRequest.setClientId(cognitoClientId);
        authRequest.addAuthParametersEntry("USERNAME", user);
        authRequest.addAuthParametersEntry("PASSWORD", password);

        InitiateAuthResult auth;
        try {
            auth = cognito.initiateAuth(authRequest);
        } catch (UserNotFoundException | NotAuthorizedException e) {
            return null;
        }

        if (!auth.getChallengeName().equals("SOFTWARE_TOKEN_MFA")) {
            throw new InvalidCognitoUserStateException("Invalid challenge name: " + auth.getChallengeName());
        }

        RespondToAuthChallengeRequest challengeRequest = new RespondToAuthChallengeRequest();
        challengeRequest.setChallengeName(ChallengeNameType.SOFTWARE_TOKEN_MFA);
        challengeRequest.setSession(auth.getSession());
        challengeRequest.setClientId(cognitoClientId);
        challengeRequest.addChallengeResponsesEntry("USERNAME", user);
        challengeRequest.addChallengeResponsesEntry("SOFTWARE_TOKEN_MFA_CODE", totp);

        RespondToAuthChallengeResult login;
        try {
             login = cognito.respondToAuthChallenge(challengeRequest);
        } catch (CodeMismatchException e) {
            return null;
        }

        GetUserRequest userRequest = new GetUserRequest();
        userRequest.setAccessToken(login.getAuthenticationResult().getAccessToken());
        return cognito.getUser(userRequest);
    }
}
