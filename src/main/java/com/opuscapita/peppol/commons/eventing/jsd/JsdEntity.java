package com.opuscapita.peppol.commons.eventing.jsd;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringEscapeUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


/*

entity.setBsc(this.configuration.getSncBsc());
entity.setBusinessGroup(this.configuration.getSncBusinessGroup());
entity.setFrom(this.configuration.getSncFrom() + HOST);

this.shortDescription = shortDescription;
this.detailedDescription = detailedDescription;

*/
public class JsdEntity {

    //@SerializedName("u_short_descr")
    private String shortDescription;

    //@SerializedName("u_det_descr")
    private String detailedDescription;

/*
{
  "fields": {
    "project": {
      "id": "10800"
    },
    "issuetype": {
      "id": "10202"
    },
    "summary": "short description",
    "description": "hfhhhdf\neyyeyyyr\nyyrytytrre\nrereyyrrr"
  }
}
*/

/*    @SerializedName("u_bsc")
    private String bsc;
    @SerializedName("u_business_group")
    private String businessGroup;
    @SerializedName("u_correlation_id")
    private String correlationId;
    @SerializedName("u_customer_id")
    private String customerId;
    @SerializedName("u_from")
    private String from;
    @SerializedName("u_occurred_on")
    private String occurredOn;
    @SerializedName("u_priority")
    private String priority;
    @SerializedName("u_email_to")
    private String emailTo;
    */

    public JsdEntity(String shortDescription, String detailedDescription, String customerId) {
        //this.priority = "3";
        //this.customerId = customerId;
        //this.emailTo = "info.no@opuscapita.com";
        this.shortDescription = shortDescription;
        //this.correlationId = createCorrelationId();
        this.detailedDescription = detailedDescription;
        //this.occurredOn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public String getShortDescription() {
        return this.shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

/*
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
*/
    public String getDetailedDescription() {
        return this.detailedDescription;
    }

    public void setDetailedDescription(String detailedDescription) {
        this.detailedDescription = detailedDescription;
    }

    public String escape( String in ) {

      return StringEscapeUtils.escapeJson( in );

    }

    public String getAsJSON() {

      String newline = "\n";

      String json =

        "{" + newline +
        "  \"fields\": {" + newline +
        "    \"project\": {" + newline +
        "      \"id\": \"10800\"" + newline +
        "    }," + newline +
        "    \"issuetype\": {" + newline +
        "      \"id\": \"10202\"" + newline +
        "    }," + newline +
        "    \"summary\": \""+this.escape(this.getShortDescription())+"\"," + newline +
        "    \"description\": \""+ this.escape( this.getDetailedDescription() )+"\"" + newline +
        "  }" + newline +
        "}" + newline;


      return json;

    }

/*
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

    private String createCorrelationId() {
        UUID uuid = UUID.randomUUID();
        String generated = uuid.toString();
        try {
            generated = Hex.encodeHexString(MessageDigest.getInstance("SHA-1").digest(generated.getBytes()));
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        if (generated.length() > 80) {
            generated = generated.substring(0, 79);
        }
        return generated;
    }
    */
}
