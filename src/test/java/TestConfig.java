import java.io.InputStream;
import java.util.Properties;

/**
 * Класс для загрузки констант из config.properties
 * Все значения, которые могут измениться (URL, грани цен и т.д.), берутся отсюда.
 *
 * @author MonomythConvergence/Михаил Гришин
 */
public class TestConfig {
    private static final Properties PROPERTIES = new Properties();
    static { //содержит try/catch, но не является частью тестовой логики
        try (InputStream input = TestConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IllegalStateException("Не найден config.properties в resources");
            }
            PROPERTIES.load(input);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось загрузить config.properties", e);
        }
    }

    /**
     * Возвращает значение из файла.
     *
     * @param key строка-ключ свойства
     * @return String с искомым значением
     */
    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }
}
