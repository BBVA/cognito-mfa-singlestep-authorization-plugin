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
    @SerializedName("Region")
    @ProfileField(key = "Region", required = true, secure = false)
    private String region;

    @Expose
    @SerializedName("ClientId")
    @ProfileField(key = "ClientId", required = true, secure = false)
    private String clientId;

    public static Configuration fromJSON(String json) {
        return GSON.fromJson(json, Configuration.class);
    }

    public String getRegion() {
        return region;
    }

    public String getClientId() {
        return clientId;
    }

    public static List<Map<String, String>> validate(Map<String, String> properties) {
        final List<Map<String, String>> validationResult = MetadataHelper.validate(Configuration.class, properties);
        return validationResult;
    }
}
