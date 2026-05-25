import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$$x;

/**
 * PageObject Яндекс Маркета для страницы результатов поиска Яндекс Маркета
 *
 * @author MonomythConvergence/Михаил Гришин
 */
public class YandexMarketSearchResultsPage extends YandexMarketBasePage<YandexMarketSearchResultsPage> {

    /**
     * Проверка на наличие продукта содержащего строку
     *
     * @return Boolean результат проверки
     */
    @Step("Проверяем наличие товара с названием, содержащим '{nameString}'")
    public boolean checkForProductName(String nameString) {
        ElementsCollection realResults = $$x(
                "//*[@data-zone-name='productSnippet' and not(ancestor::*[@data-zone-name='madvIncut'])]"
        ); //элемент внутри метода, чтобы избежать stale

        for (SelenideElement product : realResults) {
            SelenideElement  titleElement = product.$("[data-auto='snippet-title']");
            String title = titleElement.getText().trim();
            if (title.contains(nameString.toLowerCase())) {
                return true;
            }
        }

        return false;

    }
}