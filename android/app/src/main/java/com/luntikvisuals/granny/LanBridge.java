package com.luntikvisuals.granny;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.content.Context;

/** Simplified LAN bridge for Granny (pattern from Windup Wonderland). */
public class LanBridge {
    private final Context ctx;
    private final WebView webView;
    private LanRelay relay;

    public LanBridge(Context ctx, WebView webView) {
        this.ctx = ctx;
        this.webView = webView;
    }

    @JavascriptInterface
    public void createRoom() {
        if (relay == null) relay = new LanRelay(ctx, webView);
        relay.startHost();
    }

    @JavascriptInterface
    public void joinRoom(String code) {
        if (relay == null) relay = new LanRelay(ctx, webView);
        relay.join(code);
    }

    @JavascriptInterface
    public void sendState(String json) {
        if (relay != null) relay.broadcast(json);
    }

    public void shutdown() {
        if (relay != null) { relay.stop(); relay = null; }
    }
}
