package se.basile.compax;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class MsgReceiver implements Runnable {
    private String server_ip;
    private int server_port;
    // message to send to the server
    private String mServerMessage;
    private OnMessageReceived mMessageListener = null;

    // while this is true, the server will continue running
    private boolean mRun = false;
    // used to send messages
    private PrintWriter mBufferOut;
    // used to read messages from the server
    private BufferedReader mBufferIn;

    public MsgReceiver(String server_ip, String server_port, MsgReceiver.OnMessageReceived listener) {
        this.server_ip = server_ip;
        this.server_port = Integer.parseInt(server_port);

        mMessageListener = listener;
    }

    @Override
    public void run() {
        mRun = true;
        Log.d("COMPAX", "Thread1.run()");

        try {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(server_ip);

            //create a socket to make the connection with the server
            Socket socket = new Socket(serverAddr, server_port);

            try {

                //receives the message which the server sends back
                mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //in this while the client listens for the messages sent by the server
                while (mRun) {
                    mServerMessage = mBufferIn.readLine();
                    if (mServerMessage != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(mServerMessage);
                    }
                }
                Log.e("COMPAX", "S: Received Message: '" + mServerMessage + "'");

            } catch (Exception e) {

                Log.e("COMPAX", "S: Error", e);

            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
            }

        } catch (Exception e) {

            Log.e("COMPAX", "C: Error", e);

        }

    }

    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
}
