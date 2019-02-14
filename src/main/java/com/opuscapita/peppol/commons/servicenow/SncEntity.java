package com.opuscapita.peppol.commons.servicenow;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SncEntity {

    @SerializedName("u_short_descr")
    private String shortDescription;
    @SerializedName("u_bsc")
    private String bsc;
    @SerializedName("u_business_group")
    private String businessGroup;
    @SerializedName("u_correlation_id")
    private String correlationId;
    @SerializedName("u_customer_id")
    private String customerId;
    @SerializedName("u_det_descr")
    private String detailedDescription;
    @SerializedName("u_from")
    private String from;
    @SerializedName("u_occurred_on")
    private String occurredOn;
    @SerializedName("u_priority")
    private String priority;
    @SerializedName("u_email_to")
    private String emailTo;

    public SncEntity(String shortDescription, String detailedDescription, String correlationId, String customerId, Integer priority) {
        this.shortDescription = shortDescription;
        this.detailedDescription = detailedDescription;
        if (correlationId.length() > 80) {
            correlationId = correlationId.substring(0, 79);
        }

        this.correlationId = correlationId;
        this.customerId = customerId;
        if (priority < 0 || priority > 5) {
            priority = 0;
        }

        if (priority == 0) {
            priority = 3;
        }

        this.priority = priority.toString();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.occurredOn = dateFormat.format(new Date());
        this.emailTo = "info.no@opuscapita.com";
    }

    public String getShortDescription() {
        return this.shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getBsc() {
        return this.bsc;
    }

    public void setBsc(String bsc) {
        this.bsc = bsc;
    }

    public String getBusinessGroup() {
        return this.businessGroup;
    }

    public void setBusinessGroup(String businessGroup) {
        this.businessGroup = businessGroup;
    }

    public String getCorrelationId() {
        return this.correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getCustomerId() {
        return this.customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDetailedDescription() {
        return this.detailedDescription;
    }

    public void setDetailedDescription(String detailedDescription) {
        this.detailedDescription = detailedDescription;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getOccurredOn() {
        return this.occurredOn;
    }

    public void setOccurredOn(String occurredOn) {
        this.occurredOn = occurredOn;
    }

    public String getPriority() {
        return this.priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getEmailTo() {
        return this.emailTo;
    }

    public void setEmailTo(String emailTo) {
        this.emailTo = emailTo;
    }
}
