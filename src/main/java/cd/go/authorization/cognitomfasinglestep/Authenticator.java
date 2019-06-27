package cd.go.authorization.cognitomfasinglestep;

import cd.go.authorization.cognitomfasinglestep.model.AuthConfig;
import cd.go.authorization.cognitomfasinglestep.model.AuthenticationResponse;
import cd.go.authorization.cognitomfasinglestep.model.Credentials;
import cd.go.authorization.cognitomfasinglestep.model.User;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;

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
            final String clientId = authConfig.getConfiguration().getClientId();

            AWSCognitoIdentityProvider cognito = AWSCognitoIdentityProviderClientBuilder.defaultClient();

            CognitoSingleStepLoginManager loginManager = new CognitoSingleStepLoginManager(cognito, clientId);

            GetUserResult cognitouser = loginManager.login(credentials.getUsername(), secret.getPassword(), secret.getTOTP());
            if (cognitouser == null) continue;

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
        }
        return null;
    }
}