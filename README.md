# ContactsJetBrainsAcademyProject (+SQLite JDBC)
Contacts - Project for Hyperskill JetBrains Academy java developer course
Tasks:
1. Create a menu which allows to list, add, remove and update contacts
2. App should be able to work with 2 types of contacts:
Person (Name, Surname, Birth date, Gender, Number, Time created, Time last edit) and Organization (Organization name, Address, Number, Time created, Time last edit)
3. Add validation to user's input
4. Implement search functionality

- used inheritance and polymorphism for different types of contacts
- regex for search and validation of information (class ValUtils)
- instead of simply writing to the file used SQLite JDBC library to practice connecting to databases and working with SQL queries(class DataBaseUtils)
