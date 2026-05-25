import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тест: проверка фильтров и поиска на Яндекс Маркете (категория мобильных телефонов).
 *
 * @author MonomythConvergence/Михаил Гришин
 */
@Feature("Фильтры и поиск на Яндекс Маркете")
@DisplayName("Проверка фильтра производителей")
public class YandexMarketMobilePhonesTest extends BaseTest {

    @ParameterizedTest(name = "Бренд {0}, ключевое слово {1}")
    @MethodSource("parameterProvider")
    public void resultPageTest(String brandName, String brandKeyword) {
        boolean allResultsMatchFilter = new YandexMarketHomePage()
                .open(TestConfig.getProperty("yandex.market.url"))
                .selectMobilePhonesFromCatalogue()
                .isCorrectlyLoadedCheck()
                .clickBrandCheckbox(brandName)
                .checkAllResultsForKeyword(brandKeyword);

        assertThat(allResultsMatchFilter)
                .as("Результаты содержат продукты не соответствующие фильтрам")
                .isTrue();
    }

    private static Stream<Arguments> parameterProvider() {
        return Stream.of(Arguments.of(
                TestConfig.getProperty("brand.name"),
                TestConfig.getProperty("brand.keyword")
        ));
    }
}
