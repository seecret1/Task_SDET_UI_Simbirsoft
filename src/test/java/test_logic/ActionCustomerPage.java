package test_logic;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import settings.ConfigProvider;
import settings.WaitUtils;
import settings.base.BasePage;

import java.util.List;
import java.util.stream.Collectors;

public class ActionCustomerPage extends BasePage {

    @FindBy(css = "[ng-class='btnClass1']")
    private WebElement btnOpenAddCustomer;

    @FindBy(xpath = "//input[@ng-model='fName']")
    private WebElement inputFirstName;

    @FindBy(xpath = "//input[@ng-model='lName']")
    private WebElement inputLastName;

    @FindBy(xpath = "//input[@ng-model='postCd']")
    private WebElement inputPostCode;

    @FindBy(xpath = "//button[contains(@type, 'submit') and contains(., 'Add Customer')]")
    private WebElement btnAddCustomer;

    @FindBy(css = "[ng-class='btnClass3']")
    private WebElement btnCustomers;

    @FindBy(xpath = "//a[contains(@ng-click, 'fName') and contains(., 'First Name')]")
    private WebElement sortFirstName;

    @FindBy(xpath = "//table[@class='table table-bordered table-striped']")
    private WebElement tableCustomers;

    public ActionCustomerPage(WebDriver driver, WaitUtils waitUtils) {
        super(driver, waitUtils);
        driver.get(ConfigProvider.URL);
        PageFactory.initElements(driver, this);
        waitForPageToLoad();
    }

    @Step("Ожидание загрузки страницы")
    public void waitForPageToLoad() {
        waitUtils.waitForVisible(btnOpenAddCustomer);
    }

    @Step("Открыть форму добавления клиента")
    public ActionCustomerPage openAddCustomerForm() {
        waitUtils.waitForClickable(btnOpenAddCustomer);
        btnOpenAddCustomer.click();
        return this;
    }

    @Step("Ввести имя: {firstName}")
    public ActionCustomerPage enterFirstName(String firstName) {
        waitUtils.waitForClickable(inputFirstName);
        inputFirstName.sendKeys(firstName);
        return this;
    }

    @Step("Ввести фамилию: {lastName}")
    public ActionCustomerPage enterLastName(String lastName) {
        waitUtils.waitForClickable(inputLastName);
        inputLastName.sendKeys(lastName);
        return this;
    }

    @Step("Ввести почтовый индекс: {postCode}")
    public ActionCustomerPage enterPostCode(String postCode) {
        waitUtils.waitForClickable(inputPostCode);
        inputPostCode.sendKeys(postCode);
        return this;
    }

    @Step("Нажать кнопку добавления клиента")
    public ActionCustomerPage clickAddCustomer() {
        waitUtils.waitForClickable(btnAddCustomer);
        btnAddCustomer.click();
        return this;
    }

    @Step("Перейти к списку клиентов")
    public CustomersListPage navigateToCustomersList() {
        waitUtils.waitForClickable(btnCustomers);
        btnCustomers.click();
        return new CustomersListPage(driver, waitUtils);
    }

    @Step("Добавить клиента и перейти к списку")
    public CustomersListPage addCustomer(String firstName, String lastName, String postCode) {
        return openAddCustomerForm()
                .enterFirstName(firstName)
                .enterLastName(lastName)
                .enterPostCode(postCode)
                .clickAddCustomer()
                .navigateToCustomersList();
    }

    @Step("Выполнить сортировку по имени")
    public CustomersListPage clickSortByName(int num) {
        try {
            navigateToCustomersList();
            waitUtils.waitForClickable(sortFirstName);

            while (num-- > 0){
                sortFirstName.click();
                waitUtils.waitForSeconds(1);
            }
            Allure.step("Сортировка выполнена");


            return new CustomersListPage(driver, waitUtils);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось кликнуть на сортировку по имени: " + e.getMessage(), e);
        }
    }

    @Step("Удаление клиента по условию")
    public ActionCustomerPage clickDelCustomer() {

        navigateToCustomersList();
        List<WebElement> rows = tableCustomers.findElements(By.xpath(".//tbody/tr"));

        List<String> customerNames = rows.stream()
                .map(row -> {
                    List<WebElement> cells = row.findElements(By.tagName("td"));
                    return cells.get(0).getText().trim();
                })
                .collect(Collectors.toList());

        double averageNameLength = customerNames.stream()
                .mapToInt(String::length)
                .average()
                .getAsDouble();

        /// Находим имя с длиной, наиболее близкой к средней
        String targetName = customerNames.stream()
                .min((name1, name2) -> {
                    double diff1 = Math.abs(name1.length() - averageNameLength);
                    double diff2 = Math.abs(name2.length() - averageNameLength);
                    return Double.compare(diff1, diff2);
                })
                .get();

        deleteCustomer(targetName);

        return this;
    }

    @Step("Удалить клиента по имени: {firstName}")
    public ActionCustomerPage deleteCustomer(String firstName) {
        try {
            waitForPageToLoad();

            List<WebElement> rows = tableCustomers.findElements(By.xpath(".//tbody/tr"));
            Allure.step("Поиск клиента для удаления: " + firstName);

            for (WebElement row : rows) {
                List<WebElement> cells = row.findElements(By.tagName("td"));

                if (cells.size() >= 3) {
                    String actualFirstName = cells.get(0).getText().trim();

                    if (actualFirstName.equals(firstName)) {
                        WebElement deleteButton = row.findElement(By.xpath(".//button[contains(text(), 'Delete')]"));
                        waitUtils.waitForClickable(deleteButton);
                        deleteButton.click();
                        Allure.step("Клиент " + firstName + " удален");
                        return this;
                    }
                }
            }
            Allure.step("Клиент " + firstName + " не найден для удаления");
            return this;
        } catch (Exception e) {
            Allure.step("Ошибка при удалении клиента: " + e.getMessage());
            return this;
        }
    }
}