module Arkanoid {
    requires java.datatransfer;
    requires java.desktop;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;

    exports GameManager to javafx.graphics;
}