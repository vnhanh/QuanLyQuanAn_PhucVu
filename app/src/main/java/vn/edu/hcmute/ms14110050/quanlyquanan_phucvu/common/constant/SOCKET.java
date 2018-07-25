package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant;

/**
 * Created by Vo Ngoc Hanh on 6/15/2018.
 */

public class SOCKET {
//    public static String SERVER_IP = "192.168.103.2";
    public static String SERVER_IP = "qlnh.herokuapp.com";
//    public static String BASE_URL = "http://"+SERVER_IP+":8080/";
    public static String BASE_URL = "https://"+SERVER_IP+"/";

    public static String SOCKET_SERVER_DISCONNECT = "disconnect";

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
    public static String SOCKET_EVENT_UPDATE_ORDER = "server-update-order";
    public static String SOCKET_EVENT_CREATE_DETAIL_ORDER = "server-create-detail-order";
    public static String SOCKET_EVENT_UPDATE_DETAIL_ORDER = "server-update-detail-order";
    public static String SOCKET_EVENT_REMOVE_DETAIL_ORDER = "server-remove-detail-order";
    public static String SOCKET_EVENT_REMOVE_ORDER = "server-remove-order";
    public static String SOCKET_EVENT_ADD_DETAIL_ORDER = "server-add-detail-order";
    public static String SOCKET_EVENT_UPDATE_STATUS_DETAIL_ORDER = "server-update-status-detail-order";

    public static String SOCKET_EVENT_REQUEST_DELEGACY_WAITER = "server-request-delegacy-waiter";
    public static String SOCKET_EVENT_RESPONSE_DELEGACY_WAITER = "server-response-delegacy-waiter";
}
