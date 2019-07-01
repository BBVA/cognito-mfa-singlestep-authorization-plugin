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

package cd.go.authorization.cognitomfasinglestep.executor;

import cd.go.authorization.cognitomfasinglestep.Authenticator;
import cd.go.authorization.cognitomfasinglestep.model.AuthConfig;
import cd.go.authorization.cognitomfasinglestep.model.Credentials;
import cd.go.authorization.cognitomfasinglestep.model.AuthenticationResponse;
import cd.go.authorization.cognitomfasinglestep.model.RoleConfig;

import com.google.gson.Gson;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.*;

import static com.thoughtworks.go.plugin.api.response.DefaultGoApiResponse.SUCCESS_RESPONSE_CODE;

public class UserAuthenticationExecutor implements RequestExecutor {
    private static final Gson GSON = new Gson();
    private final GoPluginApiRequest request;
    private final Authenticator authenticator;

    public UserAuthenticationExecutor(GoPluginApiRequest request, Authenticator authenticator) {
        this.request = request;
        this.authenticator = authenticator;
    }

    @Override
    public GoPluginApiResponse execute() throws Exception {
        Credentials credentials = Credentials.fromJSON(request.requestBody());
        final List<AuthConfig> authConfigs = AuthConfig.fromJSONList(request.requestBody());
        final List<RoleConfig> roleConfigs = RoleConfig.fromJSONList(request.requestBody());

        AuthenticationResponse authenticationResponse = authenticator.authenticate(credentials, authConfigs);

        Map<String, Object> userMap = new HashMap<>();
        if (authenticationResponse != null) {
            userMap.put("user", authenticationResponse.getUser());
            final List<String> assignedRoles = new ArrayList<>();
            userMap.put("roles", assignedRoles);
        }

        DefaultGoPluginApiResponse response = new DefaultGoPluginApiResponse(SUCCESS_RESPONSE_CODE, GSON.toJson(userMap));
        return response;
    }
}
