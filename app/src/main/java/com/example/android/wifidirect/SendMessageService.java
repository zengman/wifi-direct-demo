package com.example.android.wifidirect;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class SendMessageService extends IntentService {
    private static final int SOCKET_TIMEOUT = 5000;
    public static final String ACTION_SEND_MESSAGE = "com.example.android.wifidirect.SEND_MESSAGE";
    public static final String EXTRAS_FILE_PATH = "file_url";
    public static final String EXTRAS_ADDRESS = "go_host";
    public static final String EXTRAS_PORT = "go_port";


    public SendMessageService() {
        super("SendMessageService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            Context context = getApplicationContext();
            if(action.equals(ACTION_SEND_MESSAGE)){
                String host = intent.getExtras().getString(EXTRAS_ADDRESS);
                int port = intent.getExtras().getInt(EXTRAS_PORT);
                Socket socket = new Socket();

                try{
                    Log.d(WiFiDirectActivity.TAG, "Opening client socket - ");
                    socket.bind(null);
                    socket.connect((new InetSocketAddress(host,port)), SOCKET_TIMEOUT);

                    Log.d(WiFiDirectActivity.TAG, "Client socket - "+socket.isConnected());
                    OutputStream stream = socket.getOutputStream();
                    ContentResolver cr = context.getContentResolver();
                    InputStream is = null;
                    try{
                        DataOutputStream writer = new DataOutputStream(stream);
                        writer.writeDouble(109.2222);

                        Log.d(WiFiDirectActivity.TAG, "send message - ");


                    }catch (IOException e){
                        Log.e(WiFiDirectActivity.TAG, e.getMessage());
                    }
                    DeviceDetailFragment.copymessage(is,stream);
                    Log.d(WiFiDirectActivity.TAG, "Client: Data written");
                }catch (IOException e){
                    Log.e(WiFiDirectActivity.TAG, e.getMessage());
                }finally {
                    if(socket != null){
                        if(socket.isConnected()){
                            try {
                                socket.close();
                            }catch (IOException e){
                                //give up
                                e.printStackTrace();
                            }
                        }
                    }
                }

            }

        }
    }


}
