import java.io.*;
import java.net.Socket;
import java.util.Properties;

/**
 * Author: Alain Hardy
 * Created on: 15.04.2016
 *
 * Communicate with a smtp server to create a mail.
 */
public final class SmptSender {
    public final static String ADRESSES_DIRECTORY = "addresses/";
    public final static String MAILS_DIRECTORY = "mails/";

    public static void sendMail(Group g, Mail m) {

        Socket socket = null;
        PrintWriter out;
        BufferedReader in;

        Properties config = new Properties();

        InputStream is = null;

        try {
            is = new FileInputStream("config.properties");

            // Load configuration file
            config.load(is);

            // Connection to the server
            socket = new Socket(config.getProperty("host"), Integer.parseInt(config.getProperty("port")));

            // Extraction of streams
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            in.readLine();

            out.println("EHLO prankster");
            String s;
            while (true) {
                s = in.readLine();
                if (s.contains("250 ")) // If the server has finished to send the header
                    break;
            }

            String mailFrom = "MAIL FROM: " + g.getSender().getMailAddress();
            out.println(mailFrom);

            in.readLine();
            String rcptTo = "";
            for (User u : g.getReceivers()) {
                rcptTo = "RCPT TO:" + u.getMailAddress();
                out.println(rcptTo);
                in.readLine();
            }

            out.println("DATA");
            in.readLine();

            String from = "From:" + g.getSender().getMailAddress();
            out.println(from);

            int i = 0;
            String to = "To:";
            for (User u : g.getReceivers()) {
                to += u.getMailAddress();
                if (++i < g.getReceivers().size())
                    to += ",";
            }
            out.println(to);

            String subject = "Subject:" + m.getSubject();
            out.println(subject);

            out.println();

            String content = m.getContent();
            out.println(content);
            out.println(".");
            in.readLine();

            out.close();
            in.close();

            socket.close();
        }catch(IOException e){
            e.printStackTrace();
        }

    }
}
