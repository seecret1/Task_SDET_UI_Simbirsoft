package test_logic;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import settings.WaitUtils;
import settings.base.BasePage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CustomersListPage extends BasePage {

    @FindBy(xpath = "//table[@class='table table-bordered table-striped']")
    private WebElement tableCustomers;

    public CustomersListPage(WebDriver driver, WaitUtils waitUtils) {
        super(driver, waitUtils);
        PageFactory.initElements(driver, this);
        waitForTableToLoad();
    }

    @Step("Ожидание загрузки таблицы клиентов")
    public void waitForTableToLoad() {
        try {
            waitUtils.waitForVisible(tableCustomers, 10);
            Allure.step("Таблица клиентов загружена");
        } catch (Exception e) {
            Allure.step("Таблица клиентов не загрузилась: " + e.getMessage());
        }
    }

    @Step("Проверить наличие клиента: {firstName} {lastName}")
    public boolean isCustomerInTable(String expectedFirstName, String expectedLastName, String expectedPostCode) {
        try {
            waitForTableToLoad();

            List<WebElement> rows = tableCustomers.findElements(By.xpath(".//tbody/tr"));
            Allure.step("Найдено строк в таблице: " + rows.size());

            for (WebElement row : rows) {
                List<WebElement> cells = row.findElements(By.tagName("td"));

                if (cells.size() >= 3) {
                    String actualFirstName = cells.get(0).getText().trim();
                    String actualLastName = cells.get(1).getText().trim();
                    String actualPostCode = cells.get(2).getText().trim();

                    Allure.step("Проверяем: " + actualFirstName + " " + actualLastName + " " + actualPostCode);

                    if (actualFirstName.equals(expectedFirstName) &&
                            actualLastName.equals(expectedLastName) &&
                            actualPostCode.equals(expectedPostCode)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (Exception e) {
            Allure.step("Ошибка при поиске клиента в таблице: " + e.getMessage());
            return false;
        }
    }

    @Step("Проверить прямую сортировку по имени (A-Z)")
    public boolean checkSortByName() {
        List<String> names = getCustomerNames();

        if (names.isEmpty()) {
            Allure.step("Список клиентов пуст");
            return true;
        }

        List<String> sortedNames = new ArrayList<>(names);
        Collections.sort(sortedNames);

        boolean isSorted = names.equals(sortedNames);
        Allure.step("Проверка прямой сортировки: " + (isSorted ? "Отсортировано A-Z" : "Не отсортировано A-Z"));
        Allure.step("Фактический порядок: " + names);
        Allure.step("Ожидаемый порядок A-Z: " + sortedNames);

        return isSorted;
    }

    @Step("Проверить обратную сортировку по имени (Z-A)")
    public boolean checkReverseSortByName() {
        List<String> names = getCustomerNames();

        if (names.isEmpty()) {
            Allure.step("Список клиентов пуст");
            return true;
        }

        List<String> reverseSortedNames = new ArrayList<>(names);
        Collections.sort(reverseSortedNames, Collections.reverseOrder());

        boolean isReverseSorted = names.equals(reverseSortedNames);
        Allure.step("Проверка обратной сортировки: " + (isReverseSorted ? "Отсортировано Z-A" : "Не отсортировано Z-A"));
        Allure.step("Фактический порядок: " + names);
        Allure.step("Ожидаемый порядок Z-A: " + reverseSortedNames);

        return isReverseSorted;
    }

    @Step("Получить список всех имен клиентов из таблицы")
    public List<String> getCustomerNames() {
        List<String> names = new ArrayList<>();

        try {
            waitForTableToLoad();

            List<WebElement> rows = tableCustomers.findElements(By.xpath(".//tbody/tr"));

            for (WebElement row : rows) {
                List<WebElement> cells = row.findElements(By.tagName("td"));
                if (cells.size() >= 1) {
                    String name = cells.get(0).getText().trim();
                    if (!name.isEmpty()) {
                        names.add(name);
                    }
                }
            }

            Allure.step("Получено имен: " + names.size() + " -> " + names);
            return names;

        } catch (Exception e) {
            Allure.step("Ошибка при получении имен клиентов: " + e.getMessage());
            return names;
        }
    }
}