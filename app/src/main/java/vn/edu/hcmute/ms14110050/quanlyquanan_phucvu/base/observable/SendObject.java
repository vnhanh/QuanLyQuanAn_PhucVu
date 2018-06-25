package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.base.observable;

/**
 * Created by Vo Ngoc Hanh on 6/14/2018.
 */

public class SendObject<MODEL> {
    private int tag;
    private MODEL value;

    public SendObject(int tag, MODEL value) {
        this.tag = tag;
        this.value = value;
    }

    public int getTag() {
        return tag;
    }

    public MODEL getValue() {
        return value;
    }
}
