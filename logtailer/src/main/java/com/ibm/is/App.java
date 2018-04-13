package com.ibm.is;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        Executor runner = Executors.newFixedThreadPool(1);
        runner.execute(new FileMonitorRunner(args[0]));

    }
}
