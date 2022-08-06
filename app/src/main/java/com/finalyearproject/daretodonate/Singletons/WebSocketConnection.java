package com.finalyearproject.daretodonate.Singletons;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.finalyearproject.daretodonate.Interfaces.SocketResponce;
import com.finalyearproject.daretodonate.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class WebSocketConnection {

    public static WebSocketConnection webSocketConnection = null;  

    private static Socket socket;
    private static URI uri;
    private static Map<String, String> auth = new HashMap<>();
    private static IO.Options options;
    private static User user;

    public static WebSocketConnection getInstance(SharedPreferences sharedPreferences, SocketResponce socketResponce) {

        user = User.getInstance();
        if (webSocketConnection == null) {
            webSocketConnection = new WebSocketConnection();
            auth.put("x-user-token", sharedPreferences.getString("x-user-token", ""));
            options = IO.Options.builder().setAuth(auth).build();
            try {
                uri = new URI("http://34.100.167.58/ws/user/update");
//                uri = new URI("http://192.168.137.1:5000/ws/user/update");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            socket = IO.socket(uri, options);
        }
        if (!socket.connected())
            socket.connect();

        socket.on("response", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args[0].toString().equals("200")) {
                    try {
                        user.setUserData(new JSONObject(args[1].toString()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.i("dslfhdkuf", args[1].toString());
                }
            }
        });
        return webSocketConnection;
    }

    public void emit(String event, String message) {
        socket.emit(event, message);
    }

    public void emit(String event, JSONObject data) {
        socket.emit(event, data);
    }

    public void disconnect(){
        socket.disconnect();
    }
}
