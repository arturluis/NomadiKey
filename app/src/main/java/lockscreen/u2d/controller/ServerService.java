package lockscreen.u2d.controller;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerService extends Service {

    ServerSocket serverSocket;

    public ServerService() {

        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private class SocketServerThread extends Thread {

        static final int SocketServerPORT = 12345;
        int count = 0;

        @Override
        public void run() {
            try {
                serverSocket = new ServerSocket(SocketServerPORT);

                Log.d("[Server]", "I am running ... ");

                while (true) {
                    Socket socket = serverSocket.accept();
                    Log.d("[Server]", "Received a connection ... ");
                    count++;
                    int dataRead;
                    ByteArrayOutputStream byteArrayOutputStream =
                            new ByteArrayOutputStream(1024);
                    byte[] buffer = new byte[1024];
                    String data;
                    InputStream is = socket.getInputStream();

                    while((dataRead = is.read(buffer)) > 0){
                        byteArrayOutputStream.reset();
                        byteArrayOutputStream.write(buffer, 0, dataRead);
                        data = byteArrayOutputStream.toString("UTF-8");

                        if(!data.equals("rst")){

                            Log.w("[Server]", "Msg: " + data);

                            if(data.contains("call")){
                                Intent i = new Intent();
                                i.setAction("IC");
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            }
                        }else{
                            break;
                        }

                    }
                    Log.d("[Server]", "Connection terminated ... ");

                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

}


