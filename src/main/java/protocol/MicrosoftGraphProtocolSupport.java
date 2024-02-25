package protocol;

import com.microsoft.graph.core.ClientException;
import com.microsoft.graph.models.Message;
import com.microsoft.graph.requests.GraphServiceClient;
import com.microsoft.graph.requests.MessageCollectionPage;
import com.microsoft.graph.requests.MessageCollectionRequest;
import com.microsoft.graph.requests.MessageCollectionRequestBuilder;
import com.microsoft.graph.authentication.IAuthenticationProvider;
import com.microsoft.graph.http.IHttpRequest;
import config.MailServerConfig;
import io.vavr.collection.List;
import okhttp3.Request;

/**
 * MicrosoftGraphProtocolSupport provides support for fetching emails using Microsoft Graph API.
 */
public class MicrosoftGraphProtocolSupport {

    private final MailServerConfig mailServerConfig;

    /**
     * Constructs a MicrosoftGraphProtocolSupport with the given mail server configuration.
     *
     * @param mailServerConfig the configuration for the mail server
     */
    public MicrosoftGraphProtocolSupport(MailServerConfig mailServerConfig) {
        this.mailServerConfig = mailServerConfig;
    }

    /**
     * Fetches emails using the Microsoft Graph protocol.
     *
     * @return a list of email messages
     * @throws Exception if fetching emails fails
     */
    public List<String> fetchEmails() throws Exception {
        List<String> emails = List.empty();

        // Setup the authentication provider
        IAuthenticationProvider authProvider = new IAuthenticationProvider() {
            @Override
            public void authenticateRequest(IHttpRequest request) {
                // Add the access token to the request header
                Request.Builder requestBuilder = ((Request) request).newBuilder();
                requestBuilder.header("Authorization", "Bearer " + mailServerConfig.getGraphAccessToken());
                request = (IHttpRequest) requestBuilder.build();
            }
        };

        // Initialize GraphServiceClient
        GraphServiceClient graphClient = GraphServiceClient.builder().authenticationProvider(authProvider).buildClient();

        try {
            // Fetch messages from the inbox
            MessageCollectionRequestBuilder requestBuilder = graphClient.me().messages();
            MessageCollectionRequest request = requestBuilder.buildRequest();
            MessageCollectionPage messages = request.get();

            for (Message message : messages.getCurrentPage()) {
                String emailContent = "From: " + message.sender.emailAddress.address +
                        "\nTo: " + message.toRecipients.get(0).emailAddress.address + // Simplified for the first recipient
                        "\nSubject: " + message.subject +
                        "\nSent Date: " + message.sentDateTime.toString();
                emails = emails.append(emailContent);
            }
        } catch (ClientException e) {
            throw new Exception("Failed to fetch emails using Microsoft Graph", e);
        }

        return emails;
    }
}
