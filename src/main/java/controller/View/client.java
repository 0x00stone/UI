package controller.View;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import util.Cypher.Aes;
import util.Cypher.Rsa;

import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * description: client <br>
 * date: 2021/1/6 22:54 <br>
 * author: s1mple <br>
 * version: 1.0 <br>
 */
public class client implements Initializable {
    @FXML
    private TextArea textPrint;

    @FXML
    private TextArea textScan;

    @FXML
    private Button post;

    public String rsaPublicKey;
    public String aesKey;
    public Scanner get;
    public PrintWriter out;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(Overview.socket);
        if(Overview.socket == null){
            Platform.exit();
        }
        client(Overview.socket);


    }


    public void send(ActionEvent event) {
        String text = textScan.getText();
        textScan.clear();

        textPrint.appendText("\t>" + text + "\n");

        out.println(text);
        out.flush();
        if ("/quit".equals(text)) {
            System.out.println("连接已中断");

        }

    }

    public void client(Socket client){
        try {
            get = new Scanner(client.getInputStream());
            out = new PrintWriter(client.getOutputStream());



            boolean flag = false;
            while (flag == false) {
                flag = auto();
            }
            out.println("已成功连接到远程服务器！");
            out.flush();//将缓冲流中的数据全部输出

            InetAddress inetAddress = client.getInetAddress();


            SocketReader socketReader = new SocketReader(get);
            socketReader.setAddress(inetAddress);
            socketReader.setIO(textScan,textPrint);

            Thread thread2 = new Thread(socketReader);
            thread2.start();
        }catch (Exception e){
            e.printStackTrace();
            log.write(e.getMessage());
        }
    }

    public boolean auto(){
        try {
            out.println(Updata.rsaPublicKey);
            out.flush();//1
            log.write(Updata.rsaPublicKey);
            //rsaPublicKey = get.nextLine();//1
            //print(Updata.rsaPublicKey);

            //out.println(Rsa.encrypt(rsaPublicKey,Updata.rsaPublicKey));
            //out.flush();//2
            rsaPublicKey = Rsa.privateDecrypt(Updata.rsaPrivateKey, get.nextLine());
            log.write(rsaPublicKey);
            //print(rsaPublicKey);

            aesKey = Aes.getAseKey(256);
            out.println(Rsa.publicEncrypt(rsaPublicKey, aesKey));
            out.flush();
            //aesKey = Rsa.privateDecrypt(Updata.rsaPrivateKey,get.nextLine());//3
            //        System.out.println(aesKey);
            log.write(aesKey);
            //print(aesKey);

            //out.println(Aes.encrypt(aesKey,"ack1"));
            //out.flush();//4
            String decrypt = Aes.decrypt(aesKey, get.nextLine());
            log.write(decrypt);
            //print(decrypt);

            if ("ack1".equals(decrypt)) {
                out.println(Aes.encrypt(aesKey, "ack2"));
                out.flush();
                log.write("密钥传输成功");
                textPrint.appendText("\t聊天加密成功\n");
                return true;
            } else {
                out.println(Aes.encrypt(aesKey, "fail"));
                out.flush();
                log.write("密钥传输成功");
                textPrint.appendText("\t聊天加密失败\n");
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            log.write(e.getMessage());
            return false;
        }
    }

}