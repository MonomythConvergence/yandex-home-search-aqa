import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Класс для загрузки констант из config.properties
 * Все значения, которые могут измениться (URL, грани цен и т.д.), берутся отсюда.
 *
 * @author MonomythConvergence/Михаил Гришин
 */
public class TestConfig {
    private static final Properties PROPERTIES = new Properties();
    static {
        try (InputStream input = TestConfig.class.getClassLoader().getResourceAsStream("config.properties");
             InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8)) { //для обработки кириллицы
            if (input == null) {
                throw new IllegalStateException("Не найден config.properties в resources");
            }
            PROPERTIES.load(reader);
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
