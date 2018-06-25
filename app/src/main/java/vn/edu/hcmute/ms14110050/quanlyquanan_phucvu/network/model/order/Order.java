package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */

public class Order {
    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("customer_name")
    @Expose
    String customerName;
    @SerializedName("waiter_username ")
    @Expose
    String waiterUsername;
    @SerializedName("waiter_fullname ")
    @Expose
    String waiterFullname;
    @SerializedName("cashier_username ")
    @Expose
    String cashierUsername;
    @SerializedName("cashier_fullname ")
    @Expose
    String cashierFullname;
    @SerializedName("flag_status")
    @Expose
    @OrderFlag
    int statusFlag;
    @SerializedName("time_creadted")
    @Expose
    long createdTime;
    @SerializedName("paid_cost")
    @Expose
    long paidCost;
    @SerializedName("final_cost")
    @Expose
    long finalCost;
    @SerializedName("description")
    @Expose
    String description;
    @SerializedName("detail_orders")
    @Expose
    ArrayList<DetailOrder> detailOrders;
    @SerializedName("tables")
    @Expose
    ArrayList<String> tables;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getWaiterUsername() {
        return waiterUsername;
    }

    public void setWaiterUsername(String usernameWaiter) {
        this.waiterUsername = usernameWaiter;
    }

    public String getWaiterFullname() {
        return waiterFullname;
    }

    public void setWaiterFullname(String waiterFullname) {
        this.waiterFullname = waiterFullname;
    }

    public String getCashierUsername() {
        return cashierUsername;
    }

    public void setCashierUsername(String cashierUsername) {
        this.cashierUsername = cashierUsername;
    }

    public String getCashierFullname() {
        return cashierFullname;
    }

    public void setCashierFullname(String cashierFullname) {
        this.cashierFullname = cashierFullname;
    }

    public int getStatusFlag() {
        return statusFlag;
    }

    public void setStatusFlag(@OrderFlag int statusFlag) {
        this.statusFlag = statusFlag;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public long getPaidCost() {
        return paidCost;
    }

    public void setPaidCost(long paidCost) {
        this.paidCost = paidCost;
    }

    public long getFinalCost() {
        return finalCost;
    }

    public void setFinalCost(long finalCost) {
        this.finalCost = finalCost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<DetailOrder> getDetailOrders() {
        return detailOrders;
    }

    public void setDetailOrders(ArrayList<DetailOrder> detailOrders) {
        this.detailOrders = detailOrders;
    }

    public ArrayList<String> getTables() {
        if (tables == null) {
            tables = new ArrayList<>();
        }
        return tables;
    }

    public void setTables(ArrayList<String> tables) {
        this.tables = tables;
    }
}
