package com.zetta.afcs;

import java.util.ArrayList;
import java.util.List;

public class FareHelper {
    private int baseKm = 5;
    private double baseFare = 8.00;
    private double succeedingFare = 0.90;
    private double student_discount = 0.20;
    private double senior_discount = 0.20;
    private double pwd_discount = 0.20;

    private Vehicle vehicle;
    private String routeNo;
    private String from;
    private String to;
    private int regularCount;
    private int studentCount;
    private int seniorCount;
    private int pwdCount;

    private double totalRegularFare;
    private double totalStudentFare;
    private double totalSeniorFare;
    private double totalPwdFare;

    /**
     * Initialized a fare helper object.
     * @param routeNo, The route number.
     * @param from, The origin of the passenger.
     * @param to, The destination of the passenger.
     * @param regular, The number of regular passengers.
     * @param student, The number of student passengers.
     * @param senior, The number of senior passengers.
     * @param pwd, The number of pwd passengers.
     */
    public FareHelper(Vehicle vehicle, String routeNo, String from, String to, int regular, int student, int senior, int pwd) {
        this.vehicle = vehicle;
        this.routeNo = routeNo;
        this.from = from;
        this.to = to;
        this.regularCount = regular;
        this.studentCount = student;
        this.seniorCount = senior;
        this.pwdCount = pwd;

        this.buildFares();
    }

    private void buildFares() {
        List<RouteInfo> routes = new ArrayList<>();
        double regularFare;
        double studentFare;
        double seniorFare;
        double pwdFare;

        if (this.routeNo.equals("1"))
            routes = this.vehicle.getRouteInfoOne();
        if (this.routeNo.equals("2"))
            routes = this.vehicle.getRouteInfoTwo();

        if (routes.size() > 0) {
            double originKm = this.getDistanceKm(this.from, routes);
            double destinationKm = this.getDistanceKm(this.to, routes);
            double distanceKm = destinationKm - originKm;

            if (distanceKm <= this.baseKm) {
                regularFare = Math.round(this.baseFare * 2) / 2.0f;
                studentFare = Math.round((this.baseFare - (this.baseFare * this.student_discount)) * 2) / 2.0f;
                seniorFare = Math.round((this.baseFare - (this.baseFare * this.senior_discount)) * 2) / 2.0f;
                pwdFare = Math.round((this.baseFare - (this.baseFare * this.pwd_discount)) * 2) / 2.0f;
            } else {
                double remKm = distanceKm - this.baseKm;
                // Round the remKm to nearest ones place.
                // Ex: 0.3 = 1
                // Ex: 0.7 = 1
                // Ex: 2.3 = 3
                // Ex: 2.7 = 3
                remKm = Math.ceil(remKm);

                double remFare = remKm * this.succeedingFare;
                remFare = Math.round(remFare);

                regularFare = Math.round((this.baseFare + remFare) * 2 / 2.0f);
                studentFare = Math.round(((this.baseFare + remFare) - ((this.baseFare + remFare) * this.student_discount)) * 2) / 2.0f;
                seniorFare = Math.round(((this.baseFare + remFare) - ((this.baseFare + remFare) * this.senior_discount)) * 2) / 2.0f;
                pwdFare = Math.round(((this.baseFare + remFare) - ((this.baseFare + remFare) * this.pwd_discount)) * 2) / 2.0f;
            }

            regularFare = regularFare * this.regularCount;
            studentFare = studentFare * this.studentCount;
            seniorFare = seniorFare * this.seniorCount;
            pwdFare = pwdFare * this.pwdCount;

            this.totalRegularFare = regularFare;
            this.totalStudentFare = studentFare;
            this.totalSeniorFare = seniorFare;
            this.totalPwdFare = pwdFare;
        }
    }

    /**
     * Gets the total fare.
     * @return, The total amount as fare of the passenger(s).
     */
    public double getTotalFare() {
//        double totalFare = 0;
//        List<RouteInfo> routes = new ArrayList<>();
//        double regularFare = 0;
//        double studentFare = 0;
//        double seniorFare = 0;
//        double pwdFare = 0;
//
//        if (this.routeNo.equals("1"))
//            routes = this.vehicle.getRouteInfoOne();
//        if (this.routeNo.equals("2"))
//            routes = this.vehicle.getRouteInfoTwo();
//
//        if (routes.size() > 0) {
//            double originKm = this.getDistanceKm(this.from, routes);
//            double destinationKm = this.getDistanceKm(this.to, routes);
//            double distanceKm = destinationKm - originKm;
//
//            if (distanceKm <= this.baseKm) {
//                regularFare = Math.round(this.baseFare * 2) / 2.0f;
//                studentFare = Math.round((this.baseFare - (this.baseFare * this.student_discount)) * 2) / 2.0f;
//                seniorFare = Math.round((this.baseFare - (this.baseFare * this.senior_discount)) * 2) / 2.0f;
//                pwdFare = Math.round((this.baseFare - (this.baseFare * this.pwd_discount)) * 2) / 2.0f;
//            } else {
//                double remKm = distanceKm - this.baseKm;
//                // Round the remKm to nearest ones place.
//                // Ex: 0.3 = 1
//                // Ex: 0.7 = 1
//                // Ex: 2.3 = 3
//                // Ex: 2.7 = 3
//                remKm = Math.ceil(remKm);
//
//                double remFare = remKm * this.succeedingFare;
//                remFare = Math.round(remFare);
//
//                regularFare = Math.round((this.baseFare + remFare) * 2 / 2.0f);
//                studentFare = Math.round(((this.baseFare + remFare) - ((this.baseFare + remFare) * this.student_discount)) * 2) / 2.0f;
//                seniorFare = Math.round(((this.baseFare + remFare) - ((this.baseFare + remFare) * this.senior_discount)) * 2) / 2.0f;
//                pwdFare = Math.round(((this.baseFare + remFare) - ((this.baseFare + remFare) * this.pwd_discount)) * 2) / 2.0f;
//            }
//
//            regularFare = regularFare * this.regularCount;
//            studentFare = studentFare * this.studentCount;
//            seniorFare = seniorFare * this.seniorCount;
//            pwdFare = pwdFare * this.pwdCount;
//
//            totalFare = regularFare + studentFare + seniorFare + pwdFare;
//        }
//
//        return  totalFare;

        return this.totalRegularFare + this.totalStudentFare + this.totalSeniorFare + this.totalPwdFare;
    }

    /**
     * Gets the set value of the distance(km) of a route or location.
     * @param location, The name of the location.
     * @param routes, The list of routes.
     * @return, The set distance.
     */
    private double getDistanceKm(String location, List<RouteInfo> routes) {
        double distanceKm = 0;
        for (RouteInfo routeInfo : routes) {
            if (routeInfo.getLocation().equals(location)) {
                distanceKm = routeInfo.distanceKm;
                break;
            }
        }

        return distanceKm;
    }

    /**
     * Rounds the decimal to the nearest 0.50.
     * @param d, The decimal to round.
     * @return, The rounded decimal.
     */
    private double roundToHalf(double d) {
        return Math.round(d * 2) / 2.0f;
    }

    /**
     * Gets the distance travelled from point A to point B.
     * @return
     */
    public double getTravelDistance() {
        List<RouteInfo> routes = new ArrayList<>();
        if (this.routeNo.equals("1"))
            routes = this.vehicle.getRouteInfoOne();
        if (this.routeNo.equals("2"))
            routes = this.vehicle.getRouteInfoTwo();
        double originKm = this.getDistanceKm(this.from, routes);
        double destinationKm = this.getDistanceKm(this.to, routes);

        return destinationKm - originKm;
    }

    public double getTotalRegularFare() {
        return this.totalRegularFare;
    }

    public double getTotalStudentFare() {
        return this.totalStudentFare;
    }

    public double getTotalSeniorFare() {
        return this.totalSeniorFare;
    }

    public double getTotalPwdFare() {
        return this.totalPwdFare;
    }
}