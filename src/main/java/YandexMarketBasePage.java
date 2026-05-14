import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import java.util.List;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

/**
 * Базовый абстрактный класс для PageObject Яндекс Маркета
 *
 * @author MonomythConvergence/Михаил Гришин
 */
public abstract class YandexMarketBasePage {

    /**
     * Локатор поля поиска в шапке всех страниц ЯМ.
     */
    protected final SelenideElement headerSearch = $(By.xpath("//input[@id='header-search']"));

    /**
     * Конструктор
     */
    protected YandexMarketBasePage() {
    }

    /**
     * Открывает указанный URL
     */
    @Step("Открываем страницу по URL")
    public void open(String url) {
        Selenide.open(url);
    }

    /**
     * Возвращает текущий URL страницы.
     */
    @Step("Получаем текущий URL страницы")
    public String getCurrentUrl() {
        return Selenide.webdriver().driver().url();
    }

    /**
     * Проверяет, что страница загружена корректно.
     */
    @Step("Проверяем, что страница загружена корректно (URL содержит '{expectedPartialUrl}')")
    public void isCorrectlyLoaded(String expectedPartialUrl, List<SelenideElement> elementChecklist) {
        String actualUrl = WebDriverRunner.url();
        if (!actualUrl.contains(expectedPartialUrl)) {
            throw new AssertionError(
                    String.format("Expected URL to contain '%s' but actual URL was '%s'",
                            expectedPartialUrl, actualUrl)
            );
        }

        // Проверка видимости всех элементов из чек-листа
        for (SelenideElement element : elementChecklist) {
            element.shouldBe(visible);
        }
    }

    /**
     * Вводит текст в элемент (с предварительной очисткой).
     */
    @Step("Вводим текст '{text}' в элемент")
    protected void sendKeys(SelenideElement element, String text) {
        element.shouldBe(visible).clear();
        element.val(text);
    }

    /**
     * Кликаем по элементу.
     */
    @Step("Кликаем по элементу")
    protected void click(SelenideElement element) {
        element.shouldBe(visible).click();
    }

    /**
     * Наводим курсор на элемент.
     */
    @Step("Наводим курсор на элемент")
    protected void moveCursorTo(SelenideElement element) {
        actions().moveToElement(element).perform();
    }

    /**
     * Прокручиваем страницу до элемента.
     */
    @Step("Прокручиваем страницу до элемента")
    protected void scrollTo(SelenideElement element) {
        element.scrollIntoView(true);
    }

    /**
     * Ищем через поиск в шапке.
     */
    @Step("Ищем через поиск в шапке: '{query}'")
    protected YandexMarketSearchResultsPage searchViaHeaderBar(String query) {
        click(headerSearch);
        sendKeys(headerSearch, query);
        headerSearch.pressEnter();

        return new YandexMarketSearchResultsPage();
    }
}