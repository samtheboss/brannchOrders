/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package branchtransfes;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author samue
 */
public class utils {
      public static String convertListToString(List<String> arraylist) {
        String str = "";
        if (arraylist.isEmpty()) {
            return "''";
        }
        str = arraylist.stream().map((arr) -> "'" + arr + "',").reduce(str, String::concat);
        return str.length() > 0 ? str.substring(0, str.length() - 1) : "";
    }
      
   public  void loadwindow(String uiname) {

        try {
            
            Parent root = FXMLLoader.load(getClass().getResource(uiname));
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(branchFrompharma.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static searchPopUpModel selectItem(Initializable controller, String type) {
        searchPopUpModel selectbtr = null;
        try {
            FXMLLoader loader = new FXMLLoader(controller.getClass().getResource(
                    "btrlist.fxml"
            ));
            loader.setController(new BtrlistController(type));
            Parent parent = loader.load();

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("BTRS");
            stage.setScene(new Scene(parent));
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.setAlwaysOnTop(true);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            selectbtr = loader.<BtrlistController>getController().getSelectedItem();
        } catch (IOException ex) {
            Logger.getLogger(branchFrompharma.class.getName()).log(Level.SEVERE, null, ex);
        }
        return selectbtr;
    }

    public static  void alert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
