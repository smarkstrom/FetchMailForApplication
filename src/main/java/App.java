import email.EmailFetcher;
import email.EmailStorage;
import protocol.CommonEmailProtocolSupport;
import protocol.MicrosoftGraphProtocolSupport;
import config.MailServerConfig;
import java.io.IOException;

/**
 * Main application class for fetching emails and storing them using various protocols.
 */
public class App {

    public static void main(String[] args) {
        // Load mail server configuration
        MailServerConfig config = new MailServerConfig("mailserver.properties");

        // Initialize protocol support
        CommonEmailProtocolSupport commonEmailProtocolSupport = new CommonEmailProtocolSupport(config);
        MicrosoftGraphProtocolSupport microsoftGraphProtocolSupport = new MicrosoftGraphProtocolSupport(config);

        // Initialize email fetcher and storage
        EmailFetcher emailFetcher = new EmailFetcher(commonEmailProtocolSupport, microsoftGraphProtocolSupport);
        EmailStorage emailStorage = new EmailStorage();

        try {
            // Fetch emails using configured protocols
            emailFetcher.fetchEmails().forEach(email -> {
                // Store each email message data into a text file
                try {
                    emailStorage.storeEmail(email);
                } catch (IOException e) {
                    System.err.println("Error storing email: " + e.getMessage());
                }
            });
        } catch (Exception e) {
            System.err.println("Error fetching emails: " + e.getMessage());
        }
    }
}
