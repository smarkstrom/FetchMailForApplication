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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import exceptions.MailFetchException;

/**
 * MicrosoftGraphProtocolSupport provides support for fetching emails using Microsoft Graph API.
 */
public class MicrosoftGraphProtocolSupport {

    private static final Logger logger = LogManager.getLogger(MicrosoftGraphProtocolSupport.class);
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
     * Fetches the oldest unread email using the Microsoft Graph protocol.
     *
     * @return the content of the oldest unread email
     * @throws MailFetchException if fetching emails fails
     */
    public String fetchOldestUnreadEmail() throws MailFetchException {
        logger.debug("Starting to fetch the oldest unread email using Microsoft Graph.");
        String emailContent = "";

        // Setup the authentication provider
        IAuthenticationProvider authProvider = new IAuthenticationProvider() {
            @Override
            public void authenticateRequest(IHttpRequest request) {
                // Add the access token to the request header
                Request.Builder requestBuilder = ((Request) request).newBuilder();
                requestBuilder.header("Authorization", "Bearer " + mailServerConfig.getGraphAccessToken());
                request = (IHttpRequest) requestBuilder.build();
                logger.debug("Authentication request has been made.");
            }
        };

        // Initialize GraphServiceClient
        GraphServiceClient graphClient = GraphServiceClient.builder().authenticationProvider(authProvider).buildClient();
        logger.debug("GraphServiceClient has been initialized.");

        try {
            // Fetch the oldest unread message
            MessageCollectionPage messages = graphClient.me().messages()
                    .buildRequest()
                    .filter("isRead eq false")
                    .top(1)
                    .orderBy("receivedDateTime asc")
                    .get();
            logger.debug("Oldest unread message has been fetched.");

            if (messages.getCurrentPage().size() > 0) {
                Message message = messages.getCurrentPage().get(0);
                emailContent = "From: " + message.sender.emailAddress.address +
                        "\nTo: " + message.toRecipients.get(0).emailAddress.address + // Simplified for the first recipient
                        "\nSubject: " + message.subject +
                        "\nSent Date: " + message.sentDateTime.toString();
            }
        } catch (ClientException e) {
            throw new MailFetchException("Failed to fetch the oldest unread email using Microsoft Graph", e);
        }

        logger.debug("Finished fetching the oldest unread email using Microsoft Graph.");
        return emailContent;
    }
}
