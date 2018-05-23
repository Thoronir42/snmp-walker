package cz.zcu.students.kiwi;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class MainLog {
    private static DateFormat SDF = new SimpleDateFormat("y.MM.dd HH-mm-SS");

    public static void configureLogging() {
        String logFile = "./log/" + SDF.format(System.currentTimeMillis());
        try {
            FileAppender appender = new FileAppender(new PatternLayout("%p\t%c{1}: %m%n"), logFile);

            BasicConfigurator.configure(appender);

        } catch (IOException e) {
            BasicConfigurator.configure();
            Logger.getRootLogger().warn("Failed to configure file logging");
        }
    }
}
