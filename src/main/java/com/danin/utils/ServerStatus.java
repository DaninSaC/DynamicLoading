package com.danin.utils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;


public class ServerStatus{
    public int getStatus(String serverip, int serverport){
        try{
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(serverip, serverport), 5000);
            socket.close();

            return 1;
        }catch(SocketTimeoutException e){
            e.printStackTrace();
            return -1;
        }catch(SocketException e){
            return 0;
        }catch(IOException e){
            e.printStackTrace();
            return -2;
        }
    }
}
