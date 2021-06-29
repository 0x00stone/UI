package controller.View;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.util.Set;

/**
 * description: index <br>
 * date: 2021/1/2 0:26 <br>
 * author: s1mple <br>
 * version: 1.0 <br>
 */
public class index {
    public static void saveIndex(user[] User){
        try{
        FileOutputStream fos = new FileOutputStream(new File("./index.properties"));
        Properties index= new Properties();
        for(user u : User){
            index.setProperty(u.id+".name",u.NickName);
            index.setProperty(u.id+".ipv6",u.IPv6);
            index.setProperty(u.id+".key",u.key);
            index.setProperty(u.id+".status", String.valueOf(u.status));
        }
        index.store(fos,"index");
        }catch (Exception e){
            e.printStackTrace();
            log.write(e.getMessage());
        }
    }

    public static user[] getIndex() {
        try {
            if (new File("./index.properties").exists()) {
                FileInputStream fis = new FileInputStream(new File("./index.properties"));
                Properties index = new Properties();
                index.load(fis);
                Set<String> keys = index.stringPropertyNames();
                user[] users = new user[keys.size() / 4];

                for (String u : keys) {
                    String[] split = u.split("\\.");
                    boolean flags = false;
                    for (int i = 0; i < users.length; i++) {
                        if (users[i] != null) {
                            if (split[0].equals(String.valueOf(users[i].id))) {
                                flags = true;
                                break;
                            }
                        }
                    }
                    if (flags == false) {
                        for (int i = 0; i < users.length; i++) {
                            if (users[i] == null) {
                                users[i] = new user(Integer.valueOf(split[0]));
                                break;
                            }
                        }
                    }
                    for (int i = 0; i < users.length; i++) {
                        if (users[i] != null) {
                            if (split[0].equals(String.valueOf(users[i].id))) {
                                switch (split[1]) {
                                    case "name":
                                        users[i].NickName = index.getProperty(u);
                                        break;
                                    case "ipv6":
                                        users[i].IPv6 = index.getProperty(u);
                                        break;
                                    case "key":
                                        users[i].key = index.getProperty(u);
                                        break;
                                    case "status":
                                        users[i].status = Boolean.valueOf(index.getProperty(u));
                                        break;
                                }
                                break;
                            }
                        }
                    }
                }
                return users;
            } else {
                FileOutputStream fos = new FileOutputStream(new File("./index.properties"));

                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.write(e.getMessage());
            return null;
        }

    }
}
