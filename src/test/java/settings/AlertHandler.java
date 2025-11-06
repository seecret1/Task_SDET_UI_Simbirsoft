package settings;

import io.qameta.allure.Allure;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AlertHandler {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public AlertHandler(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    public void clearExistingAlerts() {
        int attempts = 0;
        while (attempts < 3) {
            try {
                WebDriverWait quickWait = new WebDriverWait(driver, Duration.ofSeconds(1));
                Alert alert = quickWait.until(ExpectedConditions.alertIsPresent());
                if (alert != null) {
                    String alertText = alert.getText();
                    Allure.step("Очистка активного alert: '" + alertText + "'");
                    alert.accept();
                    wait(500); // короткая пауза после принятия alert
                }
                break; // выходим если alert обработан
            } catch (TimeoutException | NoAlertPresentException e) {
                break; // alert не найден - выходим
            } catch (Exception e) {
                attempts++;
                if (attempts == 3) {
                    Allure.step("Не удалось очистить alert после 3 попыток: " + e.getMessage());
                }
            }
        }
    }

    public void handleAlert(boolean accept, String expectedText) {
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String actualText = alert.getText();
            Allure.step("Обработка alert: '" + actualText + "'");

            // Проверяем ожидаемый текст, если он указан
            if (expectedText != null && !expectedText.trim().isEmpty()) {
                if (!actualText.contains(expectedText)) {
                    Allure.step("Текст alert не совпадает. Ожидалось: '" + expectedText + "', получено: '" + actualText + "'");
                }
            }

            if (accept) {
                alert.accept();
                Allure.step("Alert принят");
            } else {
                alert.dismiss();
                Allure.step("Alert отклонен");
            }

            wait(1000); // пауза после обработки alert

        } catch (TimeoutException e) {
            Allure.step("Alert не появился в течение 5 секунд");
            throw new RuntimeException("Alert не появился", e);
        } catch (NoAlertPresentException e) {
            Allure.step("Alert уже был закрыт или отсутствует");
        } catch (Exception e) {
            Allure.step("Ошибка при обработке alert: " + e.getMessage());
            throw new RuntimeException("Ошибка обработки alert", e);
        }
    }

    public boolean isAlertPresent() {
        return isAlertPresent(1);
    }

    public boolean isAlertPresent(int timeoutInSeconds) {
        try {
            WebDriverWait quickWait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            quickWait.until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (TimeoutException | NoAlertPresentException e) {
            return false;
        }
    }

    public String getAlertText() {
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            return alert.getText();
        } catch (TimeoutException | NoAlertPresentException e) {
            return null;
        }
    }

    public void sendTextToAlert(String text) {
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            alert.sendKeys(text);
            Allure.step("Введен текст в alert: '" + text + "'");
        } catch (TimeoutException | NoAlertPresentException e) {
            Allure.step("Не удалось ввести текст в alert: " + e.getMessage());
            throw new RuntimeException("Alert не доступен для ввода текста", e);
        }
    }

    private void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}