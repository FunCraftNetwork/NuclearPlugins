package com.fcmcpe.nuclear.core.language;

/**
 * Created on 2015/12/15 by xtypr.
 * Package com.fcmcpe.nuclear.core.language in project NuclearPlugins .
 */

import cn.nukkit.plugin.Plugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created on 2015/12/10 by xtypr.
 * Package com.fcmcpe.nuclear.login.language in project NuclearLogin .
 */
public class INILanguageLoader {

    static class GetLanguageResult{
        private Locale locale;

        private Map<String, String> result;

        public Locale getLocale() {
            return locale;
        }

        public Map<String, String> getResult() {
            return result;
        }
    }

    private Plugin plugin;
    private String dirPath;

    public INILanguageLoader(Plugin plugin, String path) {
        this.plugin = plugin;
        this.dirPath = path;
    }

    GetLanguageResult getLanguage(Locale locale){
        //String dirPath = "com/fcmcpe/nuclear/login/language/";

        InputStream langStream = plugin.getResource(dirPath + locale.getLanguage() + "_" + locale.getCountry() + ".ini");

        if(langStream == null){
            langStream = plugin.getResource(dirPath + locale.getLanguage() + ".ini");
            locale = new Locale(locale.getLanguage());
        }
        if(langStream == null){
            plugin.getResource(dirPath + "en.ini");
            locale = Locale.ENGLISH;
        }
        if(langStream == null) return null;

        GetLanguageResult result = new GetLanguageResult();
        result.result = loadINI(langStream);
        result.locale = locale;

        return result;
    }

    //Code from Nukkit http://github.com/nukkit/nukkit
    public static Map<String, String> loadINI(InputStream stream) {
        try {
            String content = readFile(stream);
            Map<String, String> d = new HashMap<>();
            for (String line : content.split("\n")) {
                line = line.trim();
                if (line.equals("") || line.charAt(0) == '#') {
                    continue;
                }
                String[] t = line.split("=");
                if (t.length < 2) {
                    continue;
                }
                String key = t[0];
                String value = "";
                for (int i = 1; i < t.length - 1; i++) {
                    value += t[i] + "=";
                }
                value += t[t.length - 1];
                if (value.equals("")) {
                    continue;
                }
                d.put(key, value);
            }
            return d;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String readFile(InputStream inputStream) throws IOException {
        return readFile(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    private static String readFile(Reader reader) throws IOException {
        BufferedReader br = new BufferedReader(reader);
        String temp;
        StringBuilder stringBuilder = new StringBuilder();
        temp = br.readLine();
        while (temp != null) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("\n");
            }
            stringBuilder.append(temp);
            temp = br.readLine();
        }
        return stringBuilder.toString();
    }
}
