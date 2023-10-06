package contacts;

public class Main {
    public static void main(String[] args) {
        ContactsApp app = new ContactsApp(getPath(args));
        app.run();
    }

    private static String getPath(String[] args) {
        if (args.length != 0) {
            return args[1];
        }
        return "contacts.db";
    }
}
