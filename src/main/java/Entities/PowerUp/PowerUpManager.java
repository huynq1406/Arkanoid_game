package Entities.PowerUp;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PowerUpManager {
    private List<PowerUp> powerUps;

    public PowerUpManager() {
        powerUps = new ArrayList<>();
    }

    public void addPowerUp(PowerUp p) {
        powerUps.add(p);
    }

    public void updateAll() {
        Iterator<PowerUp> it = powerUps.iterator();
        while (it.hasNext()) {
            PowerUp p = it.next();
            if (p instanceof IPowerUp) {
                IPowerUp ip = p;
                ip.update();
                // xóa nếu power-up đã hết hiệu lực hoặc ra khỏi màn hình
                if (!ip.isActive() && p.getY() > 800) {
                    it.remove();
                }
            }
        }
    }

    public List<PowerUp> getPowerUps() {
        return powerUps;
    }
}

