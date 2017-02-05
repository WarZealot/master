package mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailTest {

    public static void main(String[] args) {
        // Set up the SMTP server.
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.myisp.com");
        Session session = Session.getDefaultInstance(props, null);

        // Construct the message
        String to = "tkachuk9292@yahoo.de";
        String from = "me@me.com";
        String subject = "Hello";
        Message msg = new MimeMessage(session);
        try {
            msg.setFrom(new InternetAddress(from));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
            msg.setSubject(subject);
            msg.setText("Hi,\n\nHow are you?");

            // Send the message.
            Transport.send(msg);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
