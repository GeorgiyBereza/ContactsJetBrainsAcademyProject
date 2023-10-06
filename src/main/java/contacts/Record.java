package contacts;

import java.time.LocalDateTime;

public class Record {
    private String name;
    private String phoneNumber;
    private LocalDateTime created;
    private LocalDateTime lastEdited;
    private int index;

    Record(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.created = LocalDateTime.now();
        this.lastEdited = LocalDateTime.now();
        this.index = DataBaseUtils.getMaxIndex();
    }

    public String getField(String field) {
        switch (field) {
            case "surname":
                return ((Person) this).getSurname();
            case "birthDate":
                return ((Person) this).getBirthDate();
            case "gender":
                return ((Person) this).getGender();
            case "address":
                return ((Organization) this).getAddress();
        }
        return null;
    }

    public void setField(String field, String value) {
        switch (field) {
            case "surname":
                ((Person) this).setSurname(value);
                break;
            case "birthDate":
                ((Person) this).setBirthDate(value);
                break;
            case "gender":
                ((Person) this).setGender(value);
                break;
            case "address":
                ((Organization) this).setAddress(value);
                break;
        }
    }

    public String getFullName() {
        if (this instanceof Person) {
            return this.getName() + " " + ((Person) this).getSurname();
        } else {
            return this.getName();
        }
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCreated(String dTime) {
        created = LocalDateTime.parse(dTime);
    }

    public void setLastEdited() {
        lastEdited = LocalDateTime.now();
    }

    public void setLastEdited(String dTime) {
        lastEdited = LocalDateTime.parse(dTime);
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCreated() {
        return created.withSecond(0).withNano(0).toString();
    }

    public String getLastEdited() {
        return lastEdited.withSecond(0).withNano(0).toString();
    }

}