package com.github.debris.debrisclient.unsafe.mod;

import me.towdium.jecharacters.util.Match;
import me.towdium.pinin.elements.Char;
import me.towdium.pinin.elements.Pinyin;

public class JechAccess {
    public static String convertToLetters(char c) {
        Char aChar = Match.context.getChar(c);
        Pinyin[] pinYins = aChar.pinyins();
        if (pinYins.length == 0) {
            return String.valueOf(c);
        } else {
            return pinYins[0].toString();
        }
    }

    public static int compareChar(char c1, char c2) {
        return convertToLetters(c1).compareToIgnoreCase(convertToLetters(c2));
    }

    public static boolean matchesFilter(String entryString, String filterText) {
        return Match.matches(entryString, filterText);
    }
}
