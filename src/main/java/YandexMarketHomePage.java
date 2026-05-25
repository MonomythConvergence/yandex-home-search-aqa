import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import models.YandexMarketCategories;
import models.YandexMarketElectronicsSubItems;

import static com.codeborne.selenide.Selenide.$x;

/**
 * PageObject Яндекс Маркета для главной страницы Яндекс Маркета
 *
 * @author MonomythConvergence/Михаил Гришин
 */
public class YandexMarketHomePage extends YandexMarketBasePage<YandexMarketHomePage> {
    /**
     * Конструктор страницы.
     */
    protected YandexMarketHomePage() {
        super();
    }

    /**
     * Локаторы/selectors.
     */
    private final SelenideElement catalogueLargeTextButton = $x("//button[contains(@class, 'ds-button') and .//span[text()='Каталог']]");
    private final SelenideElement catalogueSmallIconButton = $x("//button[contains(@class, 'ds-button') and .//svg]");
    private final SelenideElement catalogueDialogueMenu = $x("//div[contains(@id,'catalogEntrypoint')]");

    /**
     * Открывает каталог по одной из двух вариантов кнопок (меняется динамично в зависимости от ширины экрана)
     */
    public YandexMarketHomePage openCatalogue() {
        boolean buttonVisible =
                catalogueLargeTextButton.is(Condition.visible) || catalogueSmallIconButton.is(Condition.visible);

        if (!buttonVisible) {
            return this;
        }

        if (catalogueLargeTextButton.isDisplayed()) {
            catalogueLargeTextButton.click();
        }

        if (catalogueSmallIconButton.isDisplayed()) {
            catalogueSmallIconButton.click();
        }
        return this;
    }

    /**
     * Метод наведения курсора на нужную категорию
     *
     * @param category - искомая категория
     */
    protected YandexMarketHomePage moveCursorToCategory(YandexMarketCategories category) {
        category.get().hover();
        return this;
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
        (YandexMarketElectronicsSubItems.MOBILEPHONES.get()).shouldBe(Condition.visible).click();
        return new YandexMobilePhonesCategoryPage();
    }

}


