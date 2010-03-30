/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *  
 *   http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *  
 *******************************************************************************/

package org.apache.wink.logging;

import java.util.ArrayList;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class WinkLogHandler extends Handler {

    static private ArrayList<LogRecord> logRecords = new ArrayList<LogRecord>();
    static boolean storeLogsOn = false;
    
    @Override
    public void close() throws SecurityException {
        // TODO Auto-generated method stub
    }

    @Override
    public void flush() {
        // TODO Auto-generated method stub
    }

    @Override
    public void publish(LogRecord record) {
        if (storeLogsOn) {
            logRecords.add(record);
        }
    }
    
    /**
     * turns logging capture on
     */
    public static void turnLoggingCaptureOn() {
        storeLogsOn = true;
    }
    
    /**
     * turns logging capture off
     */
    public static void turnLoggingCaptureOff() {
        storeLogsOn = false;
    }
    
    /**
     * get all captured LogRecords.  It is recommended that you inspect the returned list,
     * perform the desired asserts, then call clearRecords to clean up the list prior to the
     * next test that may wish to also capture logging.
     * 
     * @return ArrayList of LogRecords captured
     */
    public static ArrayList<LogRecord> getRecords() {
        return logRecords;
    }
    
    /**
     * Get only the records associated with the logger for a particular name.  Typically, this will
     * be the class name of the production class under test.
     * 
     * @param logName
     * @return filtered ArrayList of LogRecords captured
     */
    public static ArrayList<LogRecord> getRecordsFilteredBy(String logName) {
        ArrayList<LogRecord> filtered = new ArrayList<LogRecord>();
        for (int i = 0; i < logRecords.size(); i++) {
            if (logRecords.get(i).getLoggerName().equals(logName)) {
                filtered.add(logRecords.get(i));
            }
        }
        return filtered;
    }
    
    /**
     * Clear all logRecords from the captured list.  It is recommended that test methods do this
     * to clean up prior to the next test that may wish to also capture logging.
     */
    public static void clearRecords() {
        logRecords.clear();
    }

}