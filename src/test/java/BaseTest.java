import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import helpers.CustomAllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

/**
 * Базовый абстрактный класс для тестовых классов.
 * Содержит before/after для всех тестов
 *
 * @author MonomythConvergence/Михаил Гришин
 */
public abstract class BaseTest {

    /**
     * Сетап
     */
    @BeforeEach
    public void before() {
        Configuration.browser = "chrome";
        Configuration.baseUrl = TestConfig.getProperty("base.url");
        Configuration.browserSize = "1920x1080";
        Configuration.pageLoadTimeout = 15000;
        Configuration.timeout = 20000;

        SelenideLogger.addListener("AllureSelenide",
                new CustomAllureSelenide());

        Selenide.open();
        getWebDriver().manage().window().maximize();

    }

    /**
     * Закрытие тестовой среды(браузера) для подготовки к следующим тестам
     */
    @AfterEach
    public void after() {
        Selenide.closeWebDriver();
    }


}