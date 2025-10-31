package rehber;
import org.reflections.Reflections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class YazdiriciProvider {
    private static final Map<String, Class<? extends EtiketYazdırılabilir>> yazdiriciMap = new HashMap<>();

    static {
        Reflections reflections = new Reflections("rehber");

        Set<Class<? extends EtiketYazdırılabilir>> sınıflar =
                reflections.getSubTypesOf(EtiketYazdırılabilir.class);

        for (Class<? extends EtiketYazdırılabilir> clazz : sınıflar) {
            yazdiriciMap.put(clazz.getSimpleName(), clazz);
        }
    }

    public static Set<String> getAdlar() {
        return yazdiriciMap.keySet();
    }

    public static EtiketYazdırılabilir getYazdirici(String className) {
        try {
            Class<? extends EtiketYazdırılabilir> clazz = yazdiriciMap.get(className);
            if (clazz == null) {
                throw new RuntimeException("Yazdırıcı bulunamadı: " + className);
            }
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Yazdırıcı oluşturulamadı: " + className, e);
        }
    }
}
