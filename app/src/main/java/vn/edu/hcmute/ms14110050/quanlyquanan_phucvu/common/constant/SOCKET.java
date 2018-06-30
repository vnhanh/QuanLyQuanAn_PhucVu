package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant;

/**
 * Created by Vo Ngoc Hanh on 6/15/2018.
 */

public class SOCKET {

    public static String BASE_URL = "http://192.168.1.101:8080/";

    public static int STATUS_SUCCESS = 200;
    public static int STATUS_BAD_REQUEST = 400;

    public static String SOCKET_SERVER_UPLOAD_EMPLOYEE = "server-update-employee";

    public static String SOCKET_EVENT_ADD_REGION = "server-add-region";
    public static String SOCKET_EVENT_UPDATE_REGION = "server-update-region";
    public static String SOCKET_EVENT_DELETE_REGION = "server-delete-region";

    public static String SOCKET_EVENT_ADD_TABLE = "server-add-table";
    public static String SOCKET_EVENT_ADD_TABLE_TO_ORDER = "server-add-table-to-order";
    public static String SOCKET_EVENT_REMOVE_ORDER_TABLE = "server-remove-table-from-order";
    public static String SOCKET_EVENT_UPDATE_ACTIVED_TABLE = "server-update-active-table";
    public static String SOCKET_EVENT_DELTE_TABLE = "server-delete-table";

    public static String SOCKET_EVENT_ADD_CATEGORY_FOOD = "server-add-categoryFood";
    public static String SOCKET_EVENT_UPDATE_CATEGORY_FOOD = "server-update-categoryFood";
    public static String SOCKET_EVENT_DELETE_CATEGORY_FOOD = "server-delete-categoryFood";

    public static String SOCKET_EVENT_ADD_FOOD = "server-add-food";
    public static String SOCKET_EVENT_UPDATE_FOOD = "server-update-food";
    public static String SOCKET_EVENT_ORDER_FOOD = "server-order-food";
    public static String SOCKET_EVENT_ADD_IMAGE_FOOD = "server-add-image-food";
    public static String SOCKET_EVENT_DELETE_IMAGE_FOOD = "server-delete-image-food";
    public static String SOCKET_EVENT_UPDATE_ACTIVE_FOOD = "server-update-ative-food";

    public static String SOCKET_EVENT_UPDATE_STATUS_ORDER = "server-update-status-order";
}
