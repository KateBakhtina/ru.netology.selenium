package ru.netology.CardFormTest;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardFormTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void sendFormTest() {
        WebElement form = driver.findElement(By.cssSelector("#root form"));

        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Катя Бахтина");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+79208123162");
        form.findElement(By.cssSelector("[data-test-id='agreement'] span")).click();
        form.findElement(By.cssSelector("button.button, .button_view_extra button_size_m button_theme_alfa-on-white")).click();

        WebElement result = driver.findElement(By.cssSelector(".App_appContainer__3jRx1"));

        String text = result.findElement(By.cssSelector("[data-test-id='order-success']")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());

    }

    @Test
    void sendFormWithInvalidNameTest() {
        WebElement form = driver.findElement(By.cssSelector("#root form"));

        int counter = 0;
        String[] invalidValues = {"1,", "K", "'", ""};
        for (String value : invalidValues) {
            form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys(value);
            if (counter == 0) {
                form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+12345678910");
                form.findElement(By.cssSelector("[data-test-id='agreement'] span")).click();
            }
            counter ++;
            form.findElement(By.cssSelector("button.button")).click();
            form.findElement(By.cssSelector(".input_invalid[data-test-id='name']"));
            form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys(Keys.CONTROL + "a");
            form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys(Keys.DELETE);
        }
    }

    @Test
    void sendFormWithInvalidPhoneTest() {
        WebElement form = driver.findElement(By.cssSelector("#root form"));

        int counter = 0;
        String[] invalidValues = {"1,", "123456789012", "доб", " ", ""};
        for (String value : invalidValues) {
            form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys(value);
            if (counter == 0) {
                form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Катя Бахтина");
                form.findElement(By.cssSelector("[data-test-id='agreement'] span")).click();
            }
            counter ++;
            form.findElement(By.cssSelector("button.button")).click();
            form.findElement(By.cssSelector(".input_invalid[data-test-id='phone']"));
            form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys(Keys.CONTROL + "a");
            form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys(Keys.DELETE);
        }
    }

    @Test
    void sendFormWithFalseAgreementCheckboxTest() {
        WebElement form = driver.findElement(By.cssSelector("#root form"));

        form.findElement(By.cssSelector("[data-test-id='name'] input")).sendKeys("Катя Бахтина");
        form.findElement(By.cssSelector("[data-test-id='phone'] input")).sendKeys("+12345678910");
        form.findElement(By.cssSelector("button.button")).click();
        form.findElement(By.cssSelector(".input_invalid[data-test-id='agreement']"));
    }
}


