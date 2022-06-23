package com.example.nanohttpdemo;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

/* renamed from: aug  reason: default package */
/* loaded from: classes2.dex */
public class aug extends InputStream {
    private InputStream a;
    private Vector<String> c = new Vector<>();
    private Enumeration<String> b = this.c.elements();

    public aug(ArrayList<String> arrayList) {
        this.c.addAll(arrayList);
        try {
            a();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void a() throws IOException {
        InputStream inputStream = this.a;
        if (inputStream != null) {
            inputStream.close();
        }
        if (this.b.hasMoreElements()) {
            this.a = new URL(this.b.nextElement()).openStream();
            if (this.a == null) {
                throw new NullPointerException("Cannot unit a null inputStream !");
            }
            return;
        }
        this.a = null;
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr, int i, int i2) throws IOException {
        InputStream inputStream = this.a;
        if (inputStream == null) {
            return -1;
        }
        if (bArr == null) {
            throw new NullPointerException();
        } else if (i < 0 || i2 < 0 || i2 > bArr.length - i) {
            throw new IndexOutOfBoundsException();
        } else if (i2 == 0) {
            return 0;
        } else {
            int read = inputStream.read(bArr, i, i2);
            if (read > 0) {
                return read;
            }
            a();
            return read(bArr, i, i2);
        }
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        InputStream inputStream = this.a;
        if (inputStream == null) {
            return -1;
        }
        int read = inputStream.read();
        if (read != -1) {
            return read;
        }
        a();
        return read();
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        InputStream inputStream = this.a;
        if (inputStream == null) {
            return 0;
        }
        return inputStream.available();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        do {
            a();
        } while (this.a != null);
    }
}