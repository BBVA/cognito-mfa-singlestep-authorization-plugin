package cd.go.authorization.cognitomfasinglestep;

import cd.go.authorization.cognitomfasinglestep.model.AuthConfig;
import cd.go.authorization.cognitomfasinglestep.model.RoleConfig;
import cd.go.authorization.cognitomfasinglestep.model.User;

import java.util.List;
import java.util.Set;

public class Authorizer {
    public Set<String> authorize(User user, AuthConfig authConfig, List<RoleConfig> roleConfigs) {
        throw new RuntimeException("Implement me!");
    }
}
