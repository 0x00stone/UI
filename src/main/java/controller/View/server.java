package controller.View;

import controller.module.Server;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import util.Cypher.Aes;
import util.Cypher.Rsa;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * description: client <br>
 * date: 2021/1/6 22:54 <br>
 * author: s1mple <br>
 * version: 1.0 <br>
 */
public class server implements Initializable {
    @FXML
    private TextArea textPrint;

    @FXML
    private TextArea textScan;

    @FXML
    private Button post;

    public String rsaPublicKey;
    public String aesKey;
    public Socket socket = null;
    public Scanner get;
    public PrintWriter out;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println(this.socket = Server.client);
      /*  if(this.socket == null){
            Platform.exit();
        }*/
        client();


    }


    public void send(ActionEvent event) {
        String text = textScan.getText();
        textScan.clear();

        textPrint.appendText("\t>" + text + "\n");

        out.println(text);
        out.flush();
        if ("/quit".equals(text)) {
            System.out.println("连接已中断");
            Platform.exit();
        }

    }

    public void client(){
        try {


            InetAddress inetAddress = this.socket.getInetAddress();

            System.out.println();
            System.out.println(inetAddress + " 已成功连接到此台服务器上。");
            log.write(inetAddress + " 已成功连接到此台服务器上。");


            out = new PrintWriter(socket.getOutputStream());
            get = new Scanner(socket.getInputStream());

            boolean flag = false;
            while(flag == false) {
                flag = auto();
            }


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

    public boolean auto() throws BadPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidKeyException {
        rsaPublicKey = get.nextLine();//1
        System.out.println(rsaPublicKey);
        System.out.println(Updata.rsaPublicKey);

        out.println(Rsa.publicEncrypt(rsaPublicKey,Updata.rsaPublicKey));
        out.flush();//2
        System.out.println(Rsa.publicEncrypt(rsaPublicKey,Updata.rsaPublicKey));

        aesKey = Rsa.privateDecrypt(Updata.rsaPrivateKey,get.nextLine());//3
        System.out.println(aesKey);

        out.println(Aes.encrypt(aesKey,"ack1"));
        out.flush();//4
        System.out.println(Aes.encrypt(aesKey,"ack1"));

        String ack = Aes.decrypt(aesKey,get.nextLine());
        System.out.println(ack);
        if("ack2".equals(ack)){
            log.write("密钥传输成功");
            textPrint.appendText("聊天加密成功\n");
            return true;
        }else {
            log.write("密钥传输失败");
            textPrint.appendText("聊天加密失败\n");
            return false;//5
        }
    }
}