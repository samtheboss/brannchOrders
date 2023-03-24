/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package branchtransfes;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author samue
 */
public class BtrlistController implements Initializable {

    @FXML
    private TableColumn<searchPopUpModel, ?> colBtr;
    @FXML
    private TableColumn<searchPopUpModel, ?> colDate;
    @FXML
    private TableColumn<searchPopUpModel, ?> colAmount;
    @FXML
    private TableColumn<searchPopUpModel, ?> colUser;
    @FXML
    private TableColumn<searchPopUpModel, ?> colLocation;
    @FXML
    private TextField txtfilter;
    @FXML
    private Button btnSearch;
    @FXML
    private TableView<searchPopUpModel> searchPop;

    searchPopUpModel popmodel;

    String type;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initComponent();
    }

    public BtrlistController(String type) {
        this.type = type;
    }

    public void initComponent() {

        searchPop.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                getRow();
            }
        });
        txtfilter.setOnKeyReleased(e -> {
            filter();
        });
        initTable();
    }

    public void initTable() {
        colBtr.setCellValueFactory(new PropertyValueFactory("btrNumber"));
        colDate.setCellValueFactory(new PropertyValueFactory("date"));
        colAmount.setCellValueFactory(new PropertyValueFactory("amount"));
        colUser.setCellValueFactory(new PropertyValueFactory("user"));
        colLocation.setCellValueFactory(new PropertyValueFactory("location"));
        searchPop.setItems(searchPopUpModel.loadpopupList(type));

    }
    private void getRow() {
        if (searchPop.getSelectionModel().getSelectedItem().getBtrNumber() != null
                || !searchPop.getSelectionModel().getSelectedItem().getBtrNumber().isEmpty()) {
            popmodel = searchPop.getSelectionModel().getSelectedItem();
            closeWindow();
        }
    }

    public searchPopUpModel getSelectedItem() {
        return popmodel;

    }

    private void closeWindow() {
        ((Stage) searchPop.getScene().getWindow()).close();
    }

    private void filter() {
        FilteredList<searchPopUpModel> filteredData = new FilteredList<>(searchPop.getItems(), p -> true);
        txtfilter.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            filteredData.setPredicate(popupmodel -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return Stream.of(popupmodel.getBtrNumber(), popupmodel.getLocation(), popupmodel.getUser())
                        .filter(Objects::nonNull)
                        .anyMatch(value -> value.toLowerCase().contains(lowerCaseFilter));
            });
        });
        SortedList<searchPopUpModel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(searchPop.comparatorProperty());
        searchPop.setItems(sortedData);
    }

}
