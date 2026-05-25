import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selenide.*;


/**
 * PageObject Яндекс Маркета для страницы категории "Мобильные телефоны".
 * В задании "Смартфоны" но это явно ошибка, так как такой категории (больше?) нет.
 * В PO используются *статичный* селектор и String для проверки страницы так как PO рассчитан на конкретную категорию, а
 * не как generic PO для любой категории каталога.
 *
 * @author MonomythConvergence/Михаил Гришин
 */
public class YandexMobilePhonesCategoryPage extends YandexMarketBasePage<YandexMobilePhonesCategoryPage> {

    /**
     * Элементы и селекторы
     */
    By titleTextMobilePhone = By.xpath("//h1[@data-auto='title' and normalize-space(text())='Мобильные телефоны']");
    private  final SelenideElement loadingCurtain = $x("div[data-auto='SerpStatic-loader']");
    By searchPagerNextPage = By.xpath("//div[@data-zone-name='next']");

    /**
     * Константы вынесенные для читаемости
     */
    private static final int MAX_PAGINATION_ITERATIONS = 100;     //компромисс, чтобы избежать бесконечной подгрузки пустых страниц (частотный баг)
    private static final int NEW_RESULTS_TIMEOUT_MS = 5000;      //таймаут ожидания подгрузки

    /**
     * Метод проверки корректной загрузки верной страницы
     *
     * @throws TimeoutException если страница не прошла проверки
     */
    @Step("Проверяем корректную загрузку страницы категории Мобильные телефоны")
    public YandexMobilePhonesCategoryPage isCorrectlyLoadedCheck(){
        ElementsCollection checklist = $$(titleTextMobilePhone);
        return (YandexMobilePhonesCategoryPage) shouldBeLoaded("catalog--mobilnye-telefony", checklist);
    }

    /**
     * Отмечает чекбокс бренда по локатору
     * TODO: Потенциальное улучшение - добавить разворот списка брендов и/или использовать ввод текста для поиска (чревато
     * TODO: своей долей проблем - "Apple" вернёт больше чем один результат потенциальных фильтров)
     *
     * @param brandName String с названием бренда
     */
    @Step("Выбираем бренд по локатору")
    public YandexMobilePhonesCategoryPage clickBrandCheckbox(String brandName) {

        SelenideElement checkboxCandidateOne = $x("//label[@role='checkbox' and .//span[contains(text(), '"
                + brandName + "')]]");

        SelenideElement checkboxCandidateTwo = $x("//div[@data-zone-name='filterValue']//a[@role='button' " +
                "and .//span[contains(text(), '" + brandName + "')]]");
        //Новый дизайн, периодически тестируется, возможно от него откажутся в результате AB тестов

        SelenideElement checkbox = checkboxCandidateOne.exists() ? checkboxCandidateOne : checkboxCandidateTwo;
        checkbox.shouldBe(Condition.enabled).click();

        $x("//div[@data-zone-name='filter' and contains(@data-zone-data, '\"filterId\":\"7893318\"')]//div[contains(@class,'ds-chip') " +
                "and .//span[text()='" + brandName + "']]").should(exist);
        //Этот же элемент с текстом "Бренд" доступен сразу после нажатия на фильтр, но меняет текст на название бренда только после загрузки результатов. Привязать к ширме загрузки не вышло, использую как замену.
        return this;
    }


    /**
     * Внутренний вспомогательный метод. Проверяет применение фильтров в видимых результатах по ключевому слову
     *
     * @param keyword - ключевое слово на проверку, уже приведено к lowercase
     * @return возвращает Boolean
     */
    @Step("Проверяем применение фильтров по ключевому слову")
    private boolean checkFilterApplication(String keyword) {
        ElementsCollection realResults = $$x("//*[@data-zone-name='productSnippet' and not(ancestor::*[@data-zone-name='madvIncut']) and normalize-space()]");

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
        int iteration = 0;
        String normalizedKeyword = keyword.trim().toLowerCase();

        $(searchPagerNextPage).shouldBe(enabled);

        while (iteration < MAX_PAGINATION_ITERATIONS) { //защита от бесконечного цикла т.к. ЯМ иногда грузит пустые страницы
            iteration++;

            ElementsCollection nextButtons = $$(searchPagerNextPage);
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
        long timeout = System.currentTimeMillis() +NEW_RESULTS_TIMEOUT_MS; //5 секунд

        while (System.currentTimeMillis() < timeout) {
            int now = $$("[data-zone-name='productSnippet']").size();
            if (now > oldCount) {
                return true;
            }
            // т.к. Sleep запрещён и любые wait кидают exception то мониторим в реальном времени...
            // Thread.yield() используется чтобы снизить нагрузку на CPU.
            Thread.yield();
        }
        return false;
    }
}


