package ru.netology.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriverException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.netology.data.UserInfo;
import ru.netology.page.CardBalancePage;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.testng.Assert.assertEquals;

public class CardBalanceTest {
    UserInfo user = new UserInfo();
    CardBalancePage dashboard;

    @BeforeClass
    public void setUpClass() {
        WebDriverManager.chromedriver().setup();
        Configuration.browser = "chrome";
        Configuration.headless = true;
        Configuration.timeout = 10000;
        Configuration.reportsFolder = "target/selenide-reports";
        Configuration.screenshots = true;
    }

    @BeforeMethod
    public void setup() {
        try {
            open("http://localhost:9999/");
            System.out.println("Successfully opened the website");

            var loginPage = new LoginPage();
            System.out.println("Created LoginPage instance");

            var verifyPage = loginPage.login(user);
            System.out.println("Logged in successfully");

            dashboard = verifyPage.verify(user);
            System.out.println("Verified user and got dashboard");
        } catch (WebDriverException e) {
            System.err.println("WebDriver exception occurred: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.err.println("An unexpected exception occurred: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void shouldTransferAbsoluteValue() {
        int indexCardTo = 0;
        int indexCardFrom = 1;
        int cardBalanceTo = dashboard.getBalance(indexCardTo);
        int cardBalanceFrom = dashboard.getBalance(indexCardFrom);
        int amount = 1000;

        var transferPage = dashboard.transferClick(indexCardTo);
        transferPage.transfer(String.valueOf(amount), user.getCard(indexCardFrom));
        dashboard = transferPage.checkNotification(hidden);
        dashboard.reloadBalance();
        assertEquals(cardBalanceTo + amount, dashboard.getBalance(indexCardTo));
        assertEquals(cardBalanceFrom - amount, dashboard.getBalance(indexCardFrom));

        var revertTransferPage = dashboard.transferClick(indexCardFrom);
        revertTransferPage.transfer(String.valueOf(amount), user.getCard(indexCardTo));
        dashboard = revertTransferPage.checkNotification(hidden);
        dashboard.reloadBalance();
        assertEquals(cardBalanceTo, dashboard.getBalance(indexCardTo));
        assertEquals(cardBalanceFrom, dashboard.getBalance(indexCardFrom));
    }

    @Test
    public void notShouldTransferNullAmount() {
        int indexCardTo = 0;
        int indexCardFrom = 1;
        int cardBalanceTo = dashboard.getBalance(indexCardTo);
        int cardBalanceFrom = dashboard.getBalance(indexCardFrom);
        int amount = 0;

        var transferPage = dashboard.transferClick(indexCardTo);
        transferPage.transfer(String.valueOf(amount), user.getCard(indexCardFrom));
        dashboard = transferPage.checkNotification(visible);
        dashboard.reloadBalance();
        assertEquals(cardBalanceTo, dashboard.getBalance(indexCardTo));
        assertEquals(cardBalanceFrom, dashboard.getBalance(indexCardFrom));
    }

    @Test
    public void shouldTransferBoundaryValueOne() {
        int indexCardTo = 0;
        int indexCardFrom = 1;
        int cardBalanceTo = dashboard.getBalance(indexCardTo);
        int cardBalanceFrom = dashboard.getBalance(indexCardFrom);
        int amount = 1;

        var transferPage = dashboard.transferClick(indexCardTo);
        transferPage.transfer(String.valueOf(amount), user.getCard(indexCardFrom));
        dashboard = transferPage.checkNotification(hidden);
        dashboard.reloadBalance();
        assertEquals(cardBalanceTo + amount, dashboard.getBalance(indexCardTo));
        assertEquals(cardBalanceFrom - amount, dashboard.getBalance(indexCardFrom));

        var revertTransferPage = dashboard.transferClick(indexCardFrom);
        revertTransferPage.transfer(String.valueOf(amount), user.getCard(indexCardTo));
        dashboard = revertTransferPage.checkNotification(hidden);
        dashboard.reloadBalance();
        assertEquals(cardBalanceTo, dashboard.getBalance(indexCardTo));
        assertEquals(cardBalanceFrom, dashboard.getBalance(indexCardFrom));
    }

    @Test
    public void shouldTransferBoundaryValueTwo() {
        int indexCardTo = 0;
        int indexCardFrom = 1;
        int cardBalanceTo = dashboard.getBalance(indexCardTo);
        int cardBalanceFrom = dashboard.getBalance(indexCardFrom);
        int amount = cardBalanceFrom - 1;

        var transferPage = dashboard.transferClick(indexCardTo);
        transferPage.transfer(String.valueOf(amount), user.getCard(indexCardFrom));
        dashboard = transferPage.checkNotification(hidden);
        dashboard.reloadBalance();
        assertEquals(cardBalanceTo + amount, dashboard.getBalance(indexCardTo));
        assertEquals(cardBalanceFrom - amount, dashboard.getBalance(indexCardFrom));

        var revertTransferPage = dashboard.transferClick(indexCardFrom);
        revertTransferPage.transfer(String.valueOf(amount), user.getCard(indexCardTo));
        dashboard = revertTransferPage.checkNotification(hidden);
        dashboard.reloadBalance();
        assertEquals(cardBalanceTo, dashboard.getBalance(indexCardTo));
        assertEquals(cardBalanceFrom, dashboard.getBalance(indexCardFrom));
    }

    @Test
    public void shouldTransferBoundaryValueThree() {
        int indexCardTo = 0;
        int indexCardFrom = 1;
        int cardBalanceTo = dashboard.getBalance(indexCardTo);
        int cardBalanceFrom = dashboard.getBalance(indexCardFrom);
        int amount = cardBalanceFrom;

        var transferPage = dashboard.transferClick(indexCardTo);
        transferPage.transfer(String.valueOf(amount), user.getCard(indexCardFrom));
        dashboard = transferPage.checkNotification(hidden);
        dashboard.reloadBalance();
        assertEquals(cardBalanceTo + amount, dashboard.getBalance(indexCardTo));
        assertEquals(cardBalanceFrom - amount, dashboard.getBalance(indexCardFrom));

        var revertTransferPage = dashboard.transferClick(indexCardFrom);
        revertTransferPage.transfer(String.valueOf(amount), user.getCard(indexCardTo));
        dashboard = revertTransferPage.checkNotification(hidden);
        dashboard.reloadBalance();
        assertEquals(cardBalanceTo, dashboard.getBalance(indexCardTo));
        assertEquals(cardBalanceFrom, dashboard.getBalance(indexCardFrom));
    }

    @Test
    public void notShouldTransferBoundaryValueFour() {
        int indexCardTo = 0;
        int indexCardFrom = 1;
        int cardBalanceTo = dashboard.getBalance(indexCardTo);
        int cardBalanceFrom = dashboard.getBalance(indexCardFrom);
        int amount = cardBalanceFrom + 1;

        var transferPage = dashboard.transferClick(indexCardTo);
        transferPage.transfer(String.valueOf(amount), user.getCard(indexCardFrom));
        dashboard = transferPage.checkNotification(visible);
        dashboard.reloadBalance();
        assertEquals(cardBalanceTo, dashboard.getBalance(indexCardTo));
        assertEquals(cardBalanceFrom, dashboard.getBalance(indexCardFrom));
    }

    @Test
    public void notShouldTransferSingleCard() {
        int indexCardTo = 0;
        int indexCardFrom = 0;
        int cardBalanceTo = dashboard.getBalance(indexCardTo);
        int cardBalanceFrom = dashboard.getBalance(indexCardFrom);
        int amount = cardBalanceFrom / 2;

        var transferPage = dashboard.transferClick(indexCardTo);
        transferPage.transfer(String.valueOf(amount), user.getCard(indexCardFrom));
        dashboard = transferPage.checkNotification(visible);
        dashboard.reloadBalance();
        assertEquals(cardBalanceTo, dashboard.getBalance(indexCardTo));
        assertEquals(cardBalanceFrom, dashboard.getBalance(indexCardFrom));
    }

    @Test
    public void notShouldTransferEmptyAmount() {
        int indexCardTo = 0;
        int indexCardFrom = 1;
        int cardBalanceTo = dashboard.getBalance(indexCardTo);
        int cardBalanceFrom = dashboard.getBalance(indexCardFrom);

        var transferPage = dashboard.transferClick(indexCardTo);
        transferPage.transfer(null, user.getCard(indexCardFrom));
        dashboard = transferPage.checkNotification(visible);
        dashboard.reloadBalance();
        assertEquals(cardBalanceTo, dashboard.getBalance(indexCardTo));
        assertEquals(cardBalanceFrom, dashboard.getBalance(indexCardFrom));
    }

    @Test
    public void notShouldTransferEmptyCardFrom() {
        int indexCardTo = 0;
        int indexCardFrom = 1;
        int cardBalanceTo = dashboard.getBalance(indexCardTo);
        int cardBalanceFrom = dashboard.getBalance(indexCardFrom);
        int amount = cardBalanceFrom / 2;

        var transferPage = dashboard.transferClick(indexCardTo);
        transferPage.transfer(String.valueOf(amount), null);
        dashboard = transferPage.checkNotification(visible);
        dashboard.reloadBalance();
        assertEquals(cardBalanceTo, dashboard.getBalance(indexCardTo));
        assertEquals(cardBalanceFrom, dashboard.getBalance(indexCardFrom));
    }

    @Test
    public void shouldCancelTransfer() {
        int indexCardTo = 0;
        int indexCardFrom = 1;
        int cardBalanceTo = dashboard.getBalance(indexCardTo);
        int cardBalanceFrom = dashboard.getBalance(indexCardFrom);
        int amount = cardBalanceFrom / 2;

        var transferPage = dashboard.transferClick(indexCardTo);
        transferPage.cancelTransfer(String.valueOf(amount), user.getCard(indexCardFrom));
        dashboard = transferPage.checkNotification(hidden);
        dashboard.reloadBalance();
        assertEquals(cardBalanceTo, dashboard.getBalance(indexCardTo));
        assertEquals(cardBalanceFrom, dashboard.getBalance(indexCardFrom));
    }
}