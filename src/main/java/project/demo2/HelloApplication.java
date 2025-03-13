package project.demo2;

import javafx.application.Application;
import javafx.animation.RotateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

public class HelloApplication extends Application {

    private List<String> imagePaths = List.of(
            "/project/demo2/bmw.jpg",
            "/project/demo2/audi.jpg",
            "/project/demo2/aston.jpg",
            "/project/demo2/bantley.jpg",
            "/project/demo2/benz.jpg",
            "/project/demo2/mclaren.jpg",
            "/project/demo2/lexus.jpg",
            "/project/demo2/jaguar.jpg",
            "/project/demo2/royce.jpg"
    );

    private int currentIndex = 0;
    private ImageView fullImageView = new ImageView();
    private Label imageLabel = new Label();
    private BorderPane root;
    private VBox fullImageViewContainer;
    private double currentRotation = 0; // Track current rotation

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Rich Internet Image Gallery");

        root = new BorderPane();

        // Create Thumbnail Grid
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);

        int col = 0, row = 0;
        for (String imagePath : imagePaths) {
            Image image = loadImage(imagePath);
            if (image != null) {
                ImageView thumbnail = new ImageView(image);
                thumbnail.setFitWidth(200);
                thumbnail.setFitHeight(200);
                thumbnail.getStyleClass().add("thumbnail");

                thumbnail.setOnMouseClicked(e -> showFullImage(imagePath));

                gridPane.add(thumbnail, col, row);
                col++;
                if (col == 3) {
                    col = 0;
                    row++;
                }
            } else {
                System.err.println("Error loading image: " + imagePath);
            }
        }

        // Navigation Controls
        Button prevButton = new Button("Previous");
        Button nextButton = new Button("Next");
        Button backButton = new Button("Back to Gallery");

        prevButton.setOnAction(e -> showPreviousImage());
        nextButton.setOnAction(e -> showNextImage());
        backButton.setOnAction(e -> root.setCenter(gridPane));

        HBox navigation = new HBox(10, prevButton, nextButton, backButton);
        navigation.setAlignment(Pos.CENTER);
        navigation.setPadding(new Insets(10));
        navigation.getStyleClass().add("navigation-bar");

        // Full Image View
        fullImageView.setFitWidth(800);
        fullImageView.setPreserveRatio(true);

        fullImageViewContainer = new VBox(fullImageView, imageLabel, navigation);
        fullImageViewContainer.setAlignment(Pos.CENTER);

        // Set Initial View
        root.setCenter(gridPane);
        root.getStyleClass().add("main-container");

        Scene scene = new Scene(root, 1000, 700);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/project/demo2/styles.css")).toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showFullImage(String imagePath) {
        currentIndex = imagePaths.indexOf(imagePath);
        Image image = loadImage(imagePath);
        if (image != null) {
            fullImageView.setImage(image);
            root.setCenter(fullImageViewContainer);
            String imageName = imagePath.substring(imagePath.lastIndexOf("/") + 1);
            imageLabel.setText(imageName);
            fullImageView.setRotate(currentRotation); // Set current rotation
        } else {
            System.err.println("Error displaying full image: " + imagePath);
        }
    }

    private void showPreviousImage() {
        if (currentIndex > 0) {
            currentIndex--;
            rotateAndShowImage();
        }
    }

    private void showNextImage() {
        if (currentIndex < imagePaths.size() - 1) {
            currentIndex++;
            rotateAndShowImage();
        }
    }

    private void rotateAndShowImage() {
        double newRotation = currentRotation + 360; // Rotate by 360 degrees
        RotateTransition rotateTransition = new RotateTransition(Duration.millis(1000), fullImageView); //Increase duration
        rotateTransition.setFromAngle(currentRotation);
        rotateTransition.setToAngle(newRotation);
        rotateTransition.setOnFinished(e -> {
            currentRotation = newRotation % 360; // Update currentRotation
            showFullImage(imagePaths.get(currentIndex)); // Show the new image
        });
        rotateTransition.play();
    }

    private Image loadImage(String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is != null) {
                return new Image(is);
            } else {
                System.err.println("Image not found: " + path);
                return null;
            }
        } catch (IOException e) {
            System.err.println("Error loading image: " + path + " - " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}