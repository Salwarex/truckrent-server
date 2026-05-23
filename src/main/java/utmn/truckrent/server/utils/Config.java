package utmn.truckrent.server.utils;

import org.jetbrains.annotations.NotNull;
import utmn.truckrent.server.Application;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private static Map<String, String> lines = new HashMap<>();
    private static final String CONFIG_FILENAME = "config.txt";
    private static final Path CONFIG_PATH = Paths.get(CONFIG_FILENAME);

    static {
        init();
    }

    private static @NotNull String load() {
        try {
            if (Files.exists(CONFIG_PATH)) {
                return Files.readString(CONFIG_PATH).trim();
            }

            try (InputStream defaultStream = Application.class.getClassLoader()
                    .getResourceAsStream(CONFIG_FILENAME)) {

                if (defaultStream == null) {
                    throw new IllegalStateException(
                            "Шаблон " + CONFIG_FILENAME + " не найден в classpath! " +
                                    "Поместите файл в src/main/resources/"
                    );
                }

                String defaultContent = new String(defaultStream.readAllBytes()).trim();
                Files.writeString(CONFIG_PATH, defaultContent,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING);

                System.out.println("✅ Создан файл конфигурации: " + CONFIG_PATH.toAbsolutePath());
                return defaultContent;
            }

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при работе с " + CONFIG_FILENAME, e);
        }
    }

    private static void init() {
        lines.clear();
        String content = load();

        if (content.isEmpty()) {
            throw new RuntimeException("Конфиг пуст!");
        }

        String[] configLines = content.split("\\R");
        for (int i = 0; i < configLines.length; i++) {
            String line = configLines[i].trim();

            if (line.isEmpty() || line.startsWith("#")) continue;

            String[] parts = line.split("=", 2);
            if (parts.length != 2) {
                throw new RuntimeException("Ошибка форматирования в строке #%d: %s"
                        .formatted(i + 1, line));
            }

            String key = parts[0].trim();
            String value = parts[1].trim();

            if (key.isEmpty()) {
                throw new RuntimeException("Пустой ключ в строке #%d".formatted(i + 1));
            }

            lines.put(key, value);
        }
    }

    public static String getString(String path, String def){
        if(path == null) return def;
        if(!lines.containsKey(path)) return def;
        return lines.get(path);
    }

    public static String getString(String path){ return getString(path, null); }

    public static Integer getInt(String path, Integer def){
        if(path == null) return def;
        if(!lines.containsKey(path)) return def;
        Integer result;
        String value = lines.get(path);
        try{
            result = Integer.parseInt(value);
        }catch (NumberFormatException e){
            e.printStackTrace();
            throw new RuntimeException("Невозможно преобразовать строку конфига: %s".formatted(value));
        }
        return result;
    }

    public static Integer getInt(String path){ return getInt(path, null); }

    public static Double getDouble(String path, Double def){
        if(path == null) return def;
        if(!lines.containsKey(path)) return def;
        Double result;
        try{
            result = Double.parseDouble(lines.get(path));
        }catch (NumberFormatException e){
            throw new RuntimeException("Невозможно преобразовать строку конфига: %s".formatted(e));
        }
        return result;
    }

    public static Double getDouble(String path){ return getDouble(path, null); }

    public static Long getLong(String path, Long def){
        if(path == null) return def;
        if(!lines.containsKey(path)) return def;
        Long result;
        try{
            result = Long.parseLong(lines.get(path));
        }catch (NumberFormatException e){
            throw new RuntimeException("Невозможно преобразовать строку конфига: %s".formatted(e));
        }
        return result;
    }

    public static Long getLong(String path){ return getLong(path, null); }
}
