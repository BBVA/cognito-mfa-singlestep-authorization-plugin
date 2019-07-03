# Cognito MFA single-step GoCD Authorization Plugin

Start GoCD with the following environment variable set:

```plain
GOCD_PLUGIN_INSTALL_cognito-mfa-singlestep-authorization=https://github.com/BBVA/cognito-mfa-singlestep-authorization-plugin/releases/download/v0.0.1/cognito-mfa-singlestep-authorization-plugin-0.0.1.jar
```

Afterwards you can use the user interface configuration wizard to configure the plugin.

Alternatively you can set the configuration in `cruise-config.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<cruise xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="cruise-config.xsd" schemaVersion="124">
  <server ...>
    <security>
      <authConfigs>
        <authConfig id="users" pluginId="cd.go.authorization.cognito-mfa-singlestep">
          <property>
            <key>ClientId</key>
            <value>...</value>
          </property>
          <property>
            <key>RegionName</key>
            <value>...</value>
          </property>
        </authConfig>
      </authConfigs>
    </security>
    <backup emailOnSuccess="true" emailOnFailure="true" />
  </server>
</cruise>
```

In the configuration of your Cognito User Pool make sure that:

- TOTP MFA is set as **mandatory**.

And the Cognito User Pool App:

- Has *Enabled Identity Providers* **checked**.
- Has *Enable username-password (non-SRP) flow for app-based authentication (USER_PASSWORD_AUTH)* **checked**.
- The *App client secret* is **not set**.
