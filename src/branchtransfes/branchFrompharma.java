/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package branchtransfes;

import static branchtransfes.btrModel.loadTransferItems;
import static branchtransfes.utils.alert;
import static branchtransfes.utils.selectItem;
import java.awt.Color;
import java.io.IOException;
import java.net.URL;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.ResizeFeatures;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

/**
 *
 * @author samue
 */
public class branchFrompharma implements Initializable {

    @FXML
    private TableView<btrModel> table;
    @FXML
    private TableColumn<btrModel, ?> col_ItemCode;
    @FXML
    private TableColumn<btrModel, ?> col_ItemName;
    @FXML
    private TableColumn<btrModel, ?> col_OrdQty;
    @FXML
    private TableColumn<btrModel, ?> Col_SalePrice;
    @FXML
    private TableColumn<btrModel, ?> col_qtrypcswho;
    @FXML
    private TableColumn<btrModel, ?> col_Uom;
    @FXML
    private TableColumn<btrModel, ?> col_ItemSerial;
    @FXML
    private Button btnSearchBtr;
    @FXML
    private TextField txtSearchBtr;
    @FXML
    private TableColumn<btrModel, ?> col_packSize;
    @FXML
    private RadioButton rdrcv;
    @FXML
    private RadioButton rditf;
    @FXML
    private ToggleGroup transferOtions;
    @FXML
    private ComboBox<?> fromlocation;
    @FXML
    private ComboBox<?> tolocation;
    @FXML
    private TextField txtJounalNumber;
    @FXML
    private Button btniMPORT;
    @FXML
    private TableColumn<?, ?> col_expiryDate;
    @FXML
    private Button btnDbConfig;
    @FXML
    private Label itemCount;
    utils utiils = new utils();
    searchPopUpModel popUp;
    Connection conn = new dbConnection().db2connection();

    String type = "RCV";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initComponets();
    }

    public void initComponets() {

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                if (popupStage("").isShowing()) {
                    popupStage("").hide();
                }
                // label.setText("pop notifications");
                popupStage("").show();

            } else {
                popupStage("").hide();
            }
            Scene currentScene = table.getScene();
            currentScene.getRoot().addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
                // Check if the click occurred outside of the pop-up window

            });
        });

//
        btnDbConfig.setOnAction(e -> {
            Stage popupStage = new Stage();
            Scene popupScene = new Scene(new Label("Pop-up content"));
            if (popupStage.isShowing()) {
                popupStage.hide();
                // popupStage.show();  // Close the pop-up if it's already open
            } else {
                popupStage.setScene(popupScene);
                popupStage.setAlwaysOnTop(true);
                popupStage.show();  // Open the pop-up if it's not open
            }
            //utiils.loadwindow("savedbConfig.fxml");

        });
        txtSearchBtr.setOnKeyReleased(e -> {
            filter();
        });
        rdrcv.setOnAction(e -> {
            table.getItems().clear();

        });
        rditf.setOnAction(e -> {
            table.getItems().clear();
        });
        btnSearchBtr.setOnAction(e -> {
            //   loadwindow("btrlist.fxml");
            if (rdrcv.isSelected()) {
                type = "RCV";
            } else if (rditf.isSelected()) {
                type = "ITF";
            }
            popUp = selectItem(this, type);
            if (popUp == null) {

            } else {
                inittable(popUp.getBtrNumber(), type);
                txtJounalNumber.setText(popUp.getBtrNumber());
                itemCount.setText(String.valueOf(table.getItems().size()));
            }

        });
        btniMPORT.setOnAction(e -> {
            ImportdataTodb2("NGONG_RD", "4107");
        });
    }

    public void inittable(String btrNumber, String type) {
        col_ItemCode.setCellValueFactory(new PropertyValueFactory("itemCode"));
        col_ItemName.setCellValueFactory(new PropertyValueFactory("itemName"));
        col_OrdQty.setCellValueFactory(new PropertyValueFactory("ordQty"));
        Col_SalePrice.setCellValueFactory(new PropertyValueFactory("salePrice"));
        col_Uom.setCellValueFactory(new PropertyValueFactory("uom"));
        col_packSize.setCellValueFactory(new PropertyValueFactory("conversion"));
        col_ItemSerial.setCellValueFactory(new PropertyValueFactory("itemSerial"));
        col_expiryDate.setCellValueFactory(new PropertyValueFactory("expiryDate"));
        col_qtrypcswho.setCellValueFactory(new PropertyValueFactory("wholePcs"));

        table.setItems(loadTransferItems(btrNumber, type));
    }

    void ImportdataTodb2(String itemLocation, String jounalNumber) {

        ArrayList<String> SQLs = new ArrayList();
        ArrayList<String> notAvaibleSQLs = new ArrayList();
        try {
            Statement smt = conn.createStatement();
            for (int i = 0; i < table.getItems().size(); i++) {
                String item_code = table.getColumns().get(0).getCellData(i).toString();
                String item_name = table.getColumns().get(1).getCellData(i).toString();
                String uom = table.getColumns().get(2).getCellData(i).toString();
                String qty = table.getColumns().get(4).getCellData(i).toString();
                String ledgerNumber = "ADJUSTMENT";
                String ledgerType = "STK";
                String trnType = "ADJ";
                String itemPrice = table.getColumns().get(5).getCellData(i).toString();
                String conversion = table.getColumns().get(3).getCellData(i).toString();
                double ordqty = Double.valueOf(conversion) * Double.valueOf(qty);
                double amountValue = Double.valueOf(itemPrice) * Double.valueOf(qty);
                String orderType = "RCV".equals(type) ? "INN" : "OUT";
                String journalType = "ADJ";
                String approved = "N";
                String itemSerial = table.getColumns().get(6).getCellData(i).toString() == null ? "-"
                        : table.getColumns().get(6).getCellData(i).toString();
                String exipryDate = table.getColumns().get(7).getCellData(i).toString().isEmpty() ? "-"
                        : table.getColumns().get(7).getCellData(i).toString();

                String sql = "INSERT INTO stock_trns (item_code, description, suom,ledger_number,item_location,"
                        + "ledger_type,trn_type,conversion,ord_quantity,amount_value,order_type,journal_type,approved,"
                        + "item_serial,journal_number,date_exported,IUOM,QUANTITY) "
                        + "VALUES ('" + item_code + "', '" + item_name + "', '" + uom + "',"
                        + "'" + ledgerNumber + "','" + itemLocation + "','" + ledgerType + "',"
                        + "'" + trnType + "'," + conversion + "," + ordqty + "," + amountValue + ","
                        + "'" + orderType + "','" + journalType + "','" + approved + "','" + itemSerial + "'," + jounalNumber + ","
                        + "'" + exipryDate + "','" + uom + "','" + qty + "'"
                        + ")";
                // System.out.println(sql);

                if (checkifExist(item_code) > 0) {
                    SQLs.add(sql);
                } else {
                    String item
                            = "ITEM " + item_name + " Transfered QTY " + qty + " \n Does Not Exists in Maliplus Database \n";
                    notAvaibleSQLs.add(item);
                    System.out.println(item_code + " does not exists");
                }

            }
            for (String s : SQLs) {
                // System.out.println(s);
                smt.addBatch(s);
            }

            smt.executeBatch();
            conn.commit();
            conn.setAutoCommit(true);

            table.getItems().clear();
            if (!notAvaibleSQLs.isEmpty()) {
                alert(notAvaibleSQLs.toString());
            } else {
                alert("Items Imported To Maliplus");
            }
            smt.clearBatch();
            smt.closeOnCompletion();
        } catch (SQLException ex) {
            Logger.getLogger(branchFrompharma.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    int checkifExist(String item_code) {
        int count = 0;
        try {
            String selectSql = "SELECT COUNT(*) FROM item_master WHERE item_code = ?";
            PreparedStatement selectStmt = conn.prepareStatement(selectSql);
            selectStmt.setString(1, item_code);
            ResultSet rs = selectStmt.executeQuery();
            rs.next();
            count = rs.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(branchFrompharma.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    private void filter() {
        FilteredList<btrModel> filteredData = new FilteredList<>(table.getItems(), p -> true);
        txtSearchBtr.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            filteredData.setPredicate(btrmodel -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return Stream.of(btrmodel.getItemName(), btrmodel.getItemCode())
                        .filter(Objects::nonNull)
                        .anyMatch(value -> value.toLowerCase().contains(lowerCaseFilter));
            });
        });
        SortedList<btrModel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }

    public Stage popupStage(TableView tableView, String note) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.initOwner(tableView.getScene().getWindow());
        Label label = new Label();
        label.setText(note);
        Scene popupScene = new Scene(label, 200, 100);
        popupStage.setScene(popupScene);
        return popupStage;

    }

    Stage popupStage(String nt) {
        Stage popupStage = new Stage();
        popupStage.initOwner(table.getScene().getWindow());
        Label label = new Label();
        Scene popupScene = new Scene(label, 200, 100);
        popupStage.setScene(popupScene);

// Add mouse event filter to close the pop-up
// Add listener to display the pop-up when a row is clicked
        return popupStage;

    }

    void pop() {

        

    }

}
