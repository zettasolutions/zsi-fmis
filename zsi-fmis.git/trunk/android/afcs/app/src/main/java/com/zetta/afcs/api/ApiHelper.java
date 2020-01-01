package com.zetta.afcs.api;

public class ApiHelper {
    /**
     * The api that will be called by the application.
     */
    public static String apiURL = "http://dev.zetta-solutions.net:8886/api/getrecords";

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

}