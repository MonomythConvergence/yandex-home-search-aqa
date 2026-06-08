import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$x;

/**
 * PageObject страницы поиска Яндекс с результатами запроса
 *
 * @author MonomythConvergence/Михаил Гришин
 */
public class YandexSearchSearchResultsPage extends YandexSearchBasePage<YandexSearchSearchResultsPage> {

    /**
    * Локаторы/selectors
    */
    private final SelenideElement searchBar = $x("//div[contains(@class, 'HeaderDesktop')]//input[contains(@class, 'HeaderForm-Input')]");

    /**
     * Проверка на верность отображаемой страницы
     *
     * @return Boolean результат проверки
     */
    @Step("Проверяем что финальная страница соответствует запросу '{queryString}'")
    public boolean checkPageForCorrectness(String queryString) {
        String searchURL = "https://ya.ru/search/?text=";
        searchBar.shouldBe(Condition.visible);
        String value = searchBar.getValue();

        return getCurrentUrl().contains(searchURL)
                && value != null
                && value.contains(queryString);
    }
}