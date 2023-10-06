package contacts;

import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class DataBaseUtils {
    private static final SQLiteDataSource dataSource = new SQLiteDataSource();

    DataBaseUtils(String filePath) {
        dataSource.setUrl("jdbc:sqlite:c:\\SQLite\\DataBases\\".concat(filePath));
    }

    public void createTables() {
        try (Connection con = dataSource.getConnection()) {
            try (Statement statement = con.createStatement()) {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS persons(" +
                        "id INTEGER UNIQUE NOT NULL," +
                        "name TEXT NOT NULL," +
                        "surname TEXT NOT NULL," +
                        "birth_date TEXT NOT NULL," +
                        "gender TEXT NOT NULL," +
                        "number TEXT NOT NULL," +
                        "time_created TEXT NOT NULL," +
                        "time_edited TEXT NOT NULL)");

                statement.executeUpdate("CREATE TABLE IF NOT EXISTS organizations(" +
                        "id INTEGER UNIQUE NOT NULL," +
                        "name TEXT NOT NULL," +
                        "address TEXT NOT NULL," +
                        "number TEXT NOT NULL," +
                        "time_created TEXT NOT NULL," +
                        "time_edited TEXT NOT NULL)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getMaxIndex() {
        int max = 0;
        try (Connection con = dataSource.getConnection()) {
            try (ResultSet rSet = con.createStatement().executeQuery("SELECT MAX(id) AS maxId FROM persons")) {
                max = Math.max(rSet.getInt("maxId"), max);
            }
            try (ResultSet rSet = con.createStatement().executeQuery("SELECT MAX(id) AS maxId FROM organizations")) {
                max = Math.max(rSet.getInt("maxId"), max);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return max + 1;
    }

    public void createNewContact(Record record) {
        String personQuery = "INSERT INTO persons VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String orgQuery = "INSERT INTO organizations VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = dataSource.getConnection()) {
            if (record instanceof Person) {
                try (PreparedStatement pStat = con.prepareStatement(personQuery)) {
                    pStat.setInt(1, record.getIndex());
                    pStat.setString(2, record.getName());
                    pStat.setString(3, record.getField("surname"));
                    pStat.setString(4, record.getField("birthDate"));
                    pStat.setString(5, record.getField("gender"));
                    pStat.setString(6, record.getPhoneNumber());
                    pStat.setString(7, record.getCreated());
                    pStat.setString(8, record.getLastEdited());
                    pStat.execute();
                }
            } else {
                try (PreparedStatement pStat = con.prepareStatement(orgQuery)) {
                    pStat.setInt(1, record.getIndex());
                    pStat.setString(2, record.getName());
                    pStat.setString(3, record.getField("address"));
                    pStat.setString(4, record.getPhoneNumber());
                    pStat.setString(5, record.getCreated());
                    pStat.setString(6, record.getLastEdited());
                    pStat.execute();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateRecord(Record record, String field, String data) {
        String updateRecord = String.format("UPDATE %s SET %s = '%s' WHERE id = %d",
                record instanceof Person ? "persons" : "organizations", field, data, record.getIndex());

        try (Connection con = dataSource.getConnection()) {
            try (Statement pStat = con.createStatement()) {
                pStat.executeUpdate(updateRecord);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeRecord(Record record) {
        String removeRecord = String.format("DELETE FROM %s WHERE id = %d",
                record instanceof Person ? "persons" : "organizations", record.getIndex());

        try (Connection con = dataSource.getConnection()) {
            try (Statement stmnt = con.createStatement()) {
                stmnt.executeUpdate(removeRecord);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // search sql query
    public List<Integer> searchRecord(String word) {
        List<Integer> indices = new ArrayList<>();
        String searchPersons = String.format("Select id FROM persons " +
                "WHERE LOWER (name) LIKE '%%%1$s%%'" +
                "OR LOWER (surname) LIKE '%%%1$s%%'" +
                "OR LOWER (birth_date) LIKE '%%%1$s%%'" +
                "OR LOWER (gender) LIKE '%%%s%%'" +
                "OR LOWER (number) LIKE '%%%1$s%%'" +
                "OR LOWER (time_created) LIKE '%%%1$s%%'" +
                "OR LOWER (time_edited) LIKE '%%%1$s%%'", word);
        String searchOrgs = String.format("Select id FROM organizations " +
                "WHERE LOWER (name) LIKE '%%%1$s%%'" +
                "OR LOWER (address) LIKE '%%%s%%'" +
                "OR LOWER (number) LIKE '%%%1$s%%'" +
                "OR LOWER (time_created) LIKE '%%%1$s%%'" +
                "OR LOWER (time_edited) LIKE '%%%1$s%%'", word);
        try (Connection con = dataSource.getConnection()) {
            try (Statement stmnt = con.createStatement()) {
                try (ResultSet rSet = stmnt.executeQuery(searchPersons)) {
                    while (rSet.next()) {
                        indices.add(rSet.getInt("id"));
                    }
                }
                try (ResultSet rSet = stmnt.executeQuery(searchOrgs)) {
                    while (rSet.next()) {
                        indices.add(rSet.getInt("id"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return indices;
    }

    public TreeMap<Integer, Record> getContacts() {
        TreeMap<Integer, Record> contacts = new TreeMap<>();
        try (Connection con = dataSource.getConnection()) {
            try (ResultSet persons = con.createStatement().executeQuery("SELECT * FROM persons")) {
                while (persons.next()) {
                    Record currentRecord = new Person(persons.getString("name"),
                            persons.getString("surname"),
                            persons.getString("birth_date"),
                            persons.getString("gender"),
                            persons.getString("number"));
                    currentRecord.setIndex(persons.getInt("id"));
                    currentRecord.setCreated(persons.getString("time_created"));
                    currentRecord.setLastEdited(persons.getString("time_edited"));
                    contacts.put(persons.getInt("id"), currentRecord);
                }
            }
            try (ResultSet organizations = con.createStatement().executeQuery("SELECT * FROM organizations")) {
                while (organizations.next()) {
                    Record currentRecord = new Organization(organizations.getString("name"),
                            organizations.getString("address"),
                            organizations.getString("number"));
                    currentRecord.setIndex(organizations.getInt("id"));
                    currentRecord.setCreated(organizations.getString("time_created"));
                    currentRecord.setLastEdited(organizations.getString("time_edited"));
                    contacts.put(organizations.getInt("id"), currentRecord);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contacts;
    }
}