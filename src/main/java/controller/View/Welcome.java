package controller.View;

import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

/**
 * description: 欢迎界面 <br>
 * date: 2020/10/28 13:56 <br>
 * author: s1mple <br>
 * version: 1.0 <br>
 */
public class Welcome {
    public static void graph(TextArea textOut){
        Random random = new Random();
        int num = random.nextInt(9);
        File file = new File("./src/main/java/logos/" + num +".txt");
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                textOut.appendText(tempStr + "\n");
            }
            textOut.appendText("\n");
            reader.close();
        } catch (IOException  e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
