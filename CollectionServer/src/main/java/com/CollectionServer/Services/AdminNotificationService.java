package com.CollectionServer.Services;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AdminNotificationService
{
    public static final int MAX_ENTRIES = 5000;
    public enum LogLevel
    {
        ERROR,
        WARN,
        INFO
    }

    private static ArrayList<AdminNotificationLog> logsArray = new ArrayList<AdminNotificationLog>();

    public void submitLog(LogLevel logLevel, String loggerName, String message)
    {
        AdminNotificationLog log = new AdminNotificationLog(logLevel, loggerName, message);
        //Check that maximum size is not being exceeded
        if(logsArray.size() >= MAX_ENTRIES)
        {
            //Remove the oldest element
            logsArray.remove(0);
        }

        logsArray.add(log);
        System.out.println(log.toString());
    }

    public ArrayList<AdminNotificationLog> getLogPage(int pageSize, int pageNumber)
    {
        ArrayList<AdminNotificationLog> output = new ArrayList<AdminNotificationLog>();
        int startingIndex = pageNumber * pageSize;
        for(int i = startingIndex; i < startingIndex + pageSize && i < logsArray.size(); i++)
        {
            output.add(logsArray.get(i));
        }

        return output;
    }

    public class AdminNotificationLog
    {
        public LogLevel logLevel;
        public String message;

        public String loggerName;

        public Date dateCreated;

        public AdminNotificationLog(LogLevel logLevel, String loggerName, String message)
        {
            this.logLevel = logLevel;
            this.loggerName = loggerName;
            this.message = message;

            this.dateCreated = new Date();
        }

        @Override
        public String toString()
        {
            String output = "";

            output += dateCreated.toString() + " | ";
            output += logLevel.toString() + " | ";
            output += loggerName + " >>>> ";
            output += message;

            return output;
        }
    }
}
