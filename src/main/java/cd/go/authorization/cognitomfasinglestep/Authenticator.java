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