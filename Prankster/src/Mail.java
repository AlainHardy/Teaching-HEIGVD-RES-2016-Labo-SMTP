import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Author: Alain Hardy
 * Created on: 15.04.2016
 *
 * Define the subject and the content of a mail.
 * The first line is the subject. The rest of the file is considered has
 * the content.
 */
public class Mail {
    private String subject = "";
    private String content = "";

    public Mail(String name) {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(SmptSender.MAILS_DIRECTORY +name));

            // The first line of the file has to be the subject ...
            subject = br.readLine();


            // ... the rest is the content of the mail.
            content = "";
            String currentLine;
            while((currentLine = br.readLine()) != null) {
                content += currentLine+"\n";
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }
}
