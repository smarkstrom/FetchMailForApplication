package email;

import config.MailServerConfig;
import io.vavr.collection.List;
import protocol.CommonEmailProtocolSupport;
import protocol.MicrosoftGraphProtocolSupport;

/**
 * EmailFetcher is responsible for fetching emails from various protocols.
 */
public class EmailFetcher {

    private final CommonEmailProtocolSupport commonEmailProtocolSupport;
    private final MicrosoftGraphProtocolSupport microsoftGraphProtocolSupport;

    /**
     * Constructs an EmailFetcher with the given protocol supports.
     *
     * @param commonEmailProtocolSupport   the support for common email protocols
     * @param microsoftGraphProtocolSupport the support for Microsoft Graph protocol
     */
    public EmailFetcher(CommonEmailProtocolSupport commonEmailProtocolSupport,
                        MicrosoftGraphProtocolSupport microsoftGraphProtocolSupport) {
        this.commonEmailProtocolSupport = commonEmailProtocolSupport;
        this.microsoftGraphProtocolSupport = microsoftGraphProtocolSupport;
    }

    /**
     * Fetches emails using the configured protocols.
     *
     * @return a list of email messages
     * @throws Exception if fetching emails fails
     */
    public List<String> fetchEmails() throws Exception {
        List<String> emails = List.empty();

        // Fetch emails using common email protocols (IMAP, POP3, etc.)
        List<String> commonProtocolEmails = commonEmailProtocolSupport.fetchEmails();
        emails = emails.appendAll(commonProtocolEmails);

        // Fetch emails using Microsoft Graph protocol
        List<String> graphProtocolEmails = microsoftGraphProtocolSupport.fetchEmails();
        emails = emails.appendAll(graphProtocolEmails);

        return emails;
    }
}
