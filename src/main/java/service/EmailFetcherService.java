package service;

import config.MailServerConfig;
import exceptions.MailFetchException;
import protocol.CommonEmailProtocolSupport;
import protocol.MicrosoftGraphProtocolSupport;

public class EmailFetcherService {
    private final CommonEmailProtocolSupport commonEmailProtocolSupport;
    private final MicrosoftGraphProtocolSupport microsoftGraphProtocolSupport;

    public EmailFetcherService(MailServerConfig config) {
        this.commonEmailProtocolSupport = new CommonEmailProtocolSupport(config);
        this.microsoftGraphProtocolSupport = new MicrosoftGraphProtocolSupport(config);
    }

    public String fetchOldestUnreadEmailContent() throws MailFetchException {
        String emailContent = commonEmailProtocolSupport.fetchOldestUnreadEmail();
        if (!emailContent.isEmpty()) {
            return emailContent;
        }
        return microsoftGraphProtocolSupport.fetchOldestUnreadEmail();
    }
}
