package util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * description: Log <br>
 * date: 2021/6/28 20:51 <br>
 * author: s1mple <br>
 * version: 1.0 <br>
 */
public class Log {
    private static Calendar now = Calendar.getInstance();

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private static final int year = now.get(Calendar.YEAR);

    private static final int month = now.get(Calendar.MONTH) + 1;

    private static final String LOG_FOLDER_NAME = "AIOClient";

    private static final String LOG_FILE_SUFFIX = ".log";

    public static Logger logger = Logger.getLogger("MyLogger");

    //使用唯一的fileHandler，保证当天的所有日志写在同一个文件里
    private static FileHandler fileHandler = getFileHandler();

    private static LogFormatter LogFormatter = new LogFormatter();

    public static void localCloseToUser(){
        fileHandler = getFileHandler();
    }

    private synchronized static String getLogFilePath() {
        StringBuffer logFilePath = new StringBuffer();
//        logFilePath.append(System.getProperty("user.home"));
        logFilePath.append("./Logs");
        logFilePath.append(File.separatorChar);
        logFilePath.append(LOG_FOLDER_NAME);
        logFilePath.append(File.separatorChar);
        logFilePath.append(year);
        logFilePath.append(File.separatorChar);
        logFilePath.append(month);

        File dir = new File(logFilePath.toString());
        if (!dir.exists()) {
            dir.mkdirs();
        }

        logFilePath.append(File.separatorChar);
        logFilePath.append(sdf.format(new Date()));
        logFilePath.append(LOG_FILE_SUFFIX);

//        System.out.println(logFilePath.toString());
        return logFilePath.toString();
    }



    private static FileHandler getFileHandler() {
        FileHandler fileHandler = null;
        boolean APPEND_MODE = true;
        try {
            //文件日志内容标记为可追加
            fileHandler = new FileHandler(getLogFilePath(), APPEND_MODE);
            return fileHandler;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public synchronized static Logger setLoggerHanlder() {
        return setLoggerHanlder(Level.ALL);
    }

    //    SEVERE > WARNING > INFO > CONFIG > FINE > FINER > FINESET
    public synchronized static Logger setLoggerHanlder(Level level) {

        try {
            //以文本的形式输出
//            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setFormatter(LogFormatter);

            logger.addHandler(fileHandler);
            logger.setLevel(level);
        } catch (SecurityException e) {
            logger.severe(populateExceptionStackTrace(e));
        }
        return logger;
    }

    private synchronized static String populateExceptionStackTrace(Exception e) {
        StringBuilder sb = new StringBuilder();
        sb.append(e.toString()).append("\n");
        for (StackTraceElement elem : e.getStackTrace()) {
            sb.append("\tat ").append(elem).append("\n");
        }
        return sb.toString();
    }

    public static void finest(String message){
        logger.finest(message);
    }

    public static void finer(String message){
        logger.finer(message);
    }

    public static void fine(String message){
        logger.fine(message);
    }

    public static void config(String message){
        logger.config(message);
    }

    public static void info(String message){
        logger.info(message);
    }

    public static void warning(String message){
        logger.warning(message);
    }

    public static void severe(String message){
        logger.severe(message);
    }
}
