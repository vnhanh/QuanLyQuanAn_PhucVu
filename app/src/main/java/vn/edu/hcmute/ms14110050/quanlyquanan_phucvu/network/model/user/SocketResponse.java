package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.network.model.user;

/**
 * Created by Vo Ngoc Hanh on 6/15/2018.
 */

public class SocketResponse<MODEL> {
    int status;

    MODEL data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public MODEL getData() {
        return data;
    }

    public void setData(MODEL data) {
        this.data = data;
    }
}
