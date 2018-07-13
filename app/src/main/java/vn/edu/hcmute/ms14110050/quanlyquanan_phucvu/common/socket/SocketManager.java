package vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.socket;

import android.util.Log;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static vn.edu.hcmute.ms14110050.quanlyquanan_phucvu.common.constant.SOCKET.BASE_URL;

public class SocketManager {
    private static SocketManager INSTANCE;
    private Socket socket;
    private ArrayList<OnChangeSocketStateListener> networkStateListeners = new ArrayList<>();

    private SocketManager() {
        createSocket();

        socket.on("connect", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
//                Log.d("LOG", "SocketManger:connect event");
                for (OnChangeSocketStateListener listener : networkStateListeners) {
                    listener.onSocketConnect();
                }
            }
        });
        socket.on("disconnect", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                for (OnChangeSocketStateListener listener : networkStateListeners) {
                    listener.onSocketDisconnect();
                }
            }
        });
    }

    public static SocketManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SocketManager();
        }
        return INSTANCE;
    }

    private void createSocket() {
        try {
            socket = IO.socket(BASE_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

//    private ArrayList<String> events = new ArrayList<>();
    private HashMap<String, ArrayList<Emitter.Listener>> eventListeners = new HashMap<>();

    public void onSocket(final String event, Emitter.Listener listener) {
        ArrayList<Emitter.Listener> listeners = null;

        if (!eventListeners.containsKey(event)) {
//            Log.d("LOG", getClass().getSimpleName() + ":onSocket:create socket listen for event:" + event);

            listeners = new ArrayList<>();
        }
        else{
//            Log.d("LOG", getClass().getSimpleName() + ":onSocket:add listener to socket event:" + event);

            listeners = eventListeners.get(event);
        }

        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }

        eventListeners.put(event, listeners);

        if (listeners.size() == 1) {
            createSocketForEvent(event);
        }
    }

    private void createSocketForEvent(final String event) {
        if (socket != null) {
            socket.on(event, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    processOnEvent(event, args);
                }
            });
        }
    }

    private void processOnEvent(String event, Object... args) {
        if (eventListeners.containsKey(event)) {
            ArrayList<Emitter.Listener> listeners = eventListeners.get(event);
            for (Emitter.Listener listener : listeners) {
                listener.call(args);
            }
        }
    }

    public void offSocket(String event, Emitter.Listener listener) {
        if (socket != null && eventListeners != null && eventListeners.containsKey(event)) {
            ArrayList<Emitter.Listener> listeners = eventListeners.get(event);
            listeners.remove(listener);
            if (listeners.size() == 0) {
//                Log.d("LOG", SocketManager.class.getSimpleName() + ":offSocket:event:" + event);
                socket.off(event);
                eventListeners.remove(event);
            }
        }
    }

    // Các method không dùng đến
    /*public void disconnectSocket() {
        if (socket != null) {
            if (events != null && events.size() > 0) {
                for (String event : events) {
                    socket.off(event);
                }
                events.clear();
            }
            socket.disconnect();
        }
    }
*/
    public void addSocketStateListener(OnChangeSocketStateListener listener) {
//        Log.d("LOG", "Add OnChangeSocketStateListener");
        if (!networkStateListeners.contains(listener)) {
            networkStateListeners.add(listener);
        }
    }

    public void removeSocketStateListener(OnChangeSocketStateListener listener) {
        networkStateListeners.remove(listener);
    }

    public boolean connected() {
        return socket != null && socket.connected();
    }

    public void connect() {
        if (socket != null) {
            socket.connect();
        }
    }

    public boolean isInit() {
        return socket != null;
    }

    public void emitSocket(String socketEvent, JSONObject emitJson) {
        if (socket != null) {
//            Log.d("LOG", getClass().getSimpleName() + ":emitSocket:event:" + socketEvent);
            socket.emit(socketEvent, emitJson);
        }
    }
}
