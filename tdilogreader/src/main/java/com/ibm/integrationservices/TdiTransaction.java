package com.ibm.integrationservices;

import java.time.LocalDateTime;

public class TdiTransaction{

    public static final String XPATH_EXT_PROB_ID = "XPath EXT_PROB_ID:";
    public static final String XPATH_COMMAND = "XPath command:";

    private String netcoolEvent;
    private LocalDateTime requestTime;
    private String command;
    private String payload;

    public TdiTransaction(String command, LocalDateTime requestTime) {
        this.command = command;
        this.requestTime = requestTime;
    }

    public TdiTransaction event(String netcoolEvent){
        this.netcoolEvent = netcoolEvent;
        return this;
    }

    public TdiTransaction payload(String payload){
        this.payload = payload;
        return this;
    }

    @Override
    public String toString() {
        return "TdiTransaction{" +
                "netcoolEvent='" + netcoolEvent + '\'' +
                ", requestTime=" + requestTime +
                ", command='" + command + '\'' +
                ", payload='" + payload + '\'' +
                '}';
    }

    public String getNetcoolEvent() {
        return netcoolEvent;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public String getCommand() {
        return command;
    }

    public String getPayload() {
        return payload;
    }
}
