package controller.View;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * description: log <br>
 * date: 2020/12/23 10:35 <br>
 * author: s1mple <br>
 * version: 1.0 <br>
 */
public class log {
    static FileWriter flog;
    public static void Clientstart(){
        try{
            flog = new FileWriter("Clientlog.txt",true);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void write(String items){
        try {
            String format = new SimpleDateFormat("yy-MM-dd HH:mm:ss").format(new Date());
            flog.write(items + " : " + format + "\n");
            flog.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void close(){
        try{
            flog.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
