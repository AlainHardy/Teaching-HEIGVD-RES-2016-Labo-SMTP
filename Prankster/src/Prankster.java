import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Author: Alain Hardy
 * Created on: 19.04.2016
 *
 * Create, if possible, the number of group asked by the user, choose randomly a sender
 * among them, and chose a content in a list of existing files.
 * Them send the mail to all users, except the sender.
 * The user must provide an positive integer as an argument for the number of groups
 * to form.
 */
public final class Prankster {

    public static void main(String ... args) {
        try{
            if(args.length > 1)
                throw new Exception("Too many arguments.");

            BufferedReader br = null;
            int nbrGroups = Integer.parseInt(args[0]);

            if(nbrGroups < 0)
                throw new Exception("Incorrect value.");

            br = new BufferedReader(new FileReader(SmptSender.ADRESSES_DIRECTORY + "addressesList.txt"));

            List<User> listOfUsers = new ArrayList<User>();

            String mailString = "";
            while( (mailString = br.readLine()) != null) {
                listOfUsers.add(new User(mailString));
            }

            if(listOfUsers.size() < 3)
                throw new Exception("The file must have at least 3 addresses.");

            // If the number of groups asked by the user does not allow to have at least 3 persons in each groups,
            // it will be reduced by 1 until the previous condition is respected.
            while(listOfUsers.size() / nbrGroups < 3) {
                nbrGroups--;
            }

            List<List<User>> listOfGroups = new ArrayList<List<User>>();
            for(int i = 0; i < nbrGroups; i++){
                listOfGroups.add(new ArrayList<User>());
            }

            // Place each user in one of the available groups
            {
                int i = 0;
                for (User u : listOfUsers) {
                    listOfGroups.get(i % nbrGroups).add(listOfUsers.get(i));
                    i++;
                }
            }

            // Get all possibles files of mails
            File f = new File(SmptSender.MAILS_DIRECTORY);
            List<File> listOfMail = new ArrayList<File>();
            for(File fs : f.listFiles()) {
                if(fs.isFile())
                    listOfMail.add(fs);
            }

            Random randomGenerator = new Random();
            for(int i = 0; i < nbrGroups; i++) {
                int index = randomGenerator.nextInt(listOfGroups.get(i).size());
                // Choose one of the user in the list as the sender of the mail ...
                User tempUser = listOfGroups.get(i).get(index);
                // ... and then remove him/her from the list of receivers.
                listOfGroups.get(i).remove(index);

                // Choose randomly a file among those present
                index = randomGenerator.nextInt(listOfMail.size());

                SmptSender.sendMail(new Group(tempUser, listOfGroups.get(i)), new Mail(listOfMail.get(index).getName()));
            }

        }catch(IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
