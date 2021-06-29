package controller;

import javafx.application.Application;
import javafx.stage.Stage;


/**
 * description: main <br>
 * date: 2021/1/6 10:34 <br>
 * author: s1mple <br>
 * version: 1.0 <br>
 */
public class connect extends Application {
    private Stage primaryStage;
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        System.out.println("连接窗口开启");
    }

}
