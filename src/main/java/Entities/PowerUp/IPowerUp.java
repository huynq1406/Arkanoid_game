package Entities.PowerUp;

public abstract interface IPowerUp {
    abstract void activate();        // Kích hoạt hiệu ứng
    abstract void update();          // Cập nhật trạng thái mỗi frame
    abstract boolean isActive();     // Kiểm tra còn hiệu lực không
}

