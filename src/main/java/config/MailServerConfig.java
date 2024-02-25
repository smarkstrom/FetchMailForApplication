package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * MailServerConfig class is responsible for loading and providing
 * mail server configuration details from a properties file.
 */
public class MailServerConfig {

    private final Properties properties;

    /**
     * Constructor for MailServerConfig.
     * It loads the configuration from the specified properties file.
     *
     * @throws IOException If there is an issue reading the properties file.
     */
    public MailServerConfig(String fileLocation) throws IOException {
        properties = new Properties();
        // Load the properties file
        try (FileInputStream fis = new FileInputStream(fileLocation)) {
            properties.load(fis);
        }
    }

    public String getEmailServerHost() {
        return properties.getProperty("email.server.host");
    }

    public int getEmailServerPort() {
        return Integer.parseInt(properties.getProperty("email.server.port"));
    }

    public String getEmailServerUsername() {
        return properties.getProperty("email.server.username");
    }

    public String getEmailServerPassword() {
        return properties.getProperty("email.server.password");
    }

    public String getEmailProtocol() {
        return properties.getProperty("email.protocol");
    }

    public int getEmailProtocolPort() {
        return Integer.parseInt(properties.getProperty("email.protocol.port"));
    }

    public String getGraphClientId() {
        return properties.getProperty("graph.clientId");
    }

    public String getGraphClientSecret() {
        return properties.getProperty("graph.clientSecret");
    }

    public String getGraphTenantId() {
        return properties.getProperty("graph.tenantId");
    }

    public String getGraphAuthority() {
        return properties.getProperty("graph.authority");
    }

    public String getGraphRedirectUri() {
        return properties.getProperty("graph.redirectUri");
    }

    public String getGraphScope() {
        return properties.getProperty("graph.scope");
    }

    public boolean getEmailSslEnable() {
        return Boolean.parseBoolean(properties.getProperty("email.ssl.enable"));
    }

    public String getEmailSslTrust() {
        return properties.getProperty("email.ssl.trust");
    }

    public String getEmailSslTrustPassword() {
        return properties.getProperty("email.ssl.trust.password");
    }
}
