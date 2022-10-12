package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.CardBalance;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.web.data.DataHelper.getFirstCardNumber;
import static ru.netology.web.data.DataHelper.getSecondCardNumber;
import static ru.netology.web.page.CardBalance.pushFirstCardButton;
import static ru.netology.web.page.CardBalance.pushSecondCardButton;


public class MoneyTransferTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        val cardBalance = verificationPage.validVerify(verificationCode);
    }

    @Test
    public void shouldTransferMoneyFromFirstToSecond() {
        int amount = 5000;
        val cardBalance = new CardBalance();
        val firstCardBalanceStart = cardBalance.getFirstCardBalance();
        val secondCardBalanceStart = cardBalance.getSecondCardBalance();
        val transactionPage = pushSecondCardButton();
        transactionPage.transferMoney(amount, getFirstCardNumber());
        val firstCardBalanceFinish = firstCardBalanceStart - amount;
        val secondCardBalanceFinish = secondCardBalanceStart + amount;

        assertEquals(firstCardBalanceFinish, cardBalance.getFirstCardBalance());
        assertEquals(secondCardBalanceFinish, cardBalance.getSecondCardBalance());
    }

    @Test
    public void shouldTransferMoneyFromSecondToFirst() {
        int amount = 7000;
        val cardBalance = new CardBalance();
        val firstCardBalanceStart = cardBalance.getFirstCardBalance();
        val secondCardBalanceStart = cardBalance.getSecondCardBalance();
        val transactionPage = pushFirstCardButton();
        transactionPage.transferMoney(amount, getSecondCardNumber());
        val firstCardBalanceFinish = firstCardBalanceStart + amount;
        val secondCardBalanceFinish = secondCardBalanceStart - amount;

        assertEquals(firstCardBalanceFinish, cardBalance.getFirstCardBalance());
        assertEquals(secondCardBalanceFinish,cardBalance.getSecondCardBalance());
    }

    @Test
    public void shouldTransferExceedCardBalance() {
        int amount = 30000;
        val cardBalance = new CardBalance();
        val firstCardBalanceStart = cardBalance.getFirstCardBalance();
        val secondCardBalanceStart = cardBalance.getSecondCardBalance();
        val transactionPage = pushSecondCardButton();
        transactionPage.transferMoney(amount, getFirstCardNumber());
        transactionPage.errorLimit();

    }

    @Test
    public void shouldTransferFromSecondToSecondCard() {
        int amount = 5000;
        val cardBalance = new CardBalance();
        val firstCardBalanceStart = cardBalance.getFirstCardBalance();
        val secondCardBalanceStart = cardBalance.getSecondCardBalance();
        val transactionPage = pushSecondCardButton();
        transactionPage.transferMoney(amount, getSecondCardNumber());
        transactionPage.invalidCard();
    }
}
