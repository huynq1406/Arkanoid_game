package ObjectManager;

import Entities.Ball;
import Entities.Paddle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BallManagerTest {

    private BallManager ballManager;
    private Paddle testPaddle;

    @BeforeEach
    void setUp() {
        ballManager = new BallManager();
        testPaddle = new Paddle(0, 0, 100, 20);
    }

    @Test
    void testAddBall() {
        assertEquals(0, ballManager.getBalls().size(), "BallManager ban đầu phải rỗng");
        
        ballManager.addBall(new Ball(100, 100, 10));
        
        assertEquals(1, ballManager.getBalls().size(), "BallManager phải có 1 bóng sau khi thêm");
    }
}