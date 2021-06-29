package controller.View;

import controller.module.Server;
import controller.connect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * description: Overview <br>
 * date: 2021/1/5 11:05 <br>
 * author: s1mple <br>
 * version: 1.0 <br>
 */
public class Overview implements Initializable {
    @FXML
    private Button Button1;

    @FXML
    private Button Button2;

    @FXML
    private Button Button3;

    @FXML
    public TextArea textOut;

    @FXML
    private TextField textIn;

    @FXML
    private Button Button4;


    public static user[] U;
    static public boolean first;
    public static Socket socket = null;
    private static boolean flag = false;
    private static String send = "";
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(flag == false){
            init();
        }
        //Welcome
        //Welcome.graph(textOut);
    }

    private void init(){
        Updata updata = new Updata();
        updata.getConfig(textOut,textIn,Button4);

        log.Clientstart();
        log.write("启动");
        System.out.println("日志启动");

        new Server().start();
        flag = true;
    }


    public void send(ActionEvent event){
        String text = textIn.getText();
        this.send = text;
        textOut.appendText(text + "\n");
        textIn.clear();
    }

    public static String getSend(){
        //if(!"".equals(send))
            System.out.println(send);
        return  send;
    }

    //选项三
    public void showText(ActionEvent event){
        textOut.appendText("用户表\n");
        textOut.appendText("用户名-----" + "ipv6-----" + "状态-----" + "公钥\n");

        for (int i = 0; i < U.length; i++) {
            textOut.appendText(U[i].NickName + "-----" + U[i].IPv6 + "-----" + U[i].status + "-----" + U[i].key +"\n");
        }
        textOut.appendText("\n");
    }

    //选项一
    public void heart(ActionEvent event) throws InterruptedException {
        Overview.first = true;
        heartBeat heartBeat = new heartBeat();
        heartBeat.settextArea(textOut);
        heartBeat.start();
    }

    public void p2p(ActionEvent event) throws IOException {
        String text = textIn.getText();
        if("".equals(text)){
            textOut.appendText("请输入通信ip以及端口或当前用户表用户名(IP和端口之间用空格分开):\n");
            return;
        }
        String[] s = text.split(" ");
        textIn.clear();
        if (s.length == 1) {
            textOut.appendText("连接用户" + text + "\n");
            //用户名,连接数据库,未写
            for (user users : Overview.U) {
                if (s[0].equals(users.NickName)) {
                    socket = new Socket(users.IPv6, 9000);

                    Parent target = FXMLLoader.load(getClass().getResource("/client.fxml"));
                    Stage secondWindow=new Stage();
                    Scene scene=new Scene(target);
                    secondWindow.setTitle(users.NickName + " : "+users.IPv6);
                    secondWindow.setScene(scene);
                    secondWindow.show();

                    new Runnable() {
                        @Override
                        public void run() {
                            connect connect = new connect();
                            connect.start(secondWindow);
                        }
                    };
                    System.out.println("程序正常启动");
                    //socket = new Socket(users.IP, 9000);   //2的要改
                    //new Client().client(socket);


                }
            }
        } else if (s.length == 2) {
            textOut.appendText("连接地址" + text + "\n");
            socket = new Socket(s[0], Integer.parseInt(s[1]));
            Parent target = FXMLLoader.load(getClass().getResource("/client.fxml"));
            Stage secondWindow=new Stage();
            Scene scene=new Scene(target);
            secondWindow.setTitle(s[0] + " : "+ s[1]);
            secondWindow.setScene(scene);
            secondWindow.show();


            new Runnable() {
                @Override
                public void run() {
                    connect connect = new connect();
                    connect.start(secondWindow);

                }
            };

        }
    }
}
