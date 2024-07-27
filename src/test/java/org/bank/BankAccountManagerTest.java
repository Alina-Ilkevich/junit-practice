package org.bank;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Random;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BankAccountManagerTest {
    @InjectMocks
    private BankAccountManager manager;
    @Mock
    private BankAccountHelper helper;

    @Test
    public void testTransferMoney() throws InvalidBankOperationException {
        BankAccount fromAccount = new BankAccount();
        BankAccount toAccount = new BankAccount();
        fromAccount.setAccountCurrency(BankAccountHelper.USD_ACCOUNT);
        toAccount.setAccountCurrency(BankAccountHelper.USD_ACCOUNT);
        fromAccount.setCurrentBalance(120);
        fromAccount.setDailyLimit(100);
        toAccount.setDailyLimit(100);
        double amount = 10;
        double expectedCurrentBalanceFromAccount = fromAccount.getCurrentBalance() - amount;
        double expectedDailyLimitFromAccount = fromAccount.getDailyLimit() - amount;
        double expectedDailyLimitToAccount = toAccount.getDailyLimit() - amount;

        when(helper.convertCurrency(fromAccount.getAccountCurrency(), toAccount.getAccountCurrency(), amount)).thenReturn(amount);
        when(helper.isBalanceValidForWithdraw(fromAccount, amount, fromAccount.getAccountCurrency())).thenReturn(true);
        when(helper.isAvailableForDailyWithdraw(fromAccount, amount)).thenReturn(true);

        manager.transferMoney(fromAccount, toAccount, amount);

        assertEquals(expectedCurrentBalanceFromAccount, fromAccount.getCurrentBalance(), 0.0001);
        assertEquals(expectedDailyLimitFromAccount, fromAccount.getDailyLimit(), 0.0001);
        assertEquals(expectedDailyLimitToAccount, toAccount.getDailyLimit(), 0.0001);
    }

    @Test
    public void testTransferMoneyInvalid() throws InvalidBankOperationException {
        BankAccount fromAccount = new BankAccount();
        double amount = 10;
        BankAccount toAccount = new BankAccount();

        when(helper.isAvailableForDailyWithdraw(fromAccount, amount)).thenReturn(false);

        assertThrows(InvalidBankOperationException.class, () -> {
            manager.transferMoney(fromAccount, toAccount, amount);
        });
    }

    @Test
    public void withdrawMoney() throws InvalidBankOperationException{
        BankAccount account = new BankAccount();
        account.setCurrentBalance(120);
        account.setDailyLimit(100);
        account.setAccountCurrency(BankAccountHelper.USD_ACCOUNT);
        double amount = 10;
        double expectedCurrentBalance = account.getCurrentBalance() - amount;
        double expectedDailyLimit = account.getDailyLimit() - amount;

        when(helper.isBalanceValidForWithdraw(account, amount, account.getAccountCurrency())).thenReturn(true);
        when(helper.isAvailableForDailyWithdraw(account, amount)).thenReturn(true);
        when(helper.convertCurrency(account.getAccountCurrency(), account.getAccountCurrency(), amount)).thenReturn(amount);

        manager.withdrawMoney(account, amount, account.getAccountCurrency());

        assertEquals(expectedCurrentBalance, account.getCurrentBalance(), 0.0001);
        assertEquals(expectedDailyLimit, account.getDailyLimit(), 0.0001);
    }

    @Test
    public void withdrawMoneyInvalid() throws InvalidBankOperationException{
        BankAccount account = new BankAccount();
        double amount = 10;

        when(helper.isAvailableForDailyWithdraw(account, amount)).thenReturn(false);

        assertThrows(InvalidBankOperationException.class, () -> {
            manager.withdrawMoney(account, amount, account.getAccountCurrency());
        });
    }

    @Test
    public void testAddMoney() {
        BankAccount account = new BankAccount();
        account.setCurrentBalance(0);
        account.setAccountCurrency(BankAccountHelper.USD_ACCOUNT);
        double amount = new Random().nextDouble();
        when(helper.convertCurrency(account.getAccountCurrency(), account.getAccountCurrency(), amount)).thenReturn(amount);

        manager.addMoney(account, amount, BankAccountHelper.USD_ACCOUNT);

        assertEquals(amount, account.getCurrentBalance(), 0.0001);

    }
}