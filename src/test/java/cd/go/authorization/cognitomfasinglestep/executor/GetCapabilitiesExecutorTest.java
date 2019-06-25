package cd.go.authorization.cognitomfasinglestep.executor;

import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

public class GetCapabilitiesExecutorTest {

    @Test
    public void shouldReturnsPluginCapabilities() throws Exception {
        GoPluginApiResponse response = new GetCapabilitiesExecutor().execute();

        String expectedJSON = "{\n" +
                "    \"supported_auth_type\":\"password\",\n" +
                "    \"can_search\":false,\n" +
                "    \"can_authorize\":false,\n" +
                "    \"can_get_user_roles\":false\n" +
                "}";

        JSONAssert.assertEquals(expectedJSON, response.responseBody(), true);
    }
}
