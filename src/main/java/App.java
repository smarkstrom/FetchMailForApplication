import config.MailServerConfig;
import exceptions.MailFetchException;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.EmailFetcherService;

public class App {
    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main(String[] args) {
        MailServerConfig config;
        try {
            config = new MailServerConfig("mailserver.properties");
        } catch (IOException e) {
            logger.error("Unable to find the properties file");
            return;
        }
        
        EmailFetcherService emailFetcherService = new EmailFetcherService(config);
        
        try {
            String emailContent = emailFetcherService.fetchOldestUnreadEmailContent();
            System.out.println(emailContent);
        } catch (MailFetchException e) {
            logger.error("Error fetching the oldest unread email: ", e);
            // Handle the error appropriately here, e.g., notify the user, retry, etc.
        }
    }
}
