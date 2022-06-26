package it.daaar.learning.email;

import org.junit.jupiter.api.Test;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

class MailBoxIMAPManualTest {

    protected static final String USERNAME = "changeme@gmail.com";
    protected static final String PASSWORD = "changeme";
    protected static final String FOLDER_ID = "test";

    @Test
    void retrieveMessages() throws MessagingException {
        MailBox mailBox = new MailBoxIMAP();
        mailBox.connect("imap.gmail.com", USERNAME, PASSWORD);

        List<Message> messages = mailBox.retrieveMessages(FOLDER_ID);

        for (Message message : messages) {
            System.out.println("");
            System.out.println("subject: " + message.getSubject());
            System.out.println("from: " + stream(message.getFrom()).map(Address::toString).collect(toList()));
            System.out.println("sent date: " + message.getSentDate());
//             System.out.println("content: " + contentFrom(message));
        }

//        messages.get(0).setFlag(DELETED, true);
//        mailBox.expungeFolder(FOLDER_ID);

        mailBox.disconnect();
    }
}