package settings;

import io.qameta.allure.Allure;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtils {

    private final WebDriver driver;

    public WaitUtils(WebDriver driver) { this.driver = driver; }

    public WebElement waitForClickable(WebElement element) {
        return waitForClickable(element, 15);
    }

    public WebElement waitForClickable(WebElement element, int timeoutInSeconds) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            return customWait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (TimeoutException e) {
            Allure.step("Таймаут ожидания кликабельности элемента: " + e.getMessage());
            throw e;
        } catch (UnhandledAlertException e) {
            Allure.step("Обнаружен активный alert при ожидании кликабельного элемента");
            handleActiveAlert();
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            return customWait.until(ExpectedConditions.elementToBeClickable(element));
        }
    }

    public WebElement waitForVisible(WebElement element) {
        return waitForVisible(element, 15);
    }

    public WebElement waitForVisible(WebElement element, int timeoutInSeconds) {
        try {
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            return customWait.until(ExpectedConditions.visibilityOf(element));
        } catch (TimeoutException e) {
            Allure.step("Таймаут ожидания видимости элемента: " + e.getMessage());
            throw e;
        } catch (UnhandledAlertException e) {
            Allure.step("Обнаружен активный alert при ожидании видимого элемента");
            handleActiveAlert();
            WebDriverWait customWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            return customWait.until(ExpectedConditions.visibilityOf(element));
        }
    }

    public void waitForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void handleActiveAlert() {
        try {
            WebDriverWait quickWait = new WebDriverWait(driver, Duration.ofSeconds(2));
            Alert alert = quickWait.until(ExpectedConditions.alertIsPresent());
            if (alert != null) {
                String alertText = alert.getText();
                Allure.step("Автоматически обрабатываем активный alert: '" + alertText + "'");
                alert.accept();
                waitForSeconds(1);
            }
        } catch (TimeoutException | NoAlertPresentException e) {
        }
    }
}