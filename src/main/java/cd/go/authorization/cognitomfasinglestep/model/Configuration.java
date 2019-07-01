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

import cd.go.authorization.cognitomfasinglestep.annotation.MetadataHelper;
import cd.go.authorization.cognitomfasinglestep.annotation.ProfileField;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

import static cd.go.authorization.cognitomfasinglestep.utils.Util.GSON;

public class Configuration {
    @Expose
    @SerializedName("ClientId")
    @ProfileField(key = "ClientId", required = true, secure = false)
    private String clientId;

    @Expose
    @SerializedName("RegionName")
    @ProfileField(key = "RegionName", required = true, secure = false)
    private String regionName;

    public static Configuration fromJSON(String json) {
        return GSON.fromJson(json, Configuration.class);
    }

    public String getClientId() {
        return clientId;
    }

    public String getRegionName() {
        return regionName;
    }

    public static List<Map<String, String>> validate(Map<String, String> properties) {
        final List<Map<String, String>> validationResult = MetadataHelper.validate(Configuration.class, properties);
        return validationResult;
    }
}
