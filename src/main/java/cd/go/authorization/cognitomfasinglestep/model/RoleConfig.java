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

package cd.go.authorization.cognitomfasinglestep.model;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import static cd.go.authorization.cognitomfasinglestep.utils.Util.GSON;

public class RoleConfig {
    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("auth_config_id")
    private String authConfigId;

    @Expose
    @SerializedName("configuration")
    private RoleConfiguration roleConfiguration;

    public static List<RoleConfig> fromJSONList(String requestBody) {
        Type type = new TypeToken<List<RoleConfig>>() {
        }.getType();

        JsonObject jsonObject = GSON.fromJson(requestBody, JsonObject.class);
        return GSON.fromJson(jsonObject.get("role_configs").toString(), type);
    }

    public String getName() {
        return name;
    }

    public RoleConfiguration getRoleConfiguration() {
        return roleConfiguration;
    }

    public String getAuthConfigId() {
        return authConfigId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleConfig that = (RoleConfig) o;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return roleConfiguration != null ? roleConfiguration.equals(that.roleConfiguration) : that.roleConfiguration == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (roleConfiguration != null ? roleConfiguration.hashCode() : 0);
        return result;
    }

    public static RoleConfig fromJSON(String json) {
        return GSON.fromJson(json, RoleConfig.class);
    }
}
