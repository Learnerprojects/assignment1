module project.demo2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens project.demo2 to javafx.fxml;
    exports project.demo2;
}