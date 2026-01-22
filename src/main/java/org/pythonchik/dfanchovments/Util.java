package org.pythonchik.dfanchovments;

public class Util {
    public static String toRoman(int lvl) {
        return switch (lvl) {
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
            case 5 -> "V";
            default -> String.valueOf(lvl);
        };
    }

}
