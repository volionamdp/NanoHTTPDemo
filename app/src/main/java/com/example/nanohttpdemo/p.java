package com.example.nanohttpdemo;

import java.util.ArrayList;

/* loaded from: classes2.dex */
public class p {
    private static final p c = new p();
    private ArrayList<String> a = new ArrayList<>();
    private ArrayList<Server.AbstractC0152a> b = new ArrayList<>();

    private p() {
    }

    public static p a() {
        return c;
    }

    public void a(String str, String str2, String str3) {
        this.a.add(str);
        this.b.add(new Server.b(str2, str3));
    }

    public Server.AbstractC0152a a(String str) {
        int indexOf = this.a.indexOf(str);
        if (indexOf >= 0) {
            return this.b.get(indexOf);
        }
        return null;
    }
}