package org.bank;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Random;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BankAccountHelperTest {
    @Spy
    @InjectMocks
    private BankAccountHelper helper;

    @Test
    public void testConvertCurrencyFromUSDToEUR() {
        double amount = new Random().nextDouble();
        double expected = amount * 0.94;

        double result = helper.convertCurrency(BankAccountHelper.USD_ACCOUNT, BankAccountHelper.EUR_ACCOUNT, amount);

        assertEquals(expected, result, 0.0001);
    }

    @Test
    public void testConvertCurrencyFromUSDToBYN() {
        double amount = new Random().nextDouble();
        double expected = amount * 3.18;

        double result = helper.convertCurrency(BankAccountHelper.USD_ACCOUNT, BankAccountHelper.BYN_ACCOUNT, amount);

        assertEquals(expected, result, 0.0001);
    }

    @Test
    public void testConvertCurrencyFromEURToUSD() {
        double amount = new Random().nextDouble();
        double expected = amount * 1.07;

        double result = helper.convertCurrency(BankAccountHelper.EUR_ACCOUNT, BankAccountHelper.USD_ACCOUNT, amount);

        assertEquals(expected, result, 0.0001);
    }

    @Test
    public void testConvertCurrencyFromEURToBYN() {
        double amount = new Random().nextDouble();
        double expected = amount * 3.4;

        double result = helper.convertCurrency(BankAccountHelper.EUR_ACCOUNT, BankAccountHelper.BYN_ACCOUNT, amount);

        assertEquals(expected, result, 0.0001);
    }

    @Test
    public void testConvertCurrencyFromBYNToUSD() {
        double amount = new Random().nextDouble();
        double expected = amount * 0.31;

        double result = helper.convertCurrency(BankAccountHelper.BYN_ACCOUNT, BankAccountHelper.USD_ACCOUNT, amount);

        assertEquals(expected, result, 0.0001);
    }

    @Test
    public void testConvertCurrencyFromBYNToEUR() {
        double amount = new Random().nextDouble();
        double expected = amount * 0.29;

        double result = helper.convertCurrency(BankAccountHelper.BYN_ACCOUNT, BankAccountHelper.EUR_ACCOUNT, amount);

        assertEquals(expected, result, 0.0001);
    }

    @Test
    public void testIsBalanceValidForWithdraw() {
        BankAccount account = new BankAccount();
        account.setAccountCurrency(BankAccountHelper.USD_ACCOUNT);
        double amount = new Random().nextDouble();
        account.setCurrentBalance(10);

        when(helper.convertCurrency(account.getAccountCurrency(), account.getAccountCurrency(),amount)).thenReturn(amount);
        boolean result = helper.isBalanceValidForWithdraw(account, amount, BankAccountHelper.USD_ACCOUNT);

        assertTrue(result);
    }

    @Test
    public void testIsAvailableForDailyWithdraw() {
        BankAccount account = new BankAccount();
        account.setDailyLimit(10.0);
        double amount = new Random().nextDouble();

        boolean result = helper.isAvailableForDailyWithdraw(account,amount);

        assertTrue(result);
    }
}