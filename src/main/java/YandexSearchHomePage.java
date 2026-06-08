import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

/**
 * PageObject Яндекс Поиска для главной страницы ya.ru
 *
 * @author MonomythConvergence/Михаил Гришин
 */
public class YandexSearchHomePage extends YandexSearchBasePage<YandexSearchHomePage> {
    /**
     * Конструктор страницы.
     */
    protected YandexSearchHomePage() {
        super();
    }

    /**
     * Локаторы/селекторы
     */
    private final SelenideElement searchBar = $x("//div[contains(@class, 'search3__input-inner-container')]//textarea[@id='text']");
    private final SelenideElement searchButton = $x("//button[@aria-label='Найти']");
    private final ElementsCollection suggestItems = $$x(
            "//div[contains(@class, 'mini-suggest__popup')]//div[contains(@class, 'mini-suggest__item')]"
    );
    /**
     * Первая часть каждого теста на ввод поискового запроса
     *
     * @param query - текст запроса вводимый вы поле поиска
     */
    @Step("Совершаем клик по полю поиска перед вводом текста")
    private void activateSearchBarAndEnterQuery(String query) {
        searchBar.shouldBe(Condition.visible).click();
        searchBar.sendKeys(query);
    }

    /**
     * Открытые страницы для тестов (заданно через параметризацию в base классе PO)
     */
    protected YandexSearchHomePage open() {
        open("/");
        return this;
    }

    /**
     * Тестовый метод для ввода запроса через Enter
     *
     * @param query - передаётся подметоду activateSearchBarAndEnterQuery для ввода
     */
    protected YandexSearchSearchResultsPage searchTestViaEnter(String query) {
        activateSearchBarAndEnterQuery(query);
        searchBar.pressEnter();
        return new YandexSearchSearchResultsPage();
    }

    /**
     * Тестовый метод для ввода запроса через кнопку поиска
     *
     * @param query - передаётся подметоду activateSearchBarAndEnterQuery для ввода
     */
    protected YandexSearchSearchResultsPage searchTestViaIconButton(String query) {
        activateSearchBarAndEnterQuery(query);
        searchButton.click();
        return new YandexSearchSearchResultsPage();
    }

    /**
     * Тестовый метод для ввода запроса первую подсказку из выпадающего списка
     *
     * @param query - передаётся подметоду activateSearchBarAndEnterQuery для ввода
     */
    protected YandexSearchSearchResultsPage searchTestViaFirstSuggestion(String query) {
        activateSearchBarAndEnterQuery(query);
        suggestItems.first().click();
        return new YandexSearchSearchResultsPage();
    }

}


