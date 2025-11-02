package Levels;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class LevelLoader {

    private LevelLoader() {}
    // LevelLoader.java
    public static List<String> load(String resourcePath, int levelIndex) {
        String path = resourcePath.startsWith("/") ? resourcePath.substring(1) : resourcePath;
        List<String> lines = new ArrayList<>();

        // 1. Thử từ classpath (resources)
        InputStream is = LevelLoader.class.getClassLoader().getResourceAsStream(resourcePath);

        // 2. Nếu không tìm thấy → thử đọc từ file hệ thống (dev mode)
        if (is == null) {
            System.out.println("Không tìm thấy trong classpath, thử đọc từ file: " + path);
            File file = new File(path); // tìm từ thư mục chạy (project root)
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

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            boolean reading = false;

            while ((line = br.readLine()) != null) {
                String raw = line;
                String t = raw.trim();
                if (t.isEmpty()) continue;

                if (t.equals(String.valueOf(levelIndex))) {
                    reading = true;
                    continue;
                }
                if (t.equalsIgnoreCase("BREAK") && reading) break;

                if (reading) lines.add(raw);
            }
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi đọc map: " + e.getMessage(), e);
        }

        if (lines.isEmpty())
            throw new IllegalArgumentException("Không tìm thấy Level " + levelIndex + " trong " + resourcePath);

        return lines;
    }
}