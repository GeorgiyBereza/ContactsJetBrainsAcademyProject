package contacts;

import java.util.Map;
import java.util.Scanner;

public class Menu {
    private static final Scanner sc = new Scanner(System.in);
    private final ContactsApp app;

    Menu(ContactsApp app) {
        this.app = app;
    }

    void showMainMenu() {
        while (true) {
            System.out.print("\n[menu] Enter action (add, list, search, count, exit): ");
            switch (sc.nextLine()) {
                case "add":
                    showAddContactMenu();
                    break;
                case "list":
                    showListMenu();
                    break;
                case "search":
                    showSearchMenu();
                    break;
                case "count":
                    app.countContacts();
                    break;
                case "exit":
                    System.exit(1);
                default:
                    System.out.println("Try again.");
            }
        }
    }

    private void showAddContactMenu() {
        System.out.print("Enter the type (person, organization): ");
        String type = sc.nextLine();
        if (type.equals("person")) {
            System.out.print("Enter the name: ");
            String name = sc.nextLine();
            System.out.print("Enter the surname: ");
            String surname = sc.nextLine();
            System.out.print("Enter the birth date: ");
            String birthDate = ValUtils.validateBirthDate(sc.nextLine());
            System.out.print("Enter the gender (M, F): ");
            String gender = ValUtils.validateGender(sc.nextLine());
            System.out.print("Enter the number: ");
            String phoneNumber = ValUtils.validateNumber(sc.nextLine());
            app.addContact(name, surname, birthDate, gender, phoneNumber);
        } else {
            System.out.print("Enter the organization name: ");
            String name = sc.nextLine();
            System.out.print("Enter the address: ");
            String address = sc.nextLine();
            System.out.print("Enter the number: ");
            String phoneNumber = ValUtils.validateNumber(sc.nextLine());
            app.addContact(name, address, phoneNumber);
        }
    }

    private void showSearchMenu() {
        Map<Integer, Record> searchResult;
        System.out.print("Enter search query: ");
        String searchQuery = sc.nextLine();
        searchResult = app.searchContacts(searchQuery);
        System.out.printf("Found %d results:\n", searchResult.size());

        searchResult.forEach((k, v) ->  System.out.printf("%d. %s\n", k, v.getFullName()));

        System.out.print("\n[search] Enter action ([number], back, again): ");
        String action = sc.nextLine();
        switch (action) {
            case "back":
                showMainMenu();
                break;
            case "again":
                showSearchMenu();
                break;
            default:
                int index = Integer.parseInt(action);
                app.showInfo(searchResult.get(index).getIndex());
                shorRecordMenu(searchResult.get(index));
                break;
        }
    }

    private void shorRecordMenu(Record currentRecord) {
        System.out.print("\n[record] Enter action (edit, delete, menu): ");
        switch (sc.nextLine()) {
            case "edit":
                showEditMenu(currentRecord);
                break;
            case "delete":
                app.removeContact(currentRecord);
                showMainMenu();
                break;
            case "menu":
                showMainMenu();
                break;
        }
    }

    private void showEditMenu(Record currentRecord) {
        if (currentRecord instanceof Person) {
            System.out.print("Select a field (name, surname, birth, gender, number): ");
        } else {
            System.out.print("Select a field (name, address, number): ");
        }
        String field = sc.nextLine();
        app.editContact(currentRecord.getIndex(), field);
        shorRecordMenu(currentRecord);
    }

    private void showListMenu() {
        app.listContacts();
        System.out.print("\n[list] Enter action ([number], back): ");
        String action = sc.nextLine();
        if ("back".equals(action)) {
            showMainMenu();
        } else {
            int index = Integer.parseInt(action);
            app.showInfo(index);
            shorRecordMenu(app.getContact(index));
        }
    }
}