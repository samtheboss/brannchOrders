package branchtransfes;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class PopUpExample extends Application {

    @Override
    public void start(Stage primaryStage) {
        Button popupButton = new Button("Open Pop-up");

        // Add event handler to button to open/close the pop-up
        popupButton.setOnAction(event -> {
            lll();
        });
        // Add button to scene
        StackPane root = new StackPane(popupButton);
        Scene scene = new Scene(root, 300, 250);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    void lll() {
        Stage popupStage = new Stage();
        Scene popupScene = new Scene(new Label("Pop-up content"));
        if (popupStage.isShowing()) {
            popupStage.hide();
            popupStage.show();  // Close the pop-up if it's already open
        } else {
            popupStage.setScene(popupScene);
            popupStage.show();  // Open the pop-up if it's not open
        }
    }

}
