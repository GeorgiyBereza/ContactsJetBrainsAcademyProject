package contacts;

import java.io.File;
import java.util.*;

public class ContactsApp {
    private static final Scanner sc = new Scanner(System.in);
    private final Map<Integer, Record> contacts;
    private final Menu menu = new Menu(this);
    private final DataBaseUtils dbUtils;
    private final String filePath;

    ContactsApp(String filePath) {
        this.filePath = filePath;
        dbUtils = new DataBaseUtils(filePath);
        dbUtils.createTables();
        contacts = dbUtils.getContacts();
    }

    void run() {
        menu.showMainMenu();
    }

    Record getContact(int index) {
        return contacts.get(index);
    }

    void countContacts() {
        System.out.printf("The Phone Book has %d records.\n", contacts.size());
    }

    void listContacts() {
        contacts.forEach((k, v) -> System.out.printf("%d. %s\n", k, v.getFullName()));
    }

    void removeContact(Record currentRecord) {
        dbUtils.removeRecord(currentRecord);
        contacts.remove(currentRecord.getIndex());
        System.out.println("The record removed!");
    }

    void addContact(String name, String surname, String birthDate, String gender, String phoneNumber) {
        Record currentRecord = new Person(name, surname, birthDate, gender, phoneNumber);
        contacts.put(DataBaseUtils.getMaxIndex(), currentRecord);
        dbUtils.createNewContact(currentRecord);
        System.out.println("The record created.");
    }

    void addContact(String name, String address, String phoneNumber) {
        Record currentRecord = new Organization(name, address, phoneNumber);
        contacts.put(DataBaseUtils.getMaxIndex(), currentRecord);
        dbUtils.createNewContact(currentRecord);
        System.out.println("The record created.");
    }

    Map<Integer, Record> searchContacts(String searchQuery) {
        Map<Integer, Record> searchResult = new TreeMap<>();
        List<Integer> indices = dbUtils.searchRecord(searchQuery);
        indices.forEach(index -> {
            Record currentRecord = contacts.get(index);
            searchResult.put(indices.indexOf(index) + 1, currentRecord);
        });
        return searchResult;
    }

    void showInfo(int index) {
        Record currentRecord = contacts.get(index);
        if (currentRecord instanceof Person) {
            System.out.printf("Name: %s\n", currentRecord.getName());
            System.out.printf("Surname: %s\n", currentRecord.getField("surname"));
            System.out.printf("Birth date: %s\n", currentRecord.getField("birthDate"));
            System.out.printf("Gender: %s\n", currentRecord.getField("gender"));
        } else {
            System.out.printf("Organization name: %s\n", currentRecord.getName());
            System.out.printf("Address: %s\n", currentRecord.getField("address"));
        }
        System.out.printf("Number: %s\n", currentRecord.getPhoneNumber());
        System.out.printf("Time created: %s\n", currentRecord.getCreated());
        System.out.printf("Time last edit: %s\n", currentRecord.getLastEdited());
    }


    void editContact(int index, String field) {
        Record record = contacts.get(index);
        switch (field) {
            case "name":
                System.out.print("Enter name: ");
                record.setName(sc.nextLine());
                dbUtils.updateRecord(record, "name", record.getName());
                break;
            case "surname":
                System.out.print("Enter surname: ");
                record.setField("surname", sc.nextLine());
                dbUtils.updateRecord(record, "surname", record.getField("surname"));
                break;
            case "birth":
                System.out.print("Enter birth date: ");
                record.setField("birthDate", ValUtils.validateBirthDate(sc.nextLine()));
                dbUtils.updateRecord(record, "birth_date",record.getField("birthDate"));
                break;
            case "gender":
                System.out.print("Enter gender: ");
                record.setField("gender", ValUtils.validateGender(sc.nextLine()));
                dbUtils.updateRecord(record, "gender", record.getField("gender"));
                break;
            case "address":
                System.out.print("Enter address: ");
                record.setField("address", sc.nextLine());
                dbUtils.updateRecord(record, "address", record.getField("address"));
                break;
            case "number":
                System.out.print("Enter number: ");
                record.setPhoneNumber(ValUtils.validateNumber(sc.nextLine()));
                dbUtils.updateRecord(record, "number", record.getPhoneNumber());
                break;
        }
        record.setLastEdited();
        dbUtils.updateRecord(record, "time_edited", record.getLastEdited());
        System.out.println("Saved");
        showInfo(record.getIndex());
    }
}