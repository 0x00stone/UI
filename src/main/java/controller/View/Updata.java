package controller.View;


import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import util.Cypher.Rsa;

import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import static controller.View.index.getIndex;
import static controller.View.index.saveIndex;


public class Updata {
    public static String name ;
    public static int port;    //默认服务器端口
    public static int serverSocketPort;
    public static String host;   //默认服务器地址
    public static String rsaPublicKey;
    public static String rsaPrivateKey;
    public static int poolSize;
    public static String stringport;
    public static String stringserverSocketport;
/**
 * description: 服务端上线 
 * version: 1.0 
 * date: 2020/10/28 10:54 
 * author: Revers. 
 * 
 * @param 
 * @return void
 */
public static user[] UpdataNickname(Socket socket,TextArea textArea) throws IOException {
    //构建IO
    InputStream is = socket.getInputStream();
    OutputStream os = socket.getOutputStream();
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
    BufferedReader br = new BufferedReader(new InputStreamReader(is));

    //向服务器端发送一条消息
    String ipv6 = getLocalIPv6Address();
    bw.write(ipv6 + "," + name + "," + rsaPublicKey + "\n");
    bw.flush();

    //读取服务器返回的消息
    String mess = br.readLine();

    if (Overview.first == true) {
        System.out.println("服务器：" + mess);
        textArea.appendText("服务器：" + mess + "\n");
    }

    //接收用户表
    if (Overview.first == true) {
        System.out.println("服务器:已发送用户表至客户端");
        textArea.appendText("服务器:已发送用户表至客户端\n");
    }

    //读取客户端发送来的消息
    String length = br.readLine();
    bw.write(length + "\n");
    bw.flush();
    log.write("接收用户数");
    System.out.println("接收用户数");

    user[] User = new user[Integer.valueOf(length)];
    for (int i = 0; i < Integer.valueOf(length); i++) {
        String message = br.readLine();//用户表
        String[] split = message.split(",");
        User[i] = new user(Integer.valueOf(split[0]), split[1], split[2], split[3], Boolean.valueOf(split[4]));
    }
    Overview.U = User;
    log.write("接收用户表");
    System.out.println("接收用户表");
    bw.write("接收\n");
    bw.flush();

    saveIndex(User);
    bw.write("朕已阅\n");
    bw.flush();
    log.write("用户表接收完成");


    if(Overview.first){
        System.out.println("用户表接收完成");
        textArea.appendText("用户表接收完成\n");
    }
    Overview.first = false;

    return User;
}


    /**
     * description: 有用户名配置文件,读文件,否则输入用户名
     * version: 1.0
     * date: 2020/10/21 14:58
     * author: Revers.
     *
     * @param
     * @return void
     */



    public void getConfig(TextArea textOut,TextField textin,Button button){
    try{
        if(new File("./Config.properties").exists()){
            FileInputStream fis = new FileInputStream(new File("./Config.properties"));
            Properties Config = new Properties();
            Config.load(fis);

            //username
            name = Config.getProperty("userName");
            if(name != null || !"".equals(name)) {
                textOut.appendText("欢迎用户 " + name + " 回来\n\n");
            }else {
                addName(Config,textOut,textin,button);
                if(name.equals("")){
                    return;
                }
            }

            //key
            rsaPrivateKey = Config.getProperty("RsaPrivateKey");
            rsaPublicKey = Config.getProperty("RsaPublicKey");
            if(rsaPublicKey == null || rsaPrivateKey == null){
                Map<Integer, String> key = newKey(Config);
                rsaPublicKey = key.get(0);
                rsaPrivateKey = key.get(1);
            }

            host = Config.getProperty("host");
            if(host == null){
                host = newhost(Config);
            }

            stringport = Config.getProperty("port");
            if(stringport == null){
                port = Integer.parseInt(newport(Config));
            }else {
                port = Integer.parseInt(stringport);
            }

            stringserverSocketport = Config.getProperty("serverSocketPort");
            if(stringserverSocketport == null){
                serverSocketPort = Integer.parseInt(newserverSocketPort(Config));
            }else {
                serverSocketPort = Integer.parseInt(stringserverSocketport);
            }

            String stringpoolSize = Config.getProperty("poolSize");
            if(stringpoolSize == null){
                poolSize = Integer.parseInt(newpoolSize(Config));
            }else{
                poolSize = Integer.parseInt(stringpoolSize);
            }
            poolSize = Integer.valueOf(stringpoolSize);

        }else{
            FileOutputStream fos = new FileOutputStream(new File("./Config.properties"));
            Properties Config = new Properties();
            addName(Config,textOut,textin,button);

            Map<Integer, String> key = newKey(Config);
            rsaPublicKey = key.get(0);
            rsaPrivateKey = key.get(1);

            host = newhost(Config);

            port = Integer.parseInt(newport(Config));

            serverSocketPort = Integer.parseInt(newserverSocketPort(Config));

            poolSize = Integer.parseInt(newpoolSize(Config));
            Config.store(fos,"new");
        }
        Overview.U = getIndex();
    }catch (Exception e){
        e.printStackTrace();
        log.write(e.getMessage());
    }
}
    public String addName(Properties properties, TextArea textOut, TextField textField, Button button){
        name = "";
        new Thread() {
            public void run() {
                textOut.appendText("请输入用户名:");
                String text = "";
                while( "".equals(text)) {
                    text = Overview.getSend();
                }
                System.out.println("获得用户名");
                name = Overview.getSend();
                setProperties("userName",name, "new name",properties);
                textOut.appendText("欢迎用户 "+name +" 登录\n\n");
            };
        }.start();

        return name;
    }

    public Map<Integer, String> newKey(Properties properties) {
        Map<Integer, String> rsaKey = Rsa.getRsaKey(1024);
        rsaPublicKey = rsaKey.get(0);
        rsaPrivateKey = rsaKey.get(1);
        setProperties("RsaPublicKey", rsaPublicKey, "rsapublic",properties);
        setProperties("RsaPrivateKey", rsaPrivateKey, "rsaprivate",properties);

        return rsaKey;
    }

    public String newhost(Properties properties) {
        setProperties("host", "1.15.141.195","默认连接服务器地址",properties);
        return "1.15.141.195";
    }

    public String newport(Properties properties){
        setProperties("port", "10000","默认连接服务器端口",properties);
        return "10000";
    }

    public String newserverSocketPort(Properties properties){
        setProperties("serverSocketPort", "9000","客户端监听端口",properties);
        return "9000";
    }

    public String newpoolSize(Properties properties){
        setProperties("poolSize","5","默认线程池大小",properties);
        return "5";
    }

    public void setProperties(String name,String value,String remarks,Properties properties){
        try {
            properties.setProperty(name, value);

            FileOutputStream fos = new FileOutputStream(new File("./Config.properties"));
            properties.store(fos, remarks);//文件名和路径
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
            log.write(e.getMessage());
        }
    }

    public static String getLocalIPv6Address() throws SocketException {
        InetAddress inetAddress =null;

        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        outer:
        while(networkInterfaces.hasMoreElements()) {
            Enumeration<InetAddress> inetAds = networkInterfaces.nextElement().getInetAddresses();
            while(inetAds.hasMoreElements()) {
                inetAddress = inetAds.nextElement();
                //检查此地址是否是IPv6地址以及是否是保留地址
                if(inetAddress instanceof Inet6Address&& !isReservedAddr(inetAddress)) {
                    break outer;

                }
            }
        }
        String ipAddr = inetAddress.getHostAddress();
        //过滤网卡
        int index = ipAddr.indexOf('%');
        if(index>0) {
            ipAddr = ipAddr.substring(0, index);
        }

        return ipAddr;
    }
    private static boolean isReservedAddr(InetAddress inetAddr) {
        if(inetAddr.isAnyLocalAddress()||inetAddr.isLinkLocalAddress()||inetAddr.isLoopbackAddress())
        {
            return true;
        }
        return false;
    }

}
