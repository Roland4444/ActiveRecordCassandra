package roland.activerecord;

import org.junit.jupiter.api.Test;
import roland.activerecord.jpa.User;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static roland.activerecord.jpa.User.*;

class UserTest {

    @Test
    void filtersTest() throws ParseException {
        var date1 = new SimpleDateFormat("dd.MM.yyyy").parse("11.07.1987");
        var date2 = new SimpleDateFormat("dd.MM.yyyy").parse("23.01.1991");
        var entity1 = new User();
        entity1.setAccount("ru1234555");
        entity1.setEmails(new HashSet<>(List.of("a1a2a3@yandex.ru")));
        entity1.setPhones(new HashSet<>(List.of("79071113434")));
        entity1.setDateOfBirth(date1);
        entity1.setName("Иван Сидоров");
        entity1.setCurrentBalance(new BigDecimal(12000));
        entity1.setInitialBalance(new BigDecimal(12000));

        var entity2 = new User();
        entity2.setAccount("ru1234556");
        entity2.setEmails(new HashSet<>(List.of("b1b2b3@yandex.ru")));
        entity2.setPhones(new HashSet<>(List.of("79071114444")));
        entity2.setDateOfBirth(date2);
        entity2.setName("Виктор Петров");
        entity2.setCurrentBalance(new BigDecimal(15000));
        entity2.setInitialBalance(new BigDecimal(15000));

        var userArr = Arrays.asList(entity1, entity2);

        var item = filterByName(userArr, "Сидоров");
        assertEquals(item.get(0), entity1);
        assertEquals(item.size(), 1);

        item = filterByEmail(userArr, "b1b2b3@yandex.ru");
        assertEquals(item.get(0), entity2);
        assertEquals(item.size(), 1);

        item = filterByPhone(userArr, "79071113434");
        assertEquals(item.get(0), entity1);
        assertEquals(item.size(), 1);

        item = filterByDate(userArr, new SimpleDateFormat("dd.MM.yyyy").parse("23.01.1990"));
        assertEquals(item.get(0), entity2);
        assertEquals(item.size(), 1);


        ///assertEquals(item, entity2);


    }
}