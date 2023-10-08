package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.data.StatusCard;
import ru.netology.page.PaymentPage;

import java.sql.SQLException;

public class DatabaseTest {

    private PaymentPage paymentPage;

    @BeforeEach
    void setUpService() {
        paymentPage = new PaymentPage();
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterEach
    void clearAll() throws SQLException {
        SQLHelper.clearAllData();
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("3.1.Проверка наличия записи APPROVED в БД при оплате картой")
    void shouldBeApprovedCardInDBWhenPaymentByMoney() {
        paymentPage.paymentByMoney();
        paymentPage.setCardNumber(DataHelper.getApprovedCardNumber());
        paymentPage.setMonth(DataHelper.getValidMonth());
        paymentPage.setYear(DataHelper.getValidYear(3));
        paymentPage.setCardOwner(DataHelper.getValidCardOwnerName());
        paymentPage.setCvcNumber(DataHelper.getValidCvc());
        paymentPage.pushСontinueButton();
        paymentPage.successPaymentMessage();
        SQLHelper.checkPaymentStatus(StatusCard.APPROVED);
    }

    @SneakyThrows
    @Test
    @DisplayName("3.2.Проверка наличия записи DECLINED в БД при оплате картой")
    void shouldBeDeclinedCardInDBWhenPaymentByMoney() {
        paymentPage.paymentByMoney();
        paymentPage.setCardNumber(DataHelper.getDeclinedCardNumber());
        paymentPage.setMonth(DataHelper.getValidMonth());
        paymentPage.setYear(DataHelper.getValidYear(1));
        paymentPage.setCardOwner(DataHelper.getValidCardOwnerName());
        paymentPage.setCvcNumber(DataHelper.getValidCvc());
        paymentPage.pushСontinueButton();
        Thread.sleep(7000);
        SQLHelper.checkPaymentStatus(StatusCard.DECLINED);
    }

    @SneakyThrows
    @Test
    @DisplayName("3.3.Проверка наличия записи APPROVED в БД при оплате кредитом")
    void shouldBeApprovedCardInDBWhenPaymentByCredit() {
        paymentPage.paymentByCredit();
        paymentPage.setCardNumber(DataHelper.getApprovedCardNumber());
        paymentPage.setMonth(DataHelper.getValidMonth());
        paymentPage.setYear(DataHelper.getValidYear(3));
        paymentPage.setCardOwner(DataHelper.getValidCardOwnerName());
        paymentPage.setCvcNumber(DataHelper.getValidCvc());
        paymentPage.pushСontinueButton();
        paymentPage.successPaymentMessage();
        SQLHelper.checkCreditStatus(StatusCard.APPROVED);
    }

    @SneakyThrows
    @Test
    @DisplayName("3.4.Проверка наличия записи DECLINED в БД при оплате кредитом")
    void shouldBeDeclinedCardInDBWhenPaymentByCredit() {
        paymentPage.paymentByCredit();
        paymentPage.setCardNumber(DataHelper.getDeclinedCardNumber());
        paymentPage.setMonth(DataHelper.getValidMonth());
        paymentPage.setYear(DataHelper.getValidYear(1));
        paymentPage.setCardOwner(DataHelper.getValidCardOwnerName());
        paymentPage.setCvcNumber(DataHelper.getValidCvc());
        paymentPage.pushСontinueButton();
        Thread.sleep(10000);
        SQLHelper.checkCreditStatus(StatusCard.DECLINED);
    }

    @Test//неверно сохраняется сумма - БАГ
    @DisplayName("3.5.Проверка правильности сохранения стоимости тура в таблице payment_entity")
    void shouldBeSaveCorrectAmountOfTravelInDB() {
        paymentPage.paymentByMoney();
        paymentPage.setCardNumber(DataHelper.getApprovedCardNumber());
        paymentPage.setMonth(DataHelper.getValidMonth());
        paymentPage.setYear(DataHelper.getValidYear(3));
        paymentPage.setCardOwner(DataHelper.getValidCardOwnerName());
        paymentPage.setCvcNumber(DataHelper.getValidCvc());
        paymentPage.pushСontinueButton();
        paymentPage.successPaymentMessage();
        SQLHelper.checkAmountOfTravelInDB();
    }
}