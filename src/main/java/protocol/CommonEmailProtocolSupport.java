package protocol;

import config.MailServerConfig;
import io.vavr.collection.List;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * CommonEmailProtocolSupport provides support for fetching emails using common email protocols like IMAP and POP3.
 */
public class CommonEmailProtocolSupport {

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
     * Fetches emails using the configured common email protocol.
     *
     * @return a list of email messages
     * @throws MessagingException if fetching emails fails
     */
    public List<String> fetchEmails() throws MessagingException {
        List<String> emails = List.empty();

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
        Session emailSession = Session.getDefaultInstance(properties);
        Store store = emailSession.getStore(mailServerConfig.getEmailProtocol());
        store.connect(mailServerConfig.getEmailServerHost(), mailServerConfig.getEmailServerUsername(), mailServerConfig.getEmailServerPassword());

        // Open the inbox folder
        Folder emailFolder = store.getFolder("INBOX");
        emailFolder.open(Folder.READ_ONLY);

        // Fetch messages
        Message[] messages = emailFolder.getMessages();
        for (Message message : messages) {
            MimeMessage mimeMessage = (MimeMessage) message;
            String emailContent = processMessage(mimeMessage);
            emails = emails.append(emailContent);
        }

        // Close connections
        emailFolder.close(false);
        store.close();

        return emails;
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
            throw new MessagingException("Failed to process email message", e);
        }
    }
}
