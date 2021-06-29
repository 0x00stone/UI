package controller.module;

import controller.View.Updata;
import controller.View.log;
import controller.connect;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * description: Server <br>
 * date: 2020/11/18 14:41 <br>
 * author: s1mple <br>
 * version: 1.0 <br>
 */
public class Server extends Thread{
    static ServerSocket server;
    public static Socket client;

    static{
        try {
            server=new ServerSocket(Updata.serverSocketPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void run(){

        try {
            System.out.println("服务正在运行，等待客户端连接！");
            log.write("点对点服务端开启");
            ExecutorService pool = Executors.newFixedThreadPool(Updata.poolSize);//线程池大小
            while (true) {
                client = server.accept();

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        Parent target = null;
                        try {
                            target = FXMLLoader.load(getClass().getResource("/server.fxml"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Stage secondWindow = new Stage();
                        Scene scene = new Scene(target);
                        secondWindow.setTitle(client.getInetAddress() + " : ");
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
                };

                Platform.runLater(runnable);
                //pool.submit(runnable);

                System.out.println("收到socket");

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
