package roland.activerecord.balance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roland.activerecord.jpa.User;
import roland.activerecord.jpa.UserRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BalanceService {

    @Autowired
    private UserRepository userRepository;

    private static final BigDecimal INCREASE_MULT = new BigDecimal("1.1");
    private static final BigDecimal MAX_INCREASE_COEF = new BigDecimal("2.07");

    @Transactional
    public void increaseBalances() {
        List<User> users = userRepository.findAll();

        for (var user : users) {
            var currentBalance = user.getCurrentBalance();
            var initialDeposit = user.getInitialBalance();
            BigDecimal maxBalance = initialDeposit.multiply(MAX_INCREASE_COEF);
            if (currentBalance.compareTo(maxBalance) > 0)
                continue;
            var increasedBalance = currentBalance.multiply(INCREASE_MULT);
            if (increasedBalance.compareTo(maxBalance) > 0)
                increasedBalance = maxBalance;
            user.setCurrentBalance(increasedBalance);
        }

        userRepository.saveAll(users);
    }
}