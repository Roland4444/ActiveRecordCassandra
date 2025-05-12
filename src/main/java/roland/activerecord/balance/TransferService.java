package roland.activerecord.balance;

import org.springframework.stereotype.Service;
import roland.activerecord.utils.Callback;
import roland.activerecord.jpa.User;

import java.math.BigDecimal;
import java.util.Arrays;
import static java.util.Objects.isNull;
import static roland.activerecord.balance.TransferService.MONEY_TRANSFER_RESULT.*;

@Service
public class TransferService {
    public MONEY_TRANSFER_RESULT transferMoney(User entityFrom, User entityTo, BigDecimal amount, Callback toSaveAction){
        if (isNull(entityFrom)) return BAD_SENDER;
        if (isNull(entityTo)) return BAD_RECEIVER;

        var currentBalanceSender = entityFrom.getCurrentBalance();
        var currentBalanceReceiver = entityTo.getCurrentBalance();

        if (currentBalanceSender.compareTo(amount) < 0) return UNSUFFICIENT_FUNDS;
        currentBalanceSender = currentBalanceSender.subtract(amount);
        entityFrom.setCurrentBalance(currentBalanceSender);
        entityTo.setCurrentBalance(currentBalanceReceiver.add(amount));
        if (!isNull(toSaveAction))
            toSaveAction.actionSave(Arrays.asList(entityFrom, entityTo));
        return SUCCESS;
    };

    public enum MONEY_TRANSFER_RESULT{
        UNSUFFICIENT_FUNDS,
        BAD_RECEIVER,
        BAD_SENDER,
        ERROR_PROCESS,
        SUCCESS
    }
}
