package Levels;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class LevelLoader {

    private LevelLoader() {}
    public static List<String> load(String resourcePath, int levelIndex) {
        List<String> lines = new ArrayList<>();

        try (InputStream is = LevelLoader.class.getClassLoader()
                .getResourceAsStream(resourcePath)) {

            if (is == null) {
                throw new IllegalArgumentException("Không tìm thấy resource: " + resourcePath);
            }

            try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                String line;
                boolean reading = false;

                while ((line = br.readLine()) != null) {
                    String raw = line;              // giữ nguyên spacing
                    String t = raw.trim();
                    if (t.isEmpty()) continue;

                    if (t.equals(String.valueOf(levelIndex))) {
                        reading = true;
                        continue;
                    }
                    if (t.equalsIgnoreCase("BREAK") && reading) break;

                    if (reading) lines.add(raw);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi đọc map: " + e.getMessage(), e);
        }

        if (lines.isEmpty())
            throw new IllegalArgumentException("Không tìm thấy Level " + levelIndex + " trong " + resourcePath);

        return lines;
    }
    //update
}
