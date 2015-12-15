package com.fcmcpe.nuclear.core.ipgeo;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;
import java.util.Map;

/**
 * Created on 2015/12/10 by xtypr.
 * Package com.fcmcpe.nuclear.core.ipgeo in project NuclearLogin .
 */
public class BaiduIPGEO implements IPGEOEngine {
    Gson gson = new Gson();

    @Override
    public Locale getLocaleFromIP(String ip) {

        String httpUrl = "http://apis.baidu.com/apistore/iplookupservice/iplookup";
        String httpArg = "ip=" + ip;

        BufferedReader reader;
        String result = null;
        StringBuilder sbf = new StringBuilder();
        httpUrl = httpUrl + "?" + httpArg;

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            // 填入apikey到HTTP header
            connection.setRequestProperty("apikey",  "126700afc5c2a15c0c9dfc413a8b47d4");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        LocaleGetResult resultJson = gson.fromJson(result, LocaleGetResult.class);

        String countryStr = resultJson.retData.getOrDefault("country", "错误");
        if(countryStr.isEmpty()) countryStr = "错误";
        switch (countryStr){
            case "中国":
                return Locale.SIMPLIFIED_CHINESE;
            case "中国香港":
            case "中国台湾":
            case "中国澳门":
                return Locale.TRADITIONAL_CHINESE;
            case "俄罗斯":
                return new Locale("ru");
            case "错误":
            default:
                return Locale.ENGLISH;
        }
    }

    class LocaleGetResult{
        //int errNum;

        //String errMsg;

        Map<String, String> retData;
    }
}
