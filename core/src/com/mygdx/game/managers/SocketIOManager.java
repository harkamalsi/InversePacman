package com.mygdx.game.managers;

import com.mygdx.game.shared.Constants;

import java.net.URISyntaxException;


import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;



public class SocketIOManager {
    private static final String TAG = SocketIOManager.class.getSimpleName();

    private static SocketIOManager mInstance;
    private Socket mSocket=null;
    private String socketID=null;


    public Socket getSocketInstance(){
        if (mSocket==null){
            try {
                mSocket = IO.socket(Constants.HOST);
                initializeSocket();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return mSocket;
    }

    private void initializeSocket(){
        mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                printToTerminal(TAG, "EVENT connect");

                socketID = mSocket.connect().id();
                printToTerminal(TAG, "call: " + socketID);
            }
        }).on(Socket.EVENT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args != null) {
                    printToTerminal(TAG, "Event error: " + args[0].toString());
                }
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                printToTerminal("TAG", "Event disconnect, Socket is disconnected");
            }
        });

        //mSocket.connect();
    }

    public void start() {
        printToTerminal(TAG, "start socket...");
        if (mSocket.connected()) {
            return;
        }

        mSocket.off();

        mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                printToTerminal(TAG, "EVENT connect");

                socketID = mSocket.connect().id();
                printToTerminal(TAG, "call: " + socketID);
            }
        }).on(Socket.EVENT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args != null) {
                    printToTerminal(TAG, "Event error: " + args[0].toString());
                }
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                printToTerminal("TAG", "Event disconnect, Socket is disconnected");
            }
        });

        mSocket.connect();
    }


    public void stop() {
        printToTerminal(TAG, "stop socket...");

        if (mSocket != null) {
            mSocket.disconnect();
        }
    }

    private void printToTerminal(String tag, String msg) {
        System.out.println("SocketIO/" + tag + "" + msg);
    }
}