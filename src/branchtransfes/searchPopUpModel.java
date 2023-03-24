/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package branchtransfes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author samue
 */
public class searchPopUpModel {

    String btrNumber;
    String date;
    double amount;
    String user;
    String type;
    String location;

    public searchPopUpModel() {
    }

    public searchPopUpModel(String btrNumber, String date, double amount, String user, String type, String location) {
        this.btrNumber = btrNumber;
        this.date = date;
        this.amount = amount;
        this.user = user;
        this.type = type;
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBtrNumber() {
        return btrNumber;
    }

    public void setBtrNumber(String btrNumber) {
        this.btrNumber = btrNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static ObservableList<searchPopUpModel> loadpopupList(String type) {
        System.out.println(type);
        PreparedStatement pst;
        Connection conn = new dbConnection().sqlServerconnection();
        ResultSet rs;
        ObservableList<searchPopUpModel> data = FXCollections.observableArrayList();
        searchPopUpModel searchPop = new searchPopUpModel();

        String searchBtr = "";
        if (type.equals("RCV")) {

            searchBtr = "select i.*,b.branch_name from brn_hd i "
                    + "INNER JOIN BRANCH b ON b.BRANCHCODE=i.BRANCHCODE  "
                    + " "
                    + "ORDER BY BRN_DATE DESC";

        } else if (type.equals("ITF")) {
            searchBtr = "select i.*, b.branch_name from branchtrans_hd i "
                    + "INNER JOIN BRANCH b ON b.BRANCHCODE=i.BRANCHCODE"
                    + " "
                    + " ORDER BY btrans_date DESC ";
        }//where  BTRANS_DATE BETWEEN DATEADD(day, -20, GETDATE()) AND GETDATE()WHERE  BRN_DATE BETWEEN DATEADD(day, -20, GETDATE()) AND GETDATE()
        try {
            pst = conn.prepareStatement(searchBtr);
            rs = pst.executeQuery();
            while (rs.next()) {
                if (type.equals("ITF")) {
                    searchPop.setAmount(rs.getDouble("btrans_net"));
                    searchPop.setBtrNumber(rs.getString("btrans_num"));
                    searchPop.setDate(rs.getString("btrans_date"));
                } else {
                    searchPop.setAmount(rs.getDouble("brn_net"));
                    searchPop.setBtrNumber(rs.getString("brn_num"));
                    searchPop.setDate(rs.getString("brn_date"));
                }
                searchPop.setUser(rs.getString("username"));
                searchPop.setLocation(rs.getString("branch_name"));
                data.add(searchPop);
                searchPop = new searchPopUpModel();
            }
        } catch (SQLException ex) {
            Logger.getLogger(searchPopUpModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return data;
    }

}
