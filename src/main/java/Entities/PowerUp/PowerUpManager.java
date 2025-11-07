package Entities.PowerUp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;

public class PowerUpManager {
    private List<PowerUp> powerUps;

    public PowerUpManager() {
        powerUps = new ArrayList<>();
    }

    public void addPowerUp(PowerUp p) {
        powerUps.add(p);
    }

    public void renderAll(GraphicsContext g) {
        for (PowerUp p : powerUps) {
            p.render(g);
        }
    }

    public void updateAll() {
        Iterator<PowerUp> it = powerUps.iterator();
        while (it.hasNext()) {
            PowerUp p = it.next();
            p.update(); // Luôn gọi update (cho timer hoặc fall)

            // Điều kiện 1: Xóa nếu bị lỡ (chưa nhặt VÀ rơi ra khỏi màn hình)
            if (!p.isCollected() && p.isOffScreen(800)) {
                it.remove();
                continue;
            }

            // Điều kiện 2: Xóa nếu đã nhặt VÀ hiệu ứng đã kết thúc
            // - Loại tức thời: isCollected() = true, isActive() = false (vì ta đã xóa active=true)
            // - Loại có thời gian: isCollected() = true, isActive() = false (sau khi timer hết)
            if (p.isCollected() && !p.isActive()) {
                it.remove();
            }
        }
    }

    public List<PowerUp> getPowerUps() {
        return powerUps;
    }

    public void clearAll() {
        powerUps.clear();
    }
}

