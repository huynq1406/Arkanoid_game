package PowerUp;

import Entities.Ball;
import Entities.Paddle;
import Entities.PowerUp.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PowerUpManagerTest {

    private PowerUpManager powerUpManager;
    
    @BeforeEach
    void setUp() {
        powerUpManager = new PowerUpManager();
    }

    @Test
    void testAddPowerUp() {
        assertEquals(0, powerUpManager.getPowerUps().size());
        powerUpManager.addPowerUp(new ExtraLifePowerUp(100, 100)); // Sửa x,y thành double
        assertEquals(1, powerUpManager.getPowerUps().size());
    }

    @Test
    void testUpdateAll_RemovesOffScreenPowerUp() {
        PowerUp offScreenPU = new ExtraLifePowerUp(100, 900); // Sửa x,y thành double
        
        powerUpManager.addPowerUp(offScreenPU);
        assertEquals(1, powerUpManager.getPowerUps().size());

        powerUpManager.updateAll();

        assertEquals(0, powerUpManager.getPowerUps().size(), 
            "Power-up ngoài màn hình phải bị xóa");
    }
}