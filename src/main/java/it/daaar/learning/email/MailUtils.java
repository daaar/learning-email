package it.daaar.learning.email;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import java.io.IOException;

public class MailUtils {

    private MailUtils() {
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
