package it.daaar.learning.email;

import javax.mail.*;
import java.util.List;
import java.util.Properties;

import static java.util.Arrays.asList;
import static javax.mail.Folder.READ_WRITE;
import static javax.mail.Session.getInstance;

public class MailBoxIMAP implements MailBox {

    private final Store store;

    public MailBoxIMAP() {
        Properties properties = getProperties();
        Session session = getInstance(properties);

        try {
            store = session.getStore("imap");
        } catch (NoSuchProviderException e) {
            throw new MailBoxException("Error getting imap store", e);
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
    public void connect(String url, String username, String password) {
        try {
            store.connect(url, username, password);
        } catch (MessagingException e) {
            throw new MailBoxException("Error connecting to the email box.", e);
        }
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

}
