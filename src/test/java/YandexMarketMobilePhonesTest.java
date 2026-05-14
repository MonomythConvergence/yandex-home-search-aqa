import io.qameta.allure.Feature;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

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
        SoftAssertions soft = new SoftAssertions();

        YandexMarketHomePage yandexMarketHomePage = new YandexMarketHomePage();
        yandexMarketHomePage.open(TestConfig.getProperty("yandex.market.url"));

        YandexMobilePhonesCategoryPage mobilePhonesPage = yandexMarketHomePage.selectMobilePhonesFromCatalogue();

        mobilePhonesPage.isCorrectlyLoadedCheck();

        mobilePhonesPage.clickBrandCheckbox(brandName);

        soft.assertThat(mobilePhonesPage.checkAllResultsForKeyword(brandKeyword))
                .as("Результаты содержат продукты не соответствующие фильтрам")
                .isTrue();


        soft.assertAll();
    }

    private static Stream<Arguments> parameterProvider() {
        return Stream.of(Arguments.of(
                TestConfig.getProperty("brand.name"),
                TestConfig.getProperty("brand.keyword")
        ));
    }
}
