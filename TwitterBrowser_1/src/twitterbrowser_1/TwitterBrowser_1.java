package twitterbrowser_1;

import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterBrowser_1{
    
    
    static String TweetText=null;
    static String TweetUser=null;
    static int likes=0;
		        
    
    public static void main(String[] args) throws TwitterException {
        ConfigurationBuilder cb = new ConfigurationBuilder();
	cb.setDebugEnabled(true)
	.setOAuthConsumerKey("***************************")//Ingresa tu ConsumerKey
	.setOAuthConsumerSecret("*******************")//Ingresa tu ConsumerSecret 
        .setOAuthAccessToken("*******************************")//Ingresa tu AccessToken
        .setOAuthAccessTokenSecret("******************************");//Ingresa tu AccessTokenSecret
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        
        List<Status> statuses = twitter.getUserTimeline("udistrital", new Paging(1, 40));
        for (Status status : statuses) {
	if(status.getFavoriteCount()>likes) {
            TweetText = status.getText();
            TweetUser= status.getUser().getName();
            likes=status.getFavoriteCount();
            }
	}
        try {
            EnviarCorreo();
        } catch (AddressException ex) {
            Logger.getLogger(TwitterBrowser_1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void EnviarCorreo() throws AddressException{
        Properties propiedad = new Properties();
        propiedad.setProperty("mail.smtp.host", "smtp.gmail.com");
        propiedad.setProperty("mail.smtp.starttls.enable", "true");
        propiedad.setProperty("mail.smtp.port", "587");
        propiedad.setProperty("mail.smtp.auth", "true");
        
        Session sesion = Session.getDefaultInstance(propiedad);
        String correoEnvia = "***********";//pon tu correo
        String contrasena = "********************";//pon tu contraseña
        String destinatario = JOptionPane.showInputDialog("Ingresa tu correo electronico: ");//ingreso del correo destinado
        String asunto = "Este es el tweet de la universidad distrital con más likes :v";
        String mensaje="Hola Cristian el tweet con más likes es: "+TweetUser+" : "+TweetText+" Likes: "+likes;
        MimeMessage mail = new MimeMessage(sesion);
        try {
            mail.setFrom(new InternetAddress(correoEnvia));
            mail.addRecipient(Message.RecipientType.TO, new InternetAddress(destinatario));
            mail.setSubject(asunto);
            mail.setText(mensaje);
            
            Transport transporte = sesion.getTransport("smtp");
            transporte.connect(correoEnvia,contrasena);
            transporte.sendMessage(mail, mail.getRecipients(Message.RecipientType.TO));
            transporte.close();
            
            JOptionPane.showMessageDialog(null, "Listo, revise su correo");
            
        } catch (MessagingException ex) {
            Logger.getLogger(TwitterBrowser_1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
