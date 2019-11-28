package com.ibm.integrationservices;

import java.time.LocalDateTime;

public class TdiTransaction {

    private String netcoolEvent;
    private LocalDateTime requestTime;
    private String command;
    private TdiEvent netcoolRequest;
    private TdiEvent soapRequest;
    private TdiEvent soapResponse;
    private TdiEvent netcoolResponse;
    private TdiEvent getKeysRequest;
    private TdiEvent getKeysResponse;
    private int lineNumber;

    public TdiTransaction(int lineNumber, String command){
        this.lineNumber = lineNumber;
        this.command = command;
    }

    public String getNetcoolEvent() {
        return netcoolEvent;
    }

    public void setNetcoolEvent(String netcoolEvent) {
        this.netcoolEvent = netcoolEvent;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public TdiEvent getNetcoolRequest() {
        return netcoolRequest;
    }

    public void setNetcoolRequest(TdiEvent netcoolRequest) {
        this.netcoolRequest = netcoolRequest;
        this.netcoolRequest.setTransaction(this);
    }

    public TdiEvent getSoapRequest() {
        return soapRequest;
    }

    public void setSoapRequest(TdiEvent soapRequest) {
        this.soapRequest = soapRequest;
        this.soapRequest.setTransaction(this);
    }

    public TdiEvent getSoapResponse() {
        return soapResponse;
    }

    public void setSoapResponse(TdiEvent soapResponse) {
        this.soapResponse = soapResponse;
        this.soapResponse.setTransaction(this);
    }

    public TdiEvent getNetcoolResponse() {
        return netcoolResponse;
    }

    public void setNetcoolResponse(TdiEvent netcoolResponse) {
        this.netcoolResponse = netcoolResponse;
        this.netcoolResponse.setTransaction(this);
    }

    public TdiEvent getGetKeysRequest() {
        return getKeysRequest;
    }

    public void setGetKeysRequest(TdiEvent getKeysRequest) {
        this.getKeysRequest = getKeysRequest;
        if(this.getKeysRequest != null) {
            this.getKeysRequest.setTransaction(this);
        }
    }

    public TdiEvent getGetKeysResponse() {
        return getKeysResponse;
    }

    public void setGetKeysResponse(TdiEvent getKeysResponse) {
        this.getKeysResponse = getKeysResponse;
        if(this.getKeysResponse != null) {
            this.getKeysResponse.setTransaction(this);
        }
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }
}