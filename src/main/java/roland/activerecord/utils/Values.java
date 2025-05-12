package roland.activerecord.utils;

import java.util.regex.Pattern;

public class Values {

    public static boolean isEmail(String input){
        return Pattern.compile("^(.+)@(\\S+)$").matcher(input).matches();
    }

    public static boolean isUUID(String input){
        return Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\n").matcher(input).matches();
    }

    public static boolean isTelephoneNumber(String input){
        for (int i = 0; i < input.length(); i++)
            if (!isNumber(input.charAt(i)))
                return false;
        return true;
    };

    public static boolean isNumber(char input) {
        switch (input) {
            case '0':
                return true;
            case '1':
                return true;
            case '2':
                return true;
            case '3':
                return true;
            case '4':
                return true;
            case '5':
                return true;
            case '6':
                return true;
            case '7':
                return true;
            case '8':
                return true;
            case '9':
                return true;
        }
        return false;
    }
    public enum OPERATION_PERFORM_ITEM {
        ITEM_EXIST_IN_OTHER,
        SUCCESS,
        BAD_VALUE_TO_UPDATE,
        UKNOWN_INPUT_VALUES,
        CANT_DELETE_LAST_ELEM,
        MODIFYING_PARAM_NOT_FOUND
    }
}
