package com.luntikvisuals.granny;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.webkit.WebView;
import java.net.*;
import java.io.*;
import java.util.*;

/** Basic UDP discovery + TCP relay skeleton for local multiplayer. */
public class LanRelay {
    private final Context ctx;
    private final WebView webView;
    private volatile boolean running = false;
    private ServerSocket server;
    private final List<Socket> clients = new ArrayList<>();
    private String roomCode = "0000";

    public LanRelay(Context ctx, WebView webView) {
        this.ctx = ctx;
        this.webView = webView;
    }

    public void startHost() {
        roomCode = String.format("%04d", new Random().nextInt(10000));
        running = true;
        new Thread(this::hostLoop).start();
        evalJs("onRoomCreated('" + roomCode + "')");
    }

    public void join(String code) {
        // TODO: discover host via UDP multicast and connect
        evalJs("alert('Подключение к комнате ' + '" + code + "' (скоро полная реализация)')");
    }

    public void broadcast(String json) {
        synchronized (clients) {
            for (Socket s : clients) {
                try {
                    PrintWriter w = new PrintWriter(s.getOutputStream(), true);
                    w.println(json);
                } catch (Exception ignored) {}
            }
        }
    }

    private void hostLoop() {
        try {
            server = new ServerSocket(0);
            int port = server.getLocalPort();
            // TODO: announce via multicast (WifiManager.MulticastLock)
            while (running) {
                Socket client = server.accept();
                synchronized (clients) { clients.add(client); }
                new Thread(() -> handleClient(client)).start();
            }
        } catch (Exception e) {
            evalJs("console.error('LanRelay: " + e.getMessage() + "')");
        }
    }

    private void handleClient(Socket s) {
        try (BufferedReader r = new BufferedReader(new InputStreamReader(s.getInputStream()))) {
            String line;
            while ((line = r.readLine()) != null && running) {
                final String msg = line;
                evalJs("onLanMessage(" + JSONObject.quote(msg) + ")");
            }
        } catch (Exception ignored) {}
    }

    public void stop() {
        running = false;
        try { if (server != null) server.close(); } catch (Exception ignored) {}
        synchronized (clients) {
            for (Socket s : clients) try { s.close(); } catch (Exception ignored) {}
            clients.clear();
        }
    }

    private void evalJs(String js) {
        webView.post(() -> webView.evaluateJavascript(js, null));
    }
}
