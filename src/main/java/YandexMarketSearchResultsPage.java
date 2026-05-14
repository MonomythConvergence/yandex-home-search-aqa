import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import java.util.List;

import static com.codeborne.selenide.Selenide.$$;

/**
 * PageObject Яндекс Маркета для страницы результатов поиска Яндекс Маркета
 *
 * @author MonomythConvergence/Михаил Гришин
 */
public class YandexMarketSearchResultsPage extends YandexMarketBasePage {
    /**
     * Конструктор страницы
     */
    protected YandexMarketSearchResultsPage() {
        super();
    }

    /**
     * Проверка на наличие продукта содержащего строку
     *
     * @return Boolean результат проверки
     */
    @Step("Проверяем наличие товара с названием, содержащим '{nameString}'")
    public boolean checkForProductName(String nameString) {
        List<SelenideElement> realResults = $$(By.xpath(
                "//*[@data-zone-name='productSnippet' and not(ancestor::*[@data-zone-name='madvIncut'])]"
        ));

        for (SelenideElement product : realResults) {
            SelenideElement  titleElement = product.$("[data-auto='snippet-title']");
            String title = titleElement.getText().trim();
            if (title.contains(nameString)) {
                return true;
            }
        }

        return false;

    }
}