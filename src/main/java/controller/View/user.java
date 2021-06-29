package controller.View;

/**
 * description: user <br>
 * date: 2020/12/15 14:50 <br>
 * author: s1mple <br>
 * version: 1.0 <br>
 */
public class user {
    public int id;
    public String NickName;
    public String IPv6;
    public String key;
    public boolean status;

    public user(int id,String nickName, String IPv6, String key, boolean status) {
        this.id = id;
        NickName = nickName;
        this.IPv6 = IPv6;
        this.key = key;
        this.status = status;
    }

    public user(int id) {
        this.id = id;
    }
}
