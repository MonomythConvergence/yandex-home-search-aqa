import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тест: проверка поиска Яндекс по трём методам ввода
 *
 * @author MonomythConvergence/Михаил Гришин
 */
@Feature("Ввод запроса и проверка итоговой страницы")
@DisplayName("Тест поисковика ya.ru")
public class YandexSearchTest extends BaseTest {

    @ParameterizedTest(name = "Поисковый запрос {0}, ввод через Enter")
    @MethodSource("parameterProvider")
    public void enterInputTest(String query) {
        YandexSearchSearchResultsPage resultsPage = new YandexSearchHomePage()
                .open().searchTestViaEnter(query);

        assertThat(resultsPage.checkPageForCorrectness(query))
                .as("Страница результатов не соответствует запросу - \n" +
                        query + " - не найдено в поле поиска и/или url страницы не соответствует формату")
                .isTrue();
    }

    @ParameterizedTest(name = "Поисковый запрос {0}, ввод через кнопку интерфейса")
    @MethodSource("parameterProvider")
    public void iconButtonInputTest(String query) {
        YandexSearchSearchResultsPage resultsPage = new YandexSearchHomePage()
                .open().searchTestViaIconButton(query);

        assertThat(resultsPage.checkPageForCorrectness(query))
                .as("Страница результатов не соответствует запросу - \n" +
                        query + " - не найдено в поле поиска и/или url страницы не соответствует формату")
                .isTrue();
    }

    @ParameterizedTest(name = "Поисковый запрос {0}, ввод через первую подсказку из выпадающего меню")
    @MethodSource("parameterProvider")
    public void firstSuggestionInputTest(String query) {
        YandexSearchSearchResultsPage resultsPage = new YandexSearchHomePage()
                .open().searchTestViaFirstSuggestion(query);

        assertThat(resultsPage.checkPageForCorrectness(query))
                .as("Страница результатов не соответствует запросу - \n" +
                        query + " - не найдено в поле поиска и/или url страницы не соответствует формату")
                .isTrue();
    }

    private static Stream<Arguments> parameterProvider() {
        return Stream.of(Arguments.of(
                TestConfig.getProperty("query")
        ));
    }
}
