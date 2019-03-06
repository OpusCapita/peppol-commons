package com.opuscapita.peppol.commons.template;

public class ApiListRestResponse {

    private String publicRoute;
    private String serviceRoute;

    public ApiListRestResponse(String publicRoute, String serviceRoute) {
        this.setPublicRoute(publicRoute);
        this.setServiceRoute(serviceRoute);
    }

    public String getPublicRoute() {
        return publicRoute;
    }

    public void setPublicRoute(String publicRoute) {
        this.publicRoute = publicRoute;
    }

    public String getServiceRoute() {
        return serviceRoute;
    }

    public void setServiceRoute(String serviceRoute) {
        this.serviceRoute = serviceRoute;
    }

    @Override
    public String toString() {
        return "ApiListRestResponse{publicRoute: " + publicRoute + ", serviceRoute: " + serviceRoute + "}";
    }
}