package cd.go.authorization.cognitomfasinglestep.executor;

import cd.go.authorization.cognitomfasinglestep.model.Capabilities;
import cd.go.authorization.cognitomfasinglestep.model.SupportedAuthType;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import static com.thoughtworks.go.plugin.api.response.DefaultGoApiResponse.SUCCESS_RESPONSE_CODE;

public class GetCapabilitiesExecutor {

    public GoPluginApiResponse execute() {
        Capabilities capabilities = getCapabilities();
        return new DefaultGoPluginApiResponse(SUCCESS_RESPONSE_CODE, capabilities.toJSON());
    }

    Capabilities getCapabilities() {
        return new Capabilities(SupportedAuthType.Password, false, false, false);
    }
}
