import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

/**
 * Базовый абстрактный класс для PageObject Яндекс Маркета
 *
 * @author MonomythConvergence/Михаил Гришин
 */
public abstract class YandexMarketBasePage<T> {

    /**
     * Локатор поля поиска в шапке всех страниц ЯМ.
     */
    protected final SelenideElement headerSearch = $x("//input[@id='header-search']");

    /**
     * Конструктор
     */
    protected YandexMarketBasePage() {
    }

    /**
     * Открывает указанный URL
     */
    @Step("Открываем страницу {url}")
    public T open(String url) {
        Selenide.open(url);
        return (T) this;
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
    public T shouldBeLoaded(String expectedPartialUrl, ElementsCollection elementsToCheck) {
        String actualUrl = WebDriverRunner.url();
        if (!actualUrl.contains(expectedPartialUrl)) {
            throw new AssertionError(
                    String.format("Expected URL to contain '%s' but actual URL was '%s'",
                            expectedPartialUrl, actualUrl)
            );
        }

        elementsToCheck.forEach(el -> el.shouldBe(visible)); // Проверка видимости всех элементов из чек-листа
        return (T) this;
    }


    /**
     * Ищем через поиск в шапке.
     */
    @Step("Ищем через поиск в шапке: '{query}'")
    protected YandexMarketSearchResultsPage searchViaHeaderBar(String query) {
        headerSearch.shouldBe(visible).click();

        headerSearch
                .shouldBe(visible)
                .setValue(query)
                .pressEnter();

        return new YandexMarketSearchResultsPage();
    }
}