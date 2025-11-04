package Levels;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class LevelLoader {

    private LevelLoader() {}

    public static List<String> load(String resourcePath, int levelIndex) {
        String path = resourcePath.startsWith("/") ? resourcePath.substring(1) : resourcePath;
        List<String> lines = new ArrayList<>();

        InputStream is = LevelLoader.class.getClassLoader().getResourceAsStream(resourcePath);

        if (is == null) {
            File file = new File(path);
            if (file.exists()) {
                try {
                    is = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    throw new IllegalArgumentException("Không tìm thấy file: " + path);
                }
            } else {
                System.out.println("Current working dir: " + new File(".").getAbsolutePath());
                throw new IllegalArgumentException("Không tìm thấy resource: " + resourcePath + " và file: " + path);
            }
        }

        boolean foundLevel = false;
        boolean readingMap = false;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                String raw = line.trim();
                if (raw.isEmpty() || raw.startsWith("//")) continue; // bỏ dòng trống & comment

                // bắt đầu Level
                if (raw.equalsIgnoreCase("LEVEL " + levelIndex)) {
                    foundLevel = true;
                    continue;
                }

                // chỉ đọc nếu đúng level
                if (!foundLevel) continue;

                // bắt đầu phần MAP
                if (raw.equalsIgnoreCase("MAP")) {
                    readingMap = true;
                    continue;
                }

                // kết thúc phần MAP
                if (raw.equalsIgnoreCase("BREAK")) {
                    break;
                }

                // thu thập dòng bản đồ
                if (readingMap) {
                    lines.add(raw);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi đọc map: " + e.getMessage(), e);
        }

        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy Level " + levelIndex + " trong " + resourcePath);
        }

        return lines;
    }
}

