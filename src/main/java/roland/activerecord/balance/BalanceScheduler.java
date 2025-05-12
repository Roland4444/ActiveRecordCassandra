package roland.activerecord.balance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BalanceScheduler {

    @Autowired
    private BalanceService balanceService;

    @Scheduled(fixedRate = 30000) // запуск каждые 30 секунд
    public void updateBalances() {
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11");
        System.out.println("!!    I AM RUNNING!!!!!!!!!             !11");
        System.out.println("!!                                      !11");
        System.out.println("!!!                                      11");
        System.out.println("!!!                                      11");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!11");

        balanceService.increaseBalances();
    }
}