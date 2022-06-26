package it.daaar.learning.email;

import javax.mail.*;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import static java.util.Arrays.asList;
import static javax.mail.Folder.READ_WRITE;
import static javax.mail.Session.getInstance;

public class MailBoxIMAP implements MailBox {

    private Store store;

    @Override
    public void connect(String url, String username, String password) {
        try {

            Properties properties = getProperties();
            Session session = getInstance(properties);
            this.store = session.getStore("imap");
            this.store.connect(url, username, password);

        } catch (MessagingException e) {
            throw new MailBoxException("Error connecting to the email box.", e);
        }
    }

    private Properties getProperties() {
        Properties properties = new Properties();
        properties.put("mail.imap.ssl.checkserveridentity", "false");
        properties.put("mail.imap.ssl.trust", "*");
        properties.setProperty("mail.imap.ssl.enable", "true");
        return properties;
    }

    @Override
    public void disconnect() {
        try {
            store.close();
        } catch (MessagingException e) {
            throw new MailBoxException("Error disconnecting from email.", e);
        }
    }

    @Override
    public List<Message> retrieveMessages(String folderId) {
        try {
            Folder folder = store.getFolder(folderId);
            folder.open(READ_WRITE);
            return asList(folder.getMessages());
        } catch (MessagingException e) {
            throw new MailBoxException(String.format("Error retrieving messages from folder %s", folderId), e);
        }
    }

    @Override
    public void expungeFolder(String folderId) {
        try {
            Folder folder = store.getFolder(folderId);
            folder.open(READ_WRITE);
            folder.close(true);
        } catch (MessagingException e) {
            throw new MailBoxException(String.format("Error expunging folder %s", folderId), e);
        }
    }

    public static String contentFrom(Message message) {
        try {

            Object content = message.getContent();
            if (content instanceof String) {
                return content.toString();
            } else if (content instanceof Multipart) {
                StringBuilder sb = new StringBuilder(1024);
                Multipart multipartContent = (Multipart) content;

                for (int i = 0; i < multipartContent.getCount(); ++i) {
                    sb.append(multipartContent.getBodyPart(i).getContent());
                }

                return sb.toString();
            } else {
                throw new UnsupportedOperationException(String.format("Content type %s not supported", message.getContentType()));
            }

        } catch (IOException | MessagingException e) {
            throw new MailBoxException("Error extracting content from message", e);
        }
    }
}
