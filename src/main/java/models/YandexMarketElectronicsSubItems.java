package models;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

/**
 * Enum для подкатегорий электроники на Яндекс Маркете
 * @author MonomythConvergence/Михаил Гришин
 */
public enum YandexMarketElectronicsSubItems {

    LAPTOP("Ноутбуки",By.xpath("//div[@data-zone-name='linkSnippet' and contains(@data-zone-data, '\"categoryId\":[\"91013\"')]//a")),
    MOBILEPHONES("Мобильные телефоны",By.xpath("//div[@data-zone-name='linkSnippet' and contains(@data-zone-data, '\"categoryId\":[\"91491\"')]//a"));


    private final String displayName;
    private final By locator;

    YandexMarketElectronicsSubItems(String displayName, By locator) {
        this.displayName = displayName;
        this.locator = locator;
    }

    @Override
    public String toString() {
        return displayName;
    }

    /**
     * Возвращает SelenideElement подкатегории (с ожиданием кликабельности).
     */
    @Step("Открываем категорию {this}")
    public SelenideElement get() {
        return $(locator).shouldBe(Condition.enabled);
    }
}
