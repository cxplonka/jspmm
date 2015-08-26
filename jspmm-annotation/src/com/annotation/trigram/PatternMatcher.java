/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.annotation.trigram;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Christian
 */
public class PatternMatcher {

    public static final String WILDCARD_TEMPLATE = "\\(\\\\b\\[\\\\w'.-\\]\\+\\\\b\\)";
    public static final String ROOT_TEMPLATE = "(\\b[\\w'.-]+\\b)\\s(\\b[\\w'.-]+\\b)";

    public static void match(String value) {
        Pattern p = Pattern.compile("(?i)" + ROOT_TEMPLATE);
        Matcher m = p.matcher(value);

        while (m.find()) {
            System.out.println(m.group());
        }
    }

    public static void main(String[] arg) {
        match("Ich bin ein HSP und 27, sowie bla bla!");
    }
}
