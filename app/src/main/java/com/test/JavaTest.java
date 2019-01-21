package com.test;

import java.util.regex.Pattern;

/**
 * Created by gongkai on 2019/1/10.
 */

public class JavaTest {
    String s1 = "abc$";
    String s2 = "abc-d";
    static String s3 = "abcdefgh";

    public static void main(String[] args) {
        String patern = "^\\cd*g$";
        boolean matches = Pattern.matches(patern, s3);
        System.out.println(" = " + matches);
    }

}
