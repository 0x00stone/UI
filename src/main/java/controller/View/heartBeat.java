package controller.View;

import javafx.scene.control.TextArea;

import java.net.Socket;

/**
 * description: heartBeat <br>
 * date: 2020/12/4 16:19 <br>
 * author: s1mple <br>
 * version: 1.0 <br>
 */
public class heartBeat extends Thread {
    private TextArea textout;
    private Socket socket;
    public void settextArea(TextArea textout){
        this.textout = textout;
    }

    @Override
    public void run() {
        synchronized (this) {
                try {
                    //服务器上线
                    socket = new Socket(Updata.host, Updata.port);
                    while (true) {
                        //获取 用户表
                        Updata.UpdataNickname(socket,textout);
                        log.write("heartBeat");
                        this.wait(60000);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.write(e.getMessage());
                }
            }
        }
    }

