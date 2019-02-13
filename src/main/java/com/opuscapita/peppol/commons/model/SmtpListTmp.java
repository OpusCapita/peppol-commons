package com.opuscapita.peppol.commons.model;

import javax.persistence.*;

@Entity
@Table(name = "tmp_smtp_list")
public class SmtpListTmp {

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    @Column(name = "sender_id")
    private String senderId;

    @Column(name = "emails")
    private String emails;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }
}
