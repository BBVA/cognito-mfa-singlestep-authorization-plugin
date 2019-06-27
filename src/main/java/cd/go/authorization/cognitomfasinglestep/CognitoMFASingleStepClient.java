package cd.go.authorization.cognitomfasinglestep;

import cd.go.authorization.cognitomfasinglestep.exception.InvalidCognitoUserCredentialsException;
import cd.go.authorization.cognitomfasinglestep.exception.InvalidCognitoUserStateException;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;

public class CognitoMFASingleStepClient {
    final private AWSCognitoIdentityProvider cognito;
    final private String cognitoClientId;
    public CognitoMFASingleStepClient(AWSCognitoIdentityProvider client, String clientId) {
        cognito = client;
        cognitoClientId = clientId;
    }

    public GetUserResult login(String user, String password, String totp) {

        try {
            InitiateAuthResult auth = initiateAuth(user, password);
            if (!auth.getChallengeName().equals("SOFTWARE_TOKEN_MFA")) {
                throw new InvalidCognitoUserStateException("Invalid challenge name: " + auth.getChallengeName());
            }
            RespondToAuthChallengeResult login = finishAuth(auth.getSession(), user, totp);

            GetUserRequest userRequest = new GetUserRequest();
            userRequest.setAccessToken(login.getAuthenticationResult().getAccessToken());
            return cognito.getUser(userRequest);

        } catch (InvalidCognitoUserCredentialsException e) {
            return null;
        }
    }

    private InitiateAuthResult initiateAuth(String user, String password) {
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
