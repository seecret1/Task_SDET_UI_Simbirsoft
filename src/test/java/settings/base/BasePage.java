package settings.base;

import org.openqa.selenium.WebDriver;
import settings.WaitUtils;

abstract public class BasePage {

    protected WebDriver driver;
    protected WaitUtils waitUtils;

    public BasePage(WebDriver driver, WaitUtils waitUtils) {
        this.driver = driver;
        this.waitUtils = waitUtils;
    }
}