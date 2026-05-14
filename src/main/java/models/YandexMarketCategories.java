package models;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;


import static com.codeborne.selenide.Selenide.$;

/**
 * Enum для категорий на Яндекс Маркете
 * @author MonomythConvergence/Михаил Гришин
 */
public enum YandexMarketCategories {

    ELECTRONICS("Электроника",By.xpath("//div[@role='dialog' and contains(@class,'_12ret')]//a[.//span[normalize-space() = 'Электроника']]"));

    private final String displayName;
    private final By locator;

    YandexMarketCategories(String displayName, By locator) {
        this.displayName = displayName;
        this.locator = locator;
    }

    @Override
    public String toString() {
        return displayName;
    }

    /**
     * Возвращает SelenideElement категории (с ожиданием кликабельности).
     */
    @Step("Открываем категорию {this}")
    public SelenideElement get() {
        return $(locator).shouldBe(Condition.enabled);
    }
}
