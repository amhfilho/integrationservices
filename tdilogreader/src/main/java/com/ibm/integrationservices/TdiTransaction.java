package com.ibm.integrationservices;

import java.time.Duration;
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

    public Duration getSoapResponseTime(){
        if(soapRequest == null || soapResponse == null) return null;
        if(soapRequest.getDateTime() == null || soapResponse.getDateTime() == null) return null;
        return Duration.between(soapRequest.getDateTime(), soapResponse.getDateTime());
    }

    public boolean isUpdateOperation(){
        return command.equals("UPDATE_PROBLEM") || command.equals("UPDATE_CALLBACK");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TdiTransaction{\n");
        sb.append("\tcommand=" + command + "\n");
        sb.append("\tnetcoolEvent=" + netcoolEvent + "\n");
        sb.append("\trequestTime=" + requestTime + "\n");
        sb.append("\tnetcoolRequest=\n" + netcoolRequest + "\n");
        if(getKeysRequest != null){
            sb.append("\tgetKeysRequest=\n" + getKeysRequest + "\n");
        }
        if(getKeysResponse != null){
            sb.append("\tgetKeysResponse=\n" + getKeysResponse + "\n");
        }
        sb.append("\tsoapRequest=\n" + soapRequest + "\n");
        sb.append("\tsoapResponse=\n" + soapResponse + "\n");
        sb.append("\tnetcoolResponse=\n" + netcoolResponse + "\n");
        if(getSoapResponseTime()!=null){
            sb.append("\tSOAP response time=" + getSoapResponseTime().toString() + "\n");
        }
        sb.append("\tlineNumber=" + lineNumber + "\n");
        sb.append('}');
        return sb.toString();
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