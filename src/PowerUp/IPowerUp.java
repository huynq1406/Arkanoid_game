package PowerUp;

public interface IPowerUp {
    void activate();        // Kích hoạt hiệu ứng
    void update();          // Cập nhật trạng thái mỗi frame
    boolean isActive();     // Kiểm tra còn hiệu lực không
}

