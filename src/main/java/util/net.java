package util;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * description: net <br>
 * date: 2021/6/18 22:38 <br>
 * author: s1mple <br>
 * version: 1.0 <br>
 */
public class net {
    public static boolean isReservedAddr(InetAddress inetAddr) {
        if(inetAddr.isAnyLocalAddress()||inetAddr.isLinkLocalAddress()||inetAddr.isLoopbackAddress())
        {
            return true;
        }
        return false;
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
                if(inetAddress instanceof Inet6Address && !net.isReservedAddr(inetAddress)) {
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
}
