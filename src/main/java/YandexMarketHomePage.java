import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import models.YandexMarketCategories;
import models.YandexMarketElectronicsSubItems;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;

/**
 * PageObject Яндекс Маркета для главной страницы Яндекс Маркета
 *
 * @author MonomythConvergence/Михаил Гришин
 */
public class YandexMarketHomePage extends YandexMarketBasePage {
    /**
     * Конструктор страницы.
     */
    protected YandexMarketHomePage() {
        super();
    }

    /**
     * Локаторы/selectors.
     */
    protected final SelenideElement catalogueLargeTextButton = $(By.xpath("//button[contains(@class, 'ds-button') and .//span[text()='Каталог']]"));
    protected final SelenideElement catalogueSmallIconButton = $(By.xpath("//button[contains(@class, 'ds-button') and .//svg]"));
    protected final SelenideElement catalogueDialogueMenu = $(By.xpath("//div[contains(@id,'catalogEntrypoint')]"));

    /**
     * Открывает каталог по одной из двух вариантов кнопок (меняется динамично в зависимости от ширины экрана)
     */
    protected void openCatalogue() {
        boolean buttonVisible =
                catalogueLargeTextButton.is(Condition.visible) || catalogueSmallIconButton.is(Condition.visible);

        if (!buttonVisible) {
            return;
        }

        if (catalogueLargeTextButton.isDisplayed()) {
            catalogueLargeTextButton.click();
            return;
        }

        if (catalogueSmallIconButton.isDisplayed()) {
            catalogueSmallIconButton.click();
        }
    }

    /**
     * Метод наведения курсора на нужную категорию
     *
     * @param category - искомая категория
     */
    protected void moveCursorToCategory(YandexMarketCategories category) {
        moveCursorTo(category.get());
    }

    /**
     * Выполняет последовательность действий для тестирования главной страницы:
     * Открывает каталог главной страницы, наводит курсор на категорию электроники и выбирает мобильные телефоны
     *
     * @return экземпляр PO следующей страницы
     */
    @Step("Открываем каталог главной страницы, наводим курсор на категорию электроники и выбираем мобильные телефоны")
    protected YandexMobilePhonesCategoryPage selectMobilePhonesFromCatalogue() {
        openCatalogue();
        moveCursorToCategory(YandexMarketCategories.ELECTRONICS);
        click(YandexMarketElectronicsSubItems.MOBILEPHONES.get());
        return new YandexMobilePhonesCategoryPage();
    }

}


