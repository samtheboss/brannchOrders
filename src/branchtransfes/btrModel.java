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
public class btrModel {

    String itemCode;
    String itemName;
    double ordQty;
    double salePrice;
    String itemSerial;
    double stock;
    String uom;
    double conversion;
    double qty;
    String expiryDate;
    String wholePcs;

    public btrModel() {
    }

    public btrModel(String itemCode, String itemName, double ordQty, double salePrice, String itemSerial, double stock, String uom, double conversion, double qty, String expiryDate, String wholePcs) {
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.ordQty = ordQty;
        this.salePrice = salePrice;
        this.itemSerial = itemSerial;
        this.stock = stock;
        this.uom = uom;
        this.conversion = conversion;
        this.qty = qty;
        this.expiryDate = expiryDate;
        this.wholePcs = wholePcs;
    }

    public String getWholePcs() {
        return wholePcs;
    }

    public void setWholePcs(String wholePcs) {
        this.wholePcs = wholePcs;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public double getConversion() {
        return conversion;
    }

    public void setConversion(double conversion) {
        this.conversion = conversion;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getOrdQty() {
        return ordQty;
    }

    public void setOrdQty(double ordQty) {
        this.ordQty = ordQty;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public String getItemSerial() {
        return itemSerial;
    }

    public void setItemSerial(String itemSerial) {
        this.itemSerial = itemSerial;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public static ObservableList<btrModel> loadTransferItems(String btrNumber, String type) {
        PreparedStatement pst;
        Connection conn = new dbConnection().sqlServerconnection();
        ResultSet rs;
        ObservableList<btrModel> data = FXCollections.observableArrayList();
        btrModel btrModel = new btrModel();

        String selectItems = "";
        if (type.equals("ITF")) {
            selectItems = "select distinct BRANCHTRANS_DT.*, STOCKTRANSOUT.EXPIRY_DATE,STOCKTRANSOUT.BATCH_NUM,"
                    + "STOCKTRANSOUT.qty_out  AS TRNQTY from BRANCHTRANS_DT "
                    + "inner JOIN STOCKTRANSOUT ON BRANCHTRANS_DT.STK_NUM = STOCKTRANSOUT.STK_NUM"
                    + " where BTRANS_NUM ='" + btrNumber + "' ORDER BY INV_CODE";
        } else if (type.equals("RCV")) {
            selectItems = "select  distinct BRN_DT.*, STOCKTRANS.EXPIRY_DATE,STOCKTRANS.qty_in as TRNQTY,"
                    + "STOCKTRANS.BATCH_NUM from BRN_DT  inner JOIN STOCKTRANS "
                    + "ON BRN_DT.STK_NUM = STOCKTRANS.STK_NUM where BRN_NUM ='" + btrNumber + "' ORDER BY INV_CODE";
        }
        System.out.println(selectItems);
        try {
            pst = conn.prepareStatement(selectItems);
            rs = pst.executeQuery();
            while (rs.next()) {

                btrModel.setItemCode(rs.getString("INV_CODE"));
                btrModel.setItemName(rs.getString("DESCRIPTION").toUpperCase());
                btrModel.setSalePrice(rs.getDouble("PRICE"));
                btrModel.setQty(rs.getDouble("pwqty"));
                //  btrModel.setItemSerial(rs.getString("BATCH_NUM"));
                btrModel.setExpiryDate(rs.getString("EXPIRY_DATE"));
                String packType = rs.getString("partwhole");
                String trimeddate = rs.getString("EXPIRY_DATE").substring(0, rs.getString("EXPIRY_DATE").length() - 10);
                String itemserial = trimeddate.replace("-", "");
                btrModel.setItemSerial(itemserial);
                String whpc; 
                int qty = (int) (rs.getDouble("TRNQTY") / rs.getDouble("PACKQTY"));
                if (packType.equals("W")) {
                    btrModel.setUom("WHO");
                  

                    btrModel.setConversion(rs.getDouble("PACKQTY"));
                    if ((rs.getDouble("TRNQTY")) % (rs.getDouble("PACKQTY")) > 0) {
                        btrModel.setOrdQty(rs.getDouble("TRNQTY"));
                        btrModel.setUom("PCS");
                        btrModel.setConversion(1);
                    } else {
                        btrModel.setOrdQty(qty);
                    }

                } else if (packType.equals("P")) {

                    btrModel.setUom("PCS");
                    btrModel.setOrdQty(rs.getDouble("TRNQTY"));
                    btrModel.setConversion(1);
                }
                
                int wh = (int) (qty - (rs.getDouble("pwqty")) % (rs.getDouble("PACKQTY")));
                if(wh <0){
                wh =0;
                }
                int pcs = (int) ((rs.getDouble("pwqty")) % (rs.getDouble("PACKQTY")));
                whpc = String.valueOf(wh) + " W " + String.valueOf(pcs) + " P ";
                btrModel.setWholePcs(whpc);
                data.add(btrModel);
                btrModel = new btrModel();

            }
            //corebak2012
        } catch (SQLException ex) {
            Logger.getLogger(btrModel.class.getName()).log(Level.SEVERE, null, ex);
        }

        return data;
    }

}
