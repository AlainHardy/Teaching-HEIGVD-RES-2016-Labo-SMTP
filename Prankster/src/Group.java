import java.util.List;

/**
 * Author: Alain Hardy
 * Created on: 15.04.2016
 *
 * Contain the user defined to send the mail, and the list of persons that will
 * receive the mail.
 */
public class Group {
    private User sender;
    private List<User> receivers;


    public Group(User s, List<User> r) {
        sender = s;
        receivers = r;
    }

    public User getSender() {
        return sender;
    }
    public List<User> getReceivers() {
        return receivers;
    }
}
