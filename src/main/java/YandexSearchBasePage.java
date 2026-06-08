import com.codeborne.selenide.Selenide;
import io.qameta.allure.Step;

/**
 * Базовый абстрактный класс для PageObject поиска Яндекс
 *
 * @author MonomythConvergence/Михаил Гришин
 */
public abstract class YandexSearchBasePage<T> {

    /**
     * Конструктор
     */
    protected YandexSearchBasePage() {
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


}