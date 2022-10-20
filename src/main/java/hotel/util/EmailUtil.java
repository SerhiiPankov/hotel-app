package hotel.util;

import hotel.exception.EmailConnectionException;
import hotel.model.Request;
import hotel.model.User;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.Authenticator;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;

public class EmailUtil implements Constant {
    private static final String CHARACTER_ENCODING_UTF_8 = "UTF-8";
    private static final String CHARACTER_ENCODING_CONTENT = "text/html;charset=UTF-8";
    private static final String USER_NAME_PROPERTIES = "mail.smtp.user";
    private static final String PASSWORD_PROPERTIES = "mail.smtp.password";
    private static final String RESOURCE_BUNDLE_INVOICE_SUBJECT = "invoice.email.subject";
    private static final String RESOURCE_BUNDLE_INVOICE_TEXT_FIRST =
            "invoice.email.text.first";
    private static final String RESOURCE_BUNDLE_INVOICE_TEXT_SECOND =
            "invoice.email.text.second";
    private static final String RESOURCE_BUNDLE_NOT_BOOKING_SUBJECT =
            "not.booking.email.subject";
    private static final String RESOURCE_BUNDLE_NOT_BOOKING_TEXT_FIRST =
            "not.booking.email.text.first";
    private static final String RESOURCE_BUNDLE_NOT_BOOKING_TEXT_SECOND =
            "not.booking.email.text.second";
    private static final String RESOURCE_BUNDLE_NOT_BOOKING_TEXT_THIRD =
            "not.booking.email.text.third";
    private static final Properties MAIL_PROPERTIES = new Properties();
    private static final InternetAddress internetAddress;
    private static final Session session;

    static {
        try (InputStream inputStream = EmailUtil.class.getClassLoader()
                .getResourceAsStream(PATH_TO_MAIL_PROPERTIES)) {
            MAIL_PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new EmailConnectionException("Can't get data for Email connection");
        }
        try {
            internetAddress = new InternetAddress(
                    MAIL_PROPERTIES.getProperty(USER_NAME_PROPERTIES));
        } catch (AddressException e) {
            throw new EmailConnectionException("Can't get address for send Email");
        }
        session = Session.getInstance(MAIL_PROPERTIES,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                MAIL_PROPERTIES.getProperty(USER_NAME_PROPERTIES),
                                MAIL_PROPERTIES.getProperty(PASSWORD_PROPERTIES));
                    }
                });
    }

    private EmailUtil(){
    }

    public static void sendInvoiceEmail(File file, HttpServletRequest req, User user)
            throws MessagingException {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(SESSION_ATTRIBUTE_LANGUAGE,
                Locale.forLanguageTag(
                        (String) req.getSession().getAttribute(SESSION_ATTRIBUTE_LANGUAGE)));
        MimeMessage message = new MimeMessage(session);
        message.setFrom(internetAddress);
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(user.getEmail())
        );
        message.setSubject(resourceBundle.getString(RESOURCE_BUNDLE_INVOICE_SUBJECT),
                CHARACTER_ENCODING_UTF_8);
        BodyPart messageBodyPart = new MimeBodyPart();
        String content = resourceBundle.getString(RESOURCE_BUNDLE_INVOICE_TEXT_FIRST)
                + user.getName() + "!\n"
                + resourceBundle.getString(RESOURCE_BUNDLE_INVOICE_TEXT_SECOND);
        messageBodyPart.setContent(content, CHARACTER_ENCODING_CONTENT);
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        messageBodyPart = new MimeBodyPart();
        String filename = file.getName();
        DataSource source = new FileDataSource(filename);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(filename);
        multipart.addBodyPart(messageBodyPart);
        message.setContent(multipart);
        Transport.send(message);
    }

    public static void sendBookingNotPossibleEmail(HttpServletRequest req,
                                                   Request request, User user)
            throws MessagingException {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(SESSION_ATTRIBUTE_LANGUAGE,
                Locale.forLanguageTag(user.getLanguage()));
        MimeMessage message = new MimeMessage(session);
        message.setFrom(internetAddress);
        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(user.getEmail())
        );
        message.setSubject(resourceBundle.getString(RESOURCE_BUNDLE_NOT_BOOKING_SUBJECT),
                CHARACTER_ENCODING_UTF_8);
        BodyPart messageBodyPart = new MimeBodyPart();
        String content = resourceBundle.getString(RESOURCE_BUNDLE_NOT_BOOKING_TEXT_FIRST)
                + ", " + user.getName() + "!\n"
                + resourceBundle.getString(RESOURCE_BUNDLE_NOT_BOOKING_TEXT_SECOND)
                + request.getId() + " "
                + resourceBundle.getString(RESOURCE_BUNDLE_NOT_BOOKING_TEXT_THIRD);
        messageBodyPart.setContent(content,
                CHARACTER_ENCODING_CONTENT);
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        message.setContent(multipart);
        Transport.send(message);
    }
}
