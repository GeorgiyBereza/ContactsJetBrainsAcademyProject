package contacts;

public class ValUtils {
    private static final String GENDER_REGEX = "^[MF]$";
    private static final String BIRTHDATE_REGEX = "^\\d{4}-\\d{2}-\\d{2}$";
    private static final String PHONE_REGEX = "^[+]?((\\(([^\\W_])*\\))|([^\\W_])+)" +
            "([ -](\\(?([^\\W_]){2,}\\)?)|([^\\W_]){2,})*?";


    public static String validateGender(String gender) {
        if (!gender.matches(GENDER_REGEX)) {
            System.out.println("Bad gender!");
            return "[no data]";
        }
        return gender;
    }

    public static String validateBirthDate(String birthDate) {
        if (!birthDate.matches(BIRTHDATE_REGEX)) {
            System.out.println("Bad birth date!");
            return "[no data]";
        }
        return birthDate;
    }

    public static String validateNumber(String phoneNumber) {
        if (!phoneNumber.matches(PHONE_REGEX) || !bracketCounter(phoneNumber)) {
            System.out.println("Wrong number format!");
            return "[no number]";
        }
        return phoneNumber;
    }

    // checks if only one group of digits has brackets (or no brackets at all)
    private static boolean bracketCounter(String phoneNumber) {
        int openBracketCounter = 0;
        int closeBracketCounter = 0;
        for (char ch : phoneNumber.toCharArray()) {
            if (ch == '(') {
                openBracketCounter++;
            } else if (ch == ')') {
                closeBracketCounter++;
            }
        }
        return openBracketCounter == closeBracketCounter && closeBracketCounter <= 1;
    }
}