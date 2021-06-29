package controller.View;

import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.InetAddress;
import java.util.Scanner;

/**
 * description: SocketReader <br>
 * date: 2020/11/18 10:58 <br>
 * author: s1mple <br>
 * version: 1.0 <br>
 */
public class SocketReader extends Thread{
    private Scanner get;
    private InetAddress address;
    private TextArea textScan;
    private TextArea textPrint;
    public SocketReader(Scanner get){
        this.get=get;
    }

    public void setAddress(InetAddress address){
        this.address = address;
    }

    public void setIO(TextArea textScan, TextArea textPrint){
        this.textScan = textScan;
        this.textPrint = textPrint;
    }

    public void  run() {
        try {
            while (true) {
                String next = get.nextLine();

                textPrint.appendText(address + ":" + next+ "\n");

                if ("/quit".equals(next)) {
                    System.out.println("退出");
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("连接已中断");
            return;
        }

    }
}
