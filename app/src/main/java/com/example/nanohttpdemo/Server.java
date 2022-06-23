package com.example.nanohttpdemo;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import fi.iki.elonen.NanoHTTPD;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes2.dex */
public class Server extends NanoHTTPD {
    public static final String a = "a";
    private Map<String, AbstractC0152a> c = new ConcurrentHashMap();
    public static final String TYPE_DEL = "DELETE";
    public static final String TYPE_GET = "GET";
    public static final String TYPE_POST = "POST";
    public static final String TYPE_PUT = "PUT";
    public static final String TYPE_REQ = "request";
    public static final String TYPE_SUB = "subscribe";
    private Context context;

    /* renamed from: com.inshot.cast.xcast.service.a$a  reason: collision with other inner class name */
    /* loaded from: classes2.dex */
    public interface AbstractC0152a {
        String a();

        String b();
    }

    private void g() {
    }

    public Server(Context context,int i) {
        super(i);
        this.context = context;
    }

    @Override // defpackage.bek
    public Response serve(String str, Method mVar, Map<String, String> map, Map<String, String> map2, Map<String, String> map3) {
        AbstractC0152a a2  = p.a().a(str);
        Response nVar;
        Log.d(a, "receive request : " + (a2 == null));
        Log.i(a, "serve: " + map.toString());
//        if (str != null &&((a2 = this.c.get(str)) != null && (a2.a().startsWith("http") || a2.a().startsWith("content://") || a2.a().startsWith("file:///") || new File(a2.a()).exists() || a2.a().startsWith("WEVLF6IK:") || a2.a().startsWith("FAN_BIAN_YI_DE_SHI_SHA_BI:")))) {
//            String a3 = a2.a();
//            Log.i("filepathsss", a3);
            for (String str2 : map.keySet()) {
                Log.i("jfldlfjldslf", str2 + "=" + map.get(str2));
            }
            String str3 = map.get("range");
            boolean z = !TextUtils.isEmpty(map.get("getcontentfeatures.dlna.org"));
            try {
//                String b2 = a2.b();
//                if (a3.startsWith("FAN_BIAN_YI_DE_SHI_SHA_BI:")) {
//                    nVar = a(a3, b2, str3);
//                } else if (b2.equals("application/x-mpegurl")) {
//                    nVar = a(a3, b2);
//                } else {
//                    if (!a3.startsWith("http") && !a3.startsWith("gd_media:")) {
//                        if (str3 != null && !a3.startsWith("WEVLF6IK:")) {
//                            nVar = c(a3, b2, str3);
//                        }
//                        nVar = b(a3, b2);
//                    }
                    nVar = b(str.substring(1), "video/mp4", str3);
//                }
                b(nVar);
                if (z) {
                    c(nVar);
                }
                return nVar;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("ldsjfljslfjd", "serve: " + e.getMessage());
            }
//        }
        return newFixedLengthResponse(Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, new ByteArrayInputStream("File not found".getBytes()), -1L);
    }

    private Response a(String str, String str2, String str3) throws IOException {
        long j;
        long j2;
        g();
        URL url = new URL(str.substring(26, str.length()));
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        long contentLength = httpURLConnection.getContentLength();
        httpURLConnection.disconnect();
        long j3 = contentLength - 1;
        if (str3 != null) {
            String substring = str3.trim().substring(6);
            if (substring.startsWith("-")) {
                j = j3 - Long.parseLong(substring.substring(1));
                j2 = j3;
            } else {
                String[] split = substring.split("-");
                j = Long.parseLong(split[0]);
                j2 = split.length > 1 ? Long.parseLong(split[1]) : j3;
            }
            if (j2 > j3) {
                j2 = j3;
            }
        } else {
            j = 0;
            j2 = j3;
        }
        HttpURLConnection httpURLConnection2 = (HttpURLConnection) url.openConnection();
        httpURLConnection2.setRequestMethod(TYPE_GET);
        int responseCode = httpURLConnection2.getResponseCode();
        InputStream inputStream = (responseCode == 200 || responseCode == 206) ? httpURLConnection2.getInputStream() : null;
        if (str2 == null || !str2.startsWith("text/")) {
            long j4 = (j2 - j) + 1;
            Response nVar = newFixedLengthResponse(responseCode == 200 ? Response.Status.OK :  Response.Status.PARTIAL_CONTENT, str2, inputStream, j4);
            nVar.addHeader("Content-Length", String.valueOf(j4));
            nVar.addHeader("Content-Range", "bytes " + j + "-" + j2 + "/" + contentLength);
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(";charset=utf-8");
            nVar.addHeader(HttpMessage.CONTENT_TYPE_HEADER, sb.toString());
            nVar.addHeader("Access-Control-Allow-Origin", "*");
            return nVar;
        }
        Response nVar2 = newFixedLengthResponse( Response.Status.OK, null, inputStream, contentLength);
        nVar2.addHeader("Content-Length", String.valueOf(contentLength));
        nVar2.addHeader("Content-Range", "bytes");
        nVar2.addHeader(HttpMessage.CONTENT_TYPE_HEADER, str2 + ";charset=utf-8");
        nVar2.addHeader("Access-Control-Allow-Origin", "*");
        return nVar2;
    }

    private void b( Response nVar) {
        nVar.addHeader("Access-Control-Allow-Origin", "*");
    }

    private void c(Response nVar) {
        nVar.addHeader("contentFeatures.dlna.org", "DLNA.ORG_OP=01;DLNA.ORG_CI=0;DLNA.ORG_FLAGS=01700000000000000000000000000000");
        nVar.addHeader("TransferMode.DLNA.ORG", "Streaming");
        nVar.addHeader("RealTimeInfo.DLNA.ORG", "DLNA.ORG_TLAG=*");
    }

    private Response a(String str, String str2) throws IOException {
        InputStream fileInputStream;
        long j;
        g();
        if (str.startsWith("http")) {
            InputStream openStream = new URL(str).openStream();
            j = openStream.available();
            fileInputStream = openStream;
        } else {
            FileInputStream fileInputStream2 = new FileInputStream(str);
            j = fileInputStream2.available();
            fileInputStream = fileInputStream2;
        }
        if (str2 == null || !str2.startsWith("text/")) {
            Response nVar = newFixedLengthResponse(Response.Status.OK, str2, fileInputStream, j);
            nVar.addHeader("Content-Length", String.valueOf(j));
            nVar.addHeader("Content-Range", "bytes");
            nVar.addHeader(HttpMessage.CONTENT_TYPE_HEADER, str2 + ";charset=utf-8");
            nVar.addHeader("Access-Control-Allow-Origin", "*");
            return nVar;
        }
        Response nVar2 = newFixedLengthResponse(Response.Status.OK, null, fileInputStream, j);
        nVar2.addHeader("Content-Length", String.valueOf(j));
        nVar2.addHeader("Content-Range", "bytes");
        nVar2.addHeader(HttpMessage.CONTENT_TYPE_HEADER, str2 + ";charset=utf-8");
        nVar2.addHeader("Access-Control-Allow-Origin", "*");
        return nVar2;
    }

    private Pair<Long, InputStream> b(String str) throws IOException {
        InputStream fileInputStream = context.getContentResolver().openInputStream(Uri.parse(str));
        return new Pair((long)fileInputStream.available(),fileInputStream);
    }

    private Pair<Integer, InputStream> a(String str, long j, long j2) throws IOException {
        InputStream fileInputStream = context.getContentResolver().openInputStream(Uri.parse(str));
        int value = 200;
        if(j2 !=j) value = 206;
        return new Pair(value,fileInputStream);    }

    private Response b(String str, String str2) throws IOException {
        InputStream inputStream;
        long j;
        g();
        if (str.startsWith("content://") || str.startsWith("file:///")) {
            InputStream openInputStream = context.getContentResolver().openInputStream(Uri.parse(str));
            j = openInputStream.available();
            Log.i("jlfjdljfldjfldsf", j + "==========" + str);
            inputStream = openInputStream;
        } else if (str.startsWith("http")) {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            j = httpURLConnection.getContentLength();
            inputStream = httpURLConnection.getInputStream();
//        } else if (str.startsWith("WEVLF6IK:")) {
//            j = -1;
//            inputStream = new aug(awx.a(new File(str.substring(9, str.length()))));
        } else if (str.startsWith("gd_media:")) {
            Pair<Long, InputStream> b2 = b(str.replaceFirst("gd_media:", ""));
            inputStream = (InputStream) b2.second;
            j = ((Long) b2.first).longValue();
        } else {
            Log.d("vvvet", "b: "+str);
//            FileInputStream fileInputStream = new FileInputStream(str);

            inputStream = context.getContentResolver().openInputStream(Uri.parse(str));
            j = inputStream.available();
        }
        if (str2 == null || !str2.startsWith("text/")) {
            Response nVar = newFixedLengthResponse(Response.Status.OK, str2, inputStream, j);
            nVar.addHeader("Proxy-Connection", "Keep-alive");
            nVar.addHeader("Access-Control-Allow-Origin", "*");
            return nVar;
        }
        Response nVar2 = newFixedLengthResponse(Response.Status.OK, null, inputStream, j);
        nVar2.addHeader("Content-Length", String.valueOf(j));
        nVar2.addHeader("Content-Range", "bytes");
        nVar2.addHeader(HttpMessage.CONTENT_TYPE_HEADER, str2 + ";charset=utf-8");
        nVar2.addHeader("Access-Control-Allow-Origin", "*");
        return nVar2;
    }

    private Response b(String str, String str2, String str3) throws IOException {
        long j;
        long j2;
        long j3;
        InputStream inputStream;
        int i;
        int i2;
        Log.d("zzzzeet", "b: "+str2+"  "+str2.equals("application/x-mpegurl"));
        if (str2.equals("application/x-mpegurl") || str3 == null) {
            return b(str, str2);
        }
        g();
        if (str.startsWith("http")) {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(str).openConnection();
            j = httpURLConnection.getContentLength();
            httpURLConnection.disconnect();
        } else {
            Pair<Long, InputStream> b2 = b(str.replaceFirst("gd_media:", ""));
            j =  b2.first;
            ((InputStream) b2.second).close();
        }
        String substring = str3.trim().substring(6);
        if (substring.startsWith("-")) {
            long j4 = j - 1;
            j2 = j4 - Long.parseLong(substring.substring(1));
            j3 = j4;
        } else {
            String[] split = substring.split("-");
            j2 = Long.parseLong(split[0]);
            j3 = split.length > 1 ? Long.parseLong(split[1]) : j - 1;
        }
        long j5 = j - 1;
        if (j3 > j5) {
            j3 = j5;
        }
        if (str.startsWith("http")) {
            HttpURLConnection httpURLConnection2 = (HttpURLConnection) new URL(str).openConnection();
            if (j2 <= j3) {
                httpURLConnection2.setRequestProperty("Range", "bytes=" + j2 + "-" + j3);
            }
            httpURLConnection2.setRequestMethod(TYPE_GET);
            i = httpURLConnection2.getResponseCode();
            if (i == 200 || i == 206) {
                inputStream = httpURLConnection2.getInputStream();
                i2 = 200;
            } else {
                i2 = 200;
                inputStream = null;
            }
        } else {
            String replaceFirst = str.replaceFirst("gd_media:", "");
            i2 = 200;
            Pair<Integer, InputStream> a2 = a(replaceFirst, j2, j3);
            int intValue = a2.first;
            if (intValue == 200 || intValue == 206) {
                inputStream = (InputStream) a2.second;
                i = intValue;
            } else {
                i = intValue;
                inputStream = null;
            }
        }
        Log.d("kkkkkzz", "b: "+j2+"  "+j3);
        if (str2.startsWith("text/")) {
            Response nVar = newFixedLengthResponse(Response.Status.OK, null, inputStream, j);
            nVar.addHeader("Content-Length", String.valueOf(j));
            nVar.addHeader("Content-Range", "bytes");
            nVar.addHeader(HttpMessage.CONTENT_TYPE_HEADER, str2 + ";charset=utf-8");
            nVar.addHeader("Access-Control-Allow-Origin", "*");
            return nVar;
        }
        long j6 = (j3 - j2) + 1;
        Response nVar2 = newFixedLengthResponse(i == i2 ? Response.Status.OK : Response.Status.PARTIAL_CONTENT, str2, inputStream, j6);
        nVar2.addHeader("Content-Length", String.valueOf(j6));
        nVar2.addHeader("Content-Range", "bytes " + j2 + "-" + j3 + "/" + j);
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append(";charset=utf-8");
        nVar2.addHeader(HttpMessage.CONTENT_TYPE_HEADER, sb.toString());
        nVar2.addHeader("Access-Control-Allow-Origin", "*");
        return nVar2;
    }

    private Response c(String str, String str2, String str3) throws IOException {
        long j;
        InputStream fileInputStream;
        long j2;
        long j3;
        String substring = str3.trim().substring(6);
        if (str.startsWith("content://") || str.startsWith("file:///")) {
            InputStream openInputStream = context.getContentResolver().openInputStream(Uri.parse(str));
            j = openInputStream.available();
            fileInputStream = openInputStream;
        } else {
            File file = new File(str);
            FileInputStream fileInputStream2 = new FileInputStream(file);
            j = file.length();
            fileInputStream = fileInputStream2;
        }
        if (substring.startsWith("-")) {
            long j4 = j - 1;
            j2 = j4 - Long.parseLong(substring.substring(1));
            j3 = j4;
        } else {
            String[] split = substring.split("-");
            j2 = Long.parseLong(split[0]);
            j3 = split.length > 1 ? Long.parseLong(split[1]) : j - 1;
        }
        long j5 = j - 1;
        if (j3 <= j5) {
            j5 = j3;
        }
        if (j2 > j5) {
            return newFixedLengthResponse(Response.Status.RANGE_NOT_SATISFIABLE, null, null, -1L);
        }
        long j6 = (j5 - j2) + 1;
        g();
        fileInputStream.skip(j2);
        Response nVar =newFixedLengthResponse(Response.Status.PARTIAL_CONTENT, str2, fileInputStream, j6);
        nVar.addHeader("Content-Length", j6 + "");
        nVar.addHeader("Content-Range", "bytes " + j2 + "-" + j5 + "/" + j);
        if (str2.startsWith("text")) {
            nVar.addHeader(HttpMessage.CONTENT_TYPE_HEADER, str2 + ";charset=utf-8");
        } else {
            nVar.addHeader(HttpMessage.CONTENT_TYPE_HEADER, str2);
        }
        nVar.addHeader("Access-Control-Allow-Origin", "*");
        return nVar;
    }



    /* loaded from: classes2.dex */
    public static class b implements AbstractC0152a {
        private String a;
        private String b;

        public b(String str, String str2) {
            this.a = str;
            this.b = str2;
        }

        @Override // com.inshot.cast.xcast.service.a.AbstractC0152a
        public String a() {
            return this.a;
        }

        @Override // com.inshot.cast.xcast.service.a.AbstractC0152a
        public String b() {
            return this.b;
        }
    }
}