package com.ibm.integrationservices;

import java.time.LocalDateTime;

public class TdiTransaction {

    public static final String XPATH_EXT_PROB_ID = "XPath EXT_PROB_ID:";
    public static final String XPATH_COMMAND = "XPath command:";

    private String netcoolEvent;
    private LocalDateTime requestTime;
    private String command;
    private String netcoolRequest;
    private String soapRequest;
    private String soapResponse;
    private String netcoolResponse;

    public TdiTransaction(String command){
        this.command = command;
    }


    @Override
    public String toString() {
        return "TdiTransaction{" +
                "netcoolEvent='" + netcoolEvent + '\'' +
                ", requestTime=" + requestTime +
                ", command='" + command + '\'' +
                ", netcoolRequest='" + netcoolRequest + '\'' +
                ", soapRequest='" + soapRequest + '\'' +
                ", soapResponse='" + soapResponse + '\'' +
                ", netcoolResponse='" + netcoolResponse + '\'' +
                '}';
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

    public String getNetcoolRequest() {
        return netcoolRequest;
    }

    public void setNetcoolRequest(String netcoolRequest) {
        this.netcoolRequest = netcoolRequest;
    }

    public String getSoapRequest() {
        return soapRequest;
    }

    public void setSoapRequest(String soapRequest) {
        this.soapRequest = soapRequest;
    }

    public String getSoapResponse() {
        return soapResponse;
    }

    public void setSoapResponse(String soapResponse) {
        this.soapResponse = soapResponse;
    }

    public String getNetcoolResponse() {
        return netcoolResponse;
    }

    public void setNetcoolResponse(String netcoolResponse) {
        this.netcoolResponse = netcoolResponse;
    }
}