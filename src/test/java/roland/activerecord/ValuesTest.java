package roland.activerecord;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static roland.activerecord.utils.Values.isEmail;
import static roland.activerecord.utils.Values.isTelephoneNumber;

class ValuesTest {

    @Test
    void isCorrectEmail() {
        var correctEmail = "froger@yandex.ru";
        var incorrectEmail = "sfdfssdfsdf.jjdjjdj";
        assertEquals(true, isEmail(correctEmail));
        assertEquals(false, isEmail(incorrectEmail));

    }

    @Test
    void isTelNumber() {
        var correctNumber = "79554412255";
        var incorrectNumber = "795544@@@12255";
        assertEquals(true, isTelephoneNumber(correctNumber));
        assertEquals(false, isTelephoneNumber(incorrectNumber));
    }
}