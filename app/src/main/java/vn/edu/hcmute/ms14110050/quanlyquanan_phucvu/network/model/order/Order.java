package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.order;

import android.support.annotation.StringRes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.R;

/**
 * Created by Vo Ngoc Hanh on 6/24/2018.
 */

public class Order{
    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("number_customer")
    @Expose
    int customerNumber;
    @SerializedName("customer_name")
    @Expose
    String customerName;
    @SerializedName("waiter_username")
    @Expose
    String waiterUsername;
    @SerializedName("waiter_fullname")
    @Expose
    String waiterFullname;
    @SerializedName("cashier_username")
    @Expose
    String cashierUsername;
    @SerializedName("cashier_fullname")
    @Expose
    String cashierFullname;
    @SerializedName("flag_status")
    @Expose
    @OrderFlag
    int statusFlag;
    @SerializedName("time_created")
    @Expose
    String createdTime;
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
    @SerializedName("region_id")
    @Expose
    ArrayList<String> regionId;
    @SerializedName("delegacy")
    @Expose
    ArrayList<String> delegacies;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(int customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getCustomerName() {
        if (customerName == null) {
            customerName = "";
        }
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getWaiterUsername() {
        if (waiterUsername == null) {
            waiterUsername = "";
        }
        return waiterUsername;
    }

    public void setWaiterUsername(String usernameWaiter) {
        this.waiterUsername = usernameWaiter;
    }

    public String getWaiterFullname() {
        if (waiterFullname == null) {
            waiterFullname = "";
        }
        return waiterFullname;
    }

    public void setWaiterFullname(String waiterFullname) {
        this.waiterFullname = waiterFullname;
    }

    public String getCashierUsername() {
        if (cashierUsername == null) {
            cashierUsername = "";
        }
        return cashierUsername;
    }

    public void setCashierUsername(String cashierUsername) {
        this.cashierUsername = cashierUsername;
    }

    public String getCashierFullname() {
        if (cashierFullname == null) {
            cashierFullname = "";
        }
        return cashierFullname;
    }

    public void setCashierFullname(String cashierFullname) {
        this.cashierFullname = cashierFullname;
    }

    public int getStatusFlag() {
        return statusFlag;
    }

    @StringRes
    public int getStatusValue() {
        switch (statusFlag) {
            case OrderFlag.CREATING:
                return R.string.creating;

            case OrderFlag.PENDING:
                return R.string.pending;

            case OrderFlag.COOKING:
                return R.string.cooking;

            case OrderFlag.PREPARE:
                return R.string.prepare;

            case OrderFlag.EATING:
                return R.string.running;

            case OrderFlag.PAYING:
                return R.string.paying;

            case OrderFlag.COMPLETE:
                return R.string.complete;

            default:
                return 0;
        }
    }

    public void setStatusFlag(@OrderFlag int statusFlag) {
        this.statusFlag = statusFlag;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
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
        if (description == null) {
            description = "";
        }
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<DetailOrder> getDetailOrders() {
        if (detailOrders == null) {
            detailOrders = new ArrayList<>();
        }
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

    public ArrayList<String> getRegionId() {
        if (regionId == null) {
            regionId = new ArrayList<>();
        }
        return regionId;
    }

    public void setRegionId(ArrayList<String> regionId) {
        this.regionId = regionId;
    }

    public ArrayList<String> getDelegacies() {
        if (delegacies == null) {
            delegacies = new ArrayList<>();
        }
        return delegacies;
    }

    public void setDelegacies(ArrayList<String> delegacies) {
        this.delegacies = delegacies;
    }
}
