package email;

import io.vavr.control.Try;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import io.vavr.collection.List;

/**
 * EmailStorage is responsible for storing email messages into a text file.
 */
public class EmailStorage {

    private final String storageFilePath;

    /**
     * Constructs an EmailStorage with the specified file path for storage.
     *
     * @param storageFilePath the path of the file where emails will be stored
     */
    public EmailStorage(String storageFilePath) {
        this.storageFilePath = storageFilePath;
    }

    /**
     * Stores the provided list of email messages into a text file.
     *
     * @param emails the list of email messages to store
     * @return a Try indicating success or failure
     */
    public Try<Void> storeEmails(List<String> emails) {
        return Try.run(() -> {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(storageFilePath, true))) {
                for (String email : emails) {
                    writer.write(email);
                    writer.newLine();
                }
            }
        }).onFailure(IOException.class, e -> System.err.println("Failed to store emails: " + e.getMessage()));
    }
}
