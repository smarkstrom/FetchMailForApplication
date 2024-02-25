package protocol;

import config.MailServerConfig;
import io.vavr.collection.List;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import exceptions.MailFetchException;

/**
 * CommonEmailProtocolSupport provides support for fetching emails using common email protocols like IMAP and POP3.
 */
public class CommonEmailProtocolSupport {

    private static final Logger logger = LogManager.getLogger(CommonEmailProtocolSupport.class);
    private final MailServerConfig mailServerConfig;

    /**
     * Constructs a CommonEmailProtocolSupport with the given mail server configuration.
     *
     * @param mailServerConfig the configuration for the mail server
     */
    public CommonEmailProtocolSupport(MailServerConfig mailServerConfig) {
        this.mailServerConfig = mailServerConfig;
    }

    /**
     * Fetches the oldest unread email using the configured common email protocol.
     *
     * @return the content of the oldest unread email message
     * @throws MailFetchException if fetching emails fails
     */
    public String fetchOldestUnreadEmail() throws MailFetchException {
        logger.debug("Starting to fetch the oldest unread email using common protocols.");
        String emailContent = "";
        try {
            // Setup mail server properties
            Properties properties = new Properties();
            properties.put("mail.store.protocol", mailServerConfig.getEmailProtocol());
            properties.put("mail." + mailServerConfig.getEmailProtocol() + ".host", mailServerConfig.getEmailServerHost());
            properties.put("mail." + mailServerConfig.getEmailProtocol() + ".port", mailServerConfig.getEmailProtocolPort());
            properties.put("mail." + mailServerConfig.getEmailProtocol() + ".starttls.enable", "true");
            if (mailServerConfig.isEmailSslEnable()) {
                properties.put("mail." + mailServerConfig.getEmailProtocol() + ".ssl.enable", "true");
                properties.put("mail." + mailServerConfig.getEmailProtocol() + ".ssl.trust", mailServerConfig.getEmailSslTrust());
            }

            // Connect to the email server
            logger.debug("Connecting to the email server.");
            Session emailSession = Session.getDefaultInstance(properties);
            Store store = emailSession.getStore(mailServerConfig.getEmailProtocol());
            store.connect(mailServerConfig.getEmailServerHost(), mailServerConfig.getEmailServerUsername(), mailServerConfig.getEmailServerPassword());

            // Open the inbox folder
            logger.debug("Opening the inbox folder.");
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            // Fetch the oldest unread message
            logger.debug("Fetching the oldest unread message.");
            Message[] messages = emailFolder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            if (messages.length > 0) {
                Message message = messages[messages.length - 1]; // Assuming the last message is the oldest
                MimeMessage mimeMessage = (MimeMessage) message;
                emailContent = processMessage(mimeMessage);
            }

            // Close connections
            logger.debug("Closing connections.");
            emailFolder.close(false);
            store.close();
        } catch (MessagingException e) {
            logger.error("Failed to fetch the oldest unread email", e);
            throw new MailFetchException("Failed to fetch the oldest unread email using common protocols", e);
        }
        logger.debug("Finished fetching the oldest unread email using common protocols.");
        return emailContent;
    }

    /**
     * Processes a single email message and extracts its content.
     *
     * @param message the email message to process
     * @return the content of the email as a String
     * @throws MessagingException if processing the message fails
     */
    private String processMessage(MimeMessage message) throws MessagingException {
        try {
            return "From: " + InternetAddress.toString(message.getFrom()) +
                    "\nTo: " + InternetAddress.toString(message.getRecipients(Message.RecipientType.TO)) +
                    "\nSubject: " + message.getSubject() +
                    "\nSent Date: " + message.getSentDate().toString();
        } catch (MessagingException e) {
            logger.error("Failed to process email message", e);
            throw e;
        }
    }
}
