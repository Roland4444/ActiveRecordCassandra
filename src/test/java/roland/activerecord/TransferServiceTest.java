package roland.activerecord;

import org.junit.jupiter.api.Test;
import roland.activerecord.balance.TransferService;
import roland.activerecord.jpa.User;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransferServiceTest {

    @Test
    void transferMoney() {
        var entity1 = new User();
        entity1.setAccount("ru1234555");
        entity1.setName("Иван Сидоров");
        entity1.setCurrentBalance(new BigDecimal(12000));
        entity1.setInitialBalance(new BigDecimal(12000));

        var entity2 = new User();
        entity2.setAccount("ru1234556");
        entity2.setName("Виктор Петров");
        entity2.setCurrentBalance(new BigDecimal(15000));
        entity2.setInitialBalance(new BigDecimal(15000));

        var transferService = new TransferService();
        assertEquals(transferService.transferMoney(entity1, entity2, new BigDecimal(70000), null),
                TransferService.MONEY_TRANSFER_RESULT.UNSUFFICIENT_FUNDS);
        assertEquals(transferService.transferMoney(entity1, entity2, new BigDecimal(500), null),
                TransferService.MONEY_TRANSFER_RESULT.SUCCESS);
        assertEquals(new BigDecimal(11500), entity1.getCurrentBalance());
        assertEquals(new BigDecimal(15500), entity2.getCurrentBalance());

        var bd = new BigDecimal("20");
        bd=bd.subtract(BigDecimal.ONE);
        assertEquals(bd.toString(),"19");
    }
}