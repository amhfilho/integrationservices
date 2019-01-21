package com.ibm.integrationservices;

import java.time.LocalDateTime;

public class TdiTransaction{

    public static final String XPATH_EXT_PROB_ID = "XPath EXT_PROB_ID:";
    public static final String XPATH_COMMAND = "XPath command:";

    private String netcoolEvent;
    private LocalDateTime requestTime;
    private String command;

    @Override
    public String toString() {
        return "TdiTransaction{" +
                "netcoolEvent='" + netcoolEvent + '\'' +
                ", requestTime=" + requestTime +
                ", command='" + command + '\'' +
                '}';
    }

    public TdiTransaction(String command, String netcoolEvent, LocalDateTime requestTime) {
        this.command = command;
        this.netcoolEvent = netcoolEvent;
        this.requestTime = requestTime;
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
}
