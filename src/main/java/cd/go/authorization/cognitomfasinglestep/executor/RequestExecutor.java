package cd.go.authorization.cognitomfasinglestep.executor;

import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

public interface RequestExecutor {

    GoPluginApiResponse execute() throws Exception;
}
