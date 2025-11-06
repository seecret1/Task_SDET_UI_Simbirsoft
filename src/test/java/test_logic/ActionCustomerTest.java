package test_logic;

import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import settings.ConfigProvider;
import settings.base.BaseTest;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;

@Epic("Тесты добавления клиента")
@Feature("Заполнение формы данными")
@DisplayName("Тесты для страницы добавления клиента")
@Execution(ExecutionMode.CONCURRENT)
public class ActionCustomerTest extends BaseTest {

    private static final Map<Integer, Character> NUMBER_TO_LETTER_MAP = createMapping();
    private final Random random = new Random();

    @Test
    @Story("Позитивное добавление клиента")
    @DisplayName("Успешное добавление клиента")
    @Description("Тест проверяет корректное заполнение всех полей формы и добавление клиента")
    @Severity(SeverityLevel.NORMAL)
    public void testAddDefaultCustomer() {
        String generateNum = generatePostCode();
        String firstName = generateFirstName(generateNum);
        String lastName = ConfigProvider.DEFAULT_LAST_NAME;

        step("Добавление клиента", step -> {
            new ActionCustomerPage(getDriver(), getWaitUtils())
                    .addCustomer(firstName, lastName, generateNum);
        });

        step("Проверка наличия клиента в списке", step -> {
            boolean isCustomerAdded = new CustomersListPage(getDriver(), getWaitUtils())
                    .isCustomerInTable(firstName, lastName, generateNum);
            assertTrue(isCustomerAdded, "Клиент должен быть добавлен в систему");
        });
    }

    @Test
    @Story("Позитивное добавление клиента")
    @DisplayName("Успешное добавление клиента с фамилией в нижнем регистре")
    @Description("Тест проверяет корректное заполнение всех полей формы и добавление клиента")
    @Severity(SeverityLevel.NORMAL)
    public void testAddCustLowerName() {
        String generateNum = generatePostCode();
        String firstName = generateFirstName(generateNum);
        String lastName = ConfigProvider.LOWER_LAST_NAME;

        step("Добавление клиента", step -> {
            new ActionCustomerPage(getDriver(), getWaitUtils())
                    .addCustomer(firstName, lastName, generateNum);
        });

        step("Проверка наличия клиента в списке", step -> {
            boolean isCustomerAdded = new CustomersListPage(getDriver(), getWaitUtils())
                    .isCustomerInTable(firstName, lastName, generateNum);
            assertTrue(isCustomerAdded, "Клиент с фамилией в нижнем регистре успешно добавлен");
        });
    }

    @Test
    @Story("Позитивная попытка добавления клиента")
    @DisplayName("Успешное добавление клиента с числовыми данными в фамилии")
    @Description("Тест проверяет корректную работу сайта на заполнение поля с фамилией")
    @Severity(SeverityLevel.NORMAL)
    public void testAddCustIncorrectName() {
        String generateNum = generatePostCode();
        String firstName = generateFirstName(generateNum);
        String lastName = ConfigProvider.INCORRECT_LAST_NAME;

        step("Добавление клиента", step -> {
            new ActionCustomerPage(getDriver(), getWaitUtils())
                    .addCustomer(firstName, lastName, generateNum);
        });

        step("Проверка наличия клиента в списке", step -> {
            boolean isCustomerAdded = new CustomersListPage(getDriver(), getWaitUtils())
                    .isCustomerInTable(firstName, lastName, generateNum);
            assertTrue(isCustomerAdded, "Клиент с числовыми данными в фамилии добавлен");
        });
    }

    @Test
    @Story("Позитивная попытка добавления клиента")
    @DisplayName("Успешное добавление клиента с русской фамилией")
    @Description("Тест проверяет корректную работу сайта на добавление пользователя")
    @Severity(SeverityLevel.NORMAL)
    public void testAddCustCirillicName() {
        String generateNum = generatePostCode();
        String firstName = generateFirstName(generateNum);
        String lastName = ConfigProvider.CIRILLIC_LAST_NAME;

        step("Добавление клиента", step -> {
            new ActionCustomerPage(getDriver(), getWaitUtils())
                    .addCustomer(firstName, lastName, generateNum);
        });

        step("Проверка наличия клиента в списке", step -> {
            boolean isCustomerAdded = new CustomersListPage(getDriver(), getWaitUtils())
                    .isCustomerInTable(firstName, lastName, generateNum);
            assertTrue(isCustomerAdded, "Клиент с русской фамилией добавлен");
        });
    }

    @Test
    @Story("Негативная попытка добавления клиента")
    @DisplayName("Отказ добавления клиента с пустой фамилией")
    @Description("Тест проверяет корректную работу сайта на заполнение пустых полей")
    @Severity(SeverityLevel.NORMAL)
    public void testAddCustEmptyName() {
        String generateNum = generatePostCode();
        String firstName = generateFirstName(generateNum);
        String lastName = ConfigProvider.EMPTY_LAST_NAME;

        step("Добавление клиента с пустой фамилией", step -> {
            new ActionCustomerPage(getDriver(), getWaitUtils())
                    .addCustomer(firstName, lastName, generateNum);
        });

        step("Проверка что клиент НЕ добавлен", step -> {
            boolean customerExists = new CustomersListPage(getDriver(), getWaitUtils())
                    .isCustomerInTable(firstName, lastName, generateNum);
            assertFalse(customerExists, "Клиент с пустой фамилией не должен быть добавлен в систему");
        });
    }

    @Test
    @Story("Негативная попытка добавления клиента")
    @DisplayName("Отказ добавления клиента с пустыми полями")
    @Description("Тест проверяет корректную работу сайта на заполнение пустых полей")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddCustFullEmpty() {
        String firstName = " ";
        String lastName = " ";
        String postCode = " ";

        step("Добавление клиента с пустыми полями", step -> {
            new ActionCustomerPage(getDriver(), getWaitUtils())
                    .addCustomer(firstName.trim(), lastName.trim(), postCode.trim());
        });

        step("Проверка что клиент НЕ добавлен", step -> {
            boolean customerExists = new CustomersListPage(getDriver(), getWaitUtils())
                    .isCustomerInTable(
                    firstName.trim(), lastName.trim(), postCode.trim());
            assertFalse(customerExists, "Клиент с пустыми данными не должен быть добавлен в систему");
        });
    }

    @Test
    @DisplayName("Тест сортировки по имени")
    @Story("Сортировка клиентов")
    @Description("Проверка сортировки клиентов по имени в прямом и обратном порядке")
    @Severity(SeverityLevel.NORMAL)
    public void testSortByName() {
        step("Проверка сортировки по имени", step -> {
            CustomersListPage customersPage = new ActionCustomerPage(getDriver(), getWaitUtils())
                    .clickSortByName(2);

            assertTrue(customersPage.checkSortByName(),
                    "После второго клика таблица должна быть отсортирована по имени A-Z");
        });
    }

    @Test
    @DisplayName("Тест обратной сортировки по имени")
    @Story("Сортировка клиентов")
    @Description("Проверка сортировки клиентов по имени в обратном порядке")
    @Severity(SeverityLevel.NORMAL)
    public void testSortByNameReverse() {
        step("Проверка обратной сортировки по имени", step -> {
            CustomersListPage customersPage = new ActionCustomerPage(getDriver(), getWaitUtils())
                    .clickSortByName(1);

            assertTrue(customersPage.checkReverseSortByName(),
                    "Таблица должна быть отсортирована по имени Z-A");
        });
    }

    @Test
    @Story("Удаление клиента по условию")
    @DisplayName("Успешное удаление клиента с именем ближайшим к средней длине")
    @Description("Тест проверяет удаление клиента по условию средней длины имени")
    @Severity(SeverityLevel.NORMAL)
    public void testDelCustomer() {
        step("Удаление клиента по условию", step -> {
            new ActionCustomerPage(getDriver(), getWaitUtils())
                    .clickDelCustomer();
        });
    }

    @Test
    @Story("Удаление клиента по условию")
    @DisplayName("Успешное удаление клиента с именем ближайшим к средней длине")
    @Description("Тест проверяет удаление клиента по условию средней длины имени")
    @Severity(SeverityLevel.NORMAL)
    public void testDelCustomers() {
        step("Удаление клиента по условию", step -> {
            new ActionCustomerPage(getDriver(), getWaitUtils())
                    .clickDelCustomer()
                    .clickDelCustomer()
                    .clickDelCustomer();
        });
    }

    private static Map<Integer, Character> createMapping() {
        Map<Integer, Character> map = new HashMap<>();
        for (int i = 0; i <= 99; i++) {
            char letter = (char) ('a' + (i % 26));
            map.put(i, letter);
        }
        return map;
    }

    private String generatePostCode() {
        StringBuilder postCode = new StringBuilder();
        int postCodeLength = ConfigProvider.POST_CODE_LEN;

        for (int i = 0; i < postCodeLength; i++) {
            postCode.append(random.nextInt(10));
        }
        return postCode.toString();
    }

    private String generateFirstName(String postCode) {
        StringBuilder postName = new StringBuilder();
        int postCodeLength = ConfigProvider.POST_CODE_LEN;

        for (int i = 0; i < postCodeLength; i += 2) {
            String twoNum = postCode.substring(i, i + 2);
            int num = Integer.parseInt(twoNum);
            char sym = NUMBER_TO_LETTER_MAP.get(num);
            postName.append(sym);
        }
        return postName.toString();
    }
}