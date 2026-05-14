import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


/**
 * PageObject Яндекс Маркета для страницы категории "Мобильные телефоны".
 * В задании "Смартфоны" но это явно ошибка, так как такой категории (больше?) нет.
 *
 * @author MonomythConvergence/Михаил Гришин
 */
public class YandexMobilePhonesCategoryPage extends YandexMarketBasePage {
    /**
     * Конструктор страницы категории "Мобильные телефоны".
     */
    protected YandexMobilePhonesCategoryPage() {
        super();
    }

    /**
     * Элементы и селекторы
     */
    protected final SelenideElement titleTextMobilePhone = $(By.xpath("//h1[@data-auto='title' and normalize-space(text())='Мобильные телефоны']"));
    protected final SelenideElement loadingCurtain = $("div[data-auto='SerpStatic-loader']");
    By searchPagerNextPage = By.xpath("//div[@data-zone-name='next']");

    /**
     * Метод проверки корректной загрузки верной страницы
     *
     * @throws TimeoutException если страница не прошла проверки
     */
    @Step("Проверяем корректную загрузку страницы категории Мобильные телефоны")
    public void isCorrectlyLoadedCheck() {
        List<SelenideElement> checklist = new ArrayList<>();
        checklist.add(titleTextMobilePhone);
        isCorrectlyLoaded("catalog--mobilnye-telefony", checklist);
    }

    /**
     * Отмечает чекбокс бренда по локатору
     *
     * @param brandName String с названием бренда
     */
    @Step("Выбираем бренд по локатору")
    public void clickBrandCheckbox(String brandName) {

        SelenideElement checkboxCandidateOne = $(By.xpath(
                "//label[@role='checkbox' and .//span[contains(text(), '" + brandName + "')]]"
        ));

        SelenideElement checkboxCandidateTwo = $(By.xpath("//div[@data-zone-name='filterValue']//a[@role='button' and .//span[contains(text(), '" + brandName + "')]]"));
        //Новый дизайн, периодически тестируется, возможно от него откажутся в результате AB тестов

        SelenideElement checkbox = checkboxCandidateOne.exists() ? checkboxCandidateOne : checkboxCandidateTwo;
        checkbox.shouldBe(Condition.enabled);
        click(checkbox);

        SelenideElement finalBrandFilterIcon = $(By.xpath("//div[@data-zone-name='filter' and contains(@data-zone-data, '\"filterId\":\"7893318\"')]//div[contains(@class,'ds-chip') and .//span[text()='" + brandName + "']]"));
        finalBrandFilterIcon.should(exist); //Этот же элемент с текстом "Бренд" доступен сразу после нажатия на фильтр, но меняет текст на название бренда только после загрузки результатов. Привязать к ширме загрузки не вышло, использую как замену.
    }


    /**
     * Внутренний вспомогательный метод. Проверяет применение фильтров в видимых результатах по ключевому слову
     *
     * @param keyword - ключевое слово на проверку, уже приведено к lowercase
     * @return возвращает Boolean
     */
    @Step("Проверяем применение фильтров по ключевому слову")
    private boolean checkFilterApplication(String keyword) {
        List<SelenideElement> realResults = $$(By.xpath("//*[@data-zone-name='productSnippet' and not(ancestor::*[@data-zone-name='madvIncut']) and normalize-space()]"));

        return realResults
                .stream()
                .allMatch(product -> {
                    SelenideElement titleElement = product.$("[data-auto='snippet-title']");

                    String title = titleElement.getText().toLowerCase().trim();
                    System.out.println("Result: " + title + " | Contains: " + title.contains(keyword)); //todo delete

                    return title.contains(keyword);
                });
    }


    /**
     * Проверяет применение фильтров по ключевому слову
     * <p>
     * Метод прокрутки результатов: Нажимает на кнопку следующей страницы в панели навигации пока кнопка не пропадёт
     * или пока после нажатия кнопки не подгрузится новых результатов.
     * Во втором случае exception не бросается - у Яндекса стабильно "заедается" страницы
     * к концу списка, без этого мы не проверим соответствие фильтров.
     *
     * @param keyword - ключевое слово на проверку.
     * @return Boolean с результатом проверки, не проверяет все результаты, возвращает false сразу при нарушении правил
     */
    @Step("Прокручиваем до последней страницы результатов")
    public boolean checkAllResultsForKeyword(String keyword) {
        boolean check = true;
        int previousResultCount = 0;
        boolean finalPageReached = false;
        int maxIterations = 100; // защита от бесконечного цикла
        int iteration = 0;
        String normalizedKeyword = keyword.trim().toLowerCase();

        $(searchPagerNextPage).shouldBe(enabled);

        while (iteration < maxIterations) {
            iteration++;

            List<SelenideElement> nextButtons = $$(searchPagerNextPage);
            finalPageReached = nextButtons.stream().noneMatch(SelenideElement::isEnabled);

            if (finalPageReached) {
                break;
            }

            nextButtons.getFirst().click();
            boolean newResultsLoaded = checkForNewResults(previousResultCount);
            if (!newResultsLoaded) {
                break; // Яндекс имеет тенденцию "застревать" на страницах после 60+ - если мы не получили новых результатов то ждать дальше уже нет смысла
            }

            previousResultCount = $$("[data-zone-name='productSnippet']").size();
            check = checkFilterApplication(normalizedKeyword);

        }
        return check;
    }


    /**
     * Вынесенный отдельно для читаемости внутренний метод проверки увеличения кол-ва результатов поиска
     * Так как запрещен try/catch обходимся ручным таймером
     * @implNote Не использую wait/await/should так как такой тест не подойдёт для Яндекс-Маркета - прокрутить до конечной страницы выходит редко, практически во всех случая ЯМ зависнет на середине, *стабильно*.
     *
     * @param oldCount старое число результатов с которым сравниваем
     * @return возвращает Boolean
     */
    private boolean checkForNewResults(int oldCount) {
        long timeout = System.currentTimeMillis() + 5000L; //10 секунд

        while (System.currentTimeMillis() < timeout) {
            int now = $$("[data-zone-name='productSnippet']").size();
            if (now > oldCount) {
                return true;
            }
            //т.к. Sleep запрещён и любые wait кидают exception то мониторим в реальном времени...
        }
        return false;
    }
}


