package com.opuscapita.peppol.commons.model;

import com.opuscapita.peppol.commons.container.state.ProcessType;

public class PeppolEvent {

    private ProcessType processType;

    // File specific parameters
    private String fileName;
    private long fileSize;
    private String errorFilePath;

    // Document specific parameters
    private String invoiceId;
    private String senderId;
    private String senderName = "n/a";
    private String senderCountryCode;
    private String recipientId;
    private String recipientName;
    private String recipientCountryCode;
    private String invoiceDate;
    private String dueDate;
    private String errorMessage;
    private String transactionId;
    private String documentType;

    // Sending specific parameters
    private String commonName;
    private String sendingProtocol;
    private String originalSource;

    public ProcessType getProcessType() {
        return processType;
    }

    public void setProcessType(ProcessType processType) {
        this.processType = processType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getErrorFilePath() {
        return errorFilePath;
    }

    public void setErrorFilePath(String errorFilePath) {
        this.errorFilePath = errorFilePath;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        if (senderName != null) {
            this.senderName = senderName;
        }
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getSenderCountryCode() {
        return senderCountryCode;
    }

    public void setSenderCountryCode(String senderCountryCode) {
        this.senderCountryCode = senderCountryCode;
    }

    public String getRecipientCountryCode() {
        return recipientCountryCode;
    }

    public void setRecipientCountryCode(String recipientCountryCode) {
        this.recipientCountryCode = recipientCountryCode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getSendingProtocol() {
        return sendingProtocol;
    }

    public void setSendingProtocol(String sendingProtocol) {
        this.sendingProtocol = sendingProtocol;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getOriginalSource() {
        return originalSource;
    }

    public void setOriginalSource(String originalSource) {
        this.originalSource = originalSource;
    }

    @Override
    public String toString() {
        return "PeppolEvent{" +
                "processType=" + processType +
                ", fileName='" + fileName + '\'' +
                ", fileSize=" + fileSize +
                ", errorFilePath='" + errorFilePath + '\'' +
                ", invoiceId='" + invoiceId + '\'' +
                ", senderId='" + senderId + '\'' +
                ", senderName='" + senderName + '\'' +
                ", senderCountryCode='" + senderCountryCode + '\'' +
                ", recipientId='" + recipientId + '\'' +
                ", recipientName='" + recipientName + '\'' +
                ", recipientCountryCode='" + recipientCountryCode + '\'' +
                ", invoiceDate='" + invoiceDate + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", documentType='" + documentType + '\'' +
                ", commonName='" + commonName + '\'' +
                ", sendingProtocol='" + sendingProtocol + '\'' +
                ", originalSource='" + originalSource + '\'' +
                '}';
    }
}
