package settings.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import settings.AlertHandler;
import settings.WaitUtils;

import java.time.Duration;

abstract public class BaseTest {

    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<WaitUtils> waitUtilsThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<AlertHandler> alertHandlerThreadLocal = new ThreadLocal<>();

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--start-maximized");
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
        driver.manage().timeouts().scriptTimeout(Duration.ofSeconds(30));

        WaitUtils waitUtils = new WaitUtils(driver);
        AlertHandler alertHandler = new AlertHandler(driver);

        driverThreadLocal.set(driver);
        waitUtilsThreadLocal.set(waitUtils);
        alertHandlerThreadLocal.set(alertHandler);

        clearAlertsInTest();

        Allure.step("Драйвер инициализирован для потока: " + Thread.currentThread().getId());
    }

    @AfterEach
    public void tearDown() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                clearAlertsBeforeQuit();
                driver.quit();
            } catch (Exception e) {
                Allure.step("Ошибка при закрытии драйвера: " + e.getMessage());
            } finally {
                driverThreadLocal.remove();
                waitUtilsThreadLocal.remove();
                alertHandlerThreadLocal.remove();
            }
        }
    }

    private void clearAlertsBeforeQuit() {
        try {
            AlertHandler alertHandler = alertHandlerThreadLocal.get();
            if (alertHandler != null && alertHandler.isAlertPresent()) {
                alertHandler.clearExistingAlerts();
            }
        } catch (Exception e) {
        }
    }

    protected WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver не инициализирован для текущего потока");
        }
        return driver;
    }

    protected WaitUtils getWaitUtils() {
        WaitUtils waitUtils = waitUtilsThreadLocal.get();
        if (waitUtils == null) {
            throw new IllegalStateException("WaitUtils не инициализирован для текущего потока");
        }
        return waitUtils;
    }

    protected AlertHandler getAlertHandler() {
        AlertHandler alertHandler = alertHandlerThreadLocal.get();
        if (alertHandler == null) {
            throw new IllegalStateException("AlertHandler не инициализирован для текущего потока");
        }
        return alertHandler;
    }

    protected void handleAlertInTest(boolean accept, String expectedText) {
        Allure.step(String.format("Обработка alert: accept=%s, expectedText=%s", accept, expectedText));
        getAlertHandler().handleAlert(accept, expectedText);
    }

    protected void clearAlertsInTest() {
        Allure.step("Очистка существующих alert'ов");
        getAlertHandler().clearExistingAlerts();
    }
}