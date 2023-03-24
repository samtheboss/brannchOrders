/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package branchtransfes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 *
 * @author samue
 */
public class BranchTransfes extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
         Button popupButton = new Button("Open Pop-up");
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
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
