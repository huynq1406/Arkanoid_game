package Entities.PowerUp;

public abstract class PowerUp implements IPowerUp {
    protected float x, y, size, speed;
    protected int type; // 1 = big paddle, 2 = small paddle, 3 = multi-ball, 4 = slow, 5 = extra life
    protected int duration = 30;
    protected int timer = 20;
    protected boolean active;


    public PowerUp(float x, float y, int type) {
        this.x = x;
        this.y = y;
        this.size = 50; // khung mặc định của Powerup
        this.speed = 7; // mặc định là 7
        this.type = type;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public abstract void deactivate();

    public void update() {
        if (active) {
            timer++;
            if (timer > duration) {
                deactivate();
            }
        } else {
            fall();
        }
    }

    public void fall() {
        y += speed;
    }

    public boolean isActive() {
        return active;
    }
}
