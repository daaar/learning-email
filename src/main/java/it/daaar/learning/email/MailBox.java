package it.daaar.learning.email;

import javax.mail.Message;
import java.util.List;

public interface MailBox {
    void connect(String url, String username, String password);
    void disconnect();
    List<Message> retrieveMessages(String folderId);
    void expungeFolder(String folderId);
}
