package com.fcmcpe.nuclear.core.language;

import cn.nukkit.Server;
import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.TextFormat;

import java.util.*;

/**
 * Created on 2015/12/15 by xtypr.
 * Package com.fcmcpe.nuclear.core.language in project NuclearPlugins .
 */
public final class NuclearDictionary {

    private static Map<Locale, Map<String, String>> entries = new HashMap<>();
    private static Map<Locale, Integer> trials = new HashMap<>();
    private static Map<Plugin, INILanguageLoader> loaders = new HashMap<>();

    public static void registerPath(Plugin plugin, String path) {
        loaders.remove(plugin);
        loaders.putIfAbsent(plugin, new INILanguageLoader(plugin, path));
    }

    private static void checkLocale(Locale locale){
        if(!entries.containsKey(locale) && !entries.containsKey(new Locale(locale.getLanguage())) ){
            if (trials.getOrDefault(locale, 0) <= 5) {
                loaders.forEach((p, l) -> {
                    INILanguageLoader.GetLanguageResult result;
                    Server.getInstance().getLogger().info("Loading dictionary: " + locale.getDisplayName());
                    trials.putIfAbsent(locale, 0);
                    int trial = trials.getOrDefault(locale, 0);
                    result = l.getLanguage(locale);
                    if (result != null) {
                        entries.put(result.getLocale(), result.getResult());
                        Server.getInstance().getLogger().notice("Loaded dictionary: " + locale.getDisplayName());
                    }
                    trial++;
                    trials.replace(locale, trial);
                });
            }
        }
    }

    public static String get(Locale locale, String msg, Map<String, String> params){
        return get(locale, msg, params, true);
    }

    public static String get(Locale locale, String msg, Map<String, String> params, boolean useDictionary){
        Objects.requireNonNull(locale);
        Objects.requireNonNull(msg);

        checkLocale(locale);
        checkLocale(Locale.ENGLISH);

        if(!entries.containsKey(locale)) locale = new Locale(locale.getLanguage());
        if(!entries.containsKey(locale)) locale = Locale.ENGLISH;
        if(!entries.containsKey(locale)) return "#LANGUAGE ERROR#";
        String rawMsg;

        if (useDictionary) {
            if (entries.get(locale).containsKey(msg)) {
                rawMsg = entries.get(locale).get(msg);
            }else if(entries.get(Locale.ENGLISH).containsKey(msg)) {
                rawMsg = entries.get(Locale.ENGLISH).get(msg);
            }else{
                return msg;
            }
        } else {
            rawMsg = msg;
        }

        params.putIfAbsent("{enter}", "\n");

        params.putIfAbsent("{black}", TextFormat.BLACK);
        params.putIfAbsent("{dark-blue}", TextFormat.DARK_BLUE);
        params.putIfAbsent("{dark-green}", TextFormat.DARK_GREEN);
        params.putIfAbsent("{dark-aqua}", TextFormat.DARK_AQUA);
        params.putIfAbsent("{dark-red}", TextFormat.DARK_RED);
        params.putIfAbsent("{dark-purple}", TextFormat.DARK_PURPLE);
        params.putIfAbsent("{gold}", TextFormat.GOLD);
        params.putIfAbsent("{gray}", TextFormat.GRAY);
        params.putIfAbsent("{dark-gray}", TextFormat.DARK_GRAY);
        params.putIfAbsent("{blue}", TextFormat.BLUE);
        params.putIfAbsent("{green}", TextFormat.GREEN);
        params.putIfAbsent("{aqua}", TextFormat.AQUA);
        params.putIfAbsent("{red}", TextFormat.RED);
        params.putIfAbsent("{light-purple}", TextFormat.LIGHT_PURPLE);
        params.putIfAbsent("{yellow}", TextFormat.YELLOW);
        params.putIfAbsent("{white}", TextFormat.WHITE);

        params.putIfAbsent("{obfuscated}", TextFormat.OBFUSCATED);
        params.putIfAbsent("{bold}", TextFormat.BOLD);
        params.putIfAbsent("{strike-through}", TextFormat.STRIKETHROUGH);
        params.putIfAbsent("{underline}", TextFormat.UNDERLINE);
        params.putIfAbsent("{italic}", TextFormat.ITALIC);
        params.putIfAbsent("{reset}", TextFormat.RESET);

        final String[] targetMsg = {rawMsg};
        params.forEach((from, to)->targetMsg[0] = targetMsg[0].replace(from, to));
        return targetMsg[0];
    }

    // param is like {0} {1}
    public static String get(Locale locale, String msg, String... params){
        Objects.requireNonNull(locale);
        Objects.requireNonNull(msg);
        Map<String, String> paramsMap = new HashMap<>();
        if(params != null){
            for(int i=0;i<params.length;i++){
                paramsMap.put("{"+i+"}", params[i]);
            }
        }

        return get(locale, msg, paramsMap);
    }

}
