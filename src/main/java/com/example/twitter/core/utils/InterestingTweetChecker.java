package com.example.twitter.core.utils;

import com.example.twitter.core.database.MongoDbUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class InterestingTweetChecker {

    private static MongoDbUtils mongoDbUtils = null;
    private static List<Pattern> regexPatterns = null;
    private static final int MAXVALUE = 5000;
    private static int counter = MAXVALUE + 1;

    private InterestingTweetChecker() {
        throw new IllegalStateException("InterestingTweetChecker");
    }

    public static synchronized boolean check(String text) {
        if (mongoDbUtils == null) {
            mongoDbUtils = new MongoDbUtils();
            counter = MAXVALUE + 1;
        }
        if (counter > MAXVALUE || regexPatterns == null) {
            updateRegex();
        }
        counter = counter + 1;
        for (Pattern pattern : regexPatterns) {
            if (pattern.matcher(text).matches())
                return true;
        }
        return false;
    }

    private static void updateRegex() {
        counter = 0;
        regexPatterns = new ArrayList<>();
        List<String> regex = mongoDbUtils.getRegex();
        for (String r : regex) {
            regexPatterns.add(Pattern.compile(r, Pattern.CASE_INSENSITIVE));
        }
    }

    public static synchronized void closeMongodb() {
        if (mongoDbUtils != null){
            mongoDbUtils.close();
        }
        System.out.println("Mongodb closed");
    }
}
