package com.zetta.afcs.api;

public class ApiHelper {
    /**
     * The api that will be called by the application.
     */
    public static String apiURL = "http://afcs.zetta-solutions.net/api/getrecords";

    /**
     * The static sql code key exposed from the api.
     */
    public static String SqlCodeKey = "sqlCode";

    /**
     * The name of the sql command for the users exposed by the api.
     */
    public static String SqlCodeUsers = "A211"; //afcs_users_sel

    /**
     * The name of the sql command for the settings exposed by the api.
     */
    public static String SqlCodeSettings = "A212"; //afcs_assets_routes_ref_sel

    /**
     * The name of the sql command for the configurations exposed by the api.
     */
    public static String SqlCodeConfigurations = "A213"; //afcs_configurations_sel

    /**
     * The name of the sql command when buying a QR code exposed by the api.
     */
    public static String SqlCodeReLoader = "A215"; //afcs_loader_upd

    /**
     * The name of the sql command for the QR payment exposed by the api.
     */
    public static String SqlCodeQRPayment = "A219"; //afcs_qr_payment_upd

    /**
     * The name of the sql command when reloading a QR exposed by the api.
     */
    public static String SqlCodeReloadQR = "A220"; //afcs_reload_qr_upd

    /**
     * The name of the sql command for the saving of payments exposed by the api.
     */
    public static String SqlCodeSavePayment = "A221"; //afcs_save_payment_upd

    /**
     * The name of the sql command for the scanning the qr balance exposed by the api.
     */
    public static String SqlCodeScanQRBalance = "A222"; //afcs_scan_qr_balance_sel

    /**
     * The name of the sql command for the cancellation of the payment exposed by the api.
     */
    public static String SqlCodeCancelPayment = "A229"; //afcs_cancel_payment_upd

}