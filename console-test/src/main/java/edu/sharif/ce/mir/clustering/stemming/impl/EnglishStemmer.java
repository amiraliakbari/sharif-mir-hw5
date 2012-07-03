package edu.sharif.ce.mir.clustering.stemming.impl;


import edu.sharif.ce.mir.clustering.stemming.Stemmer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (19/2/12, 0:52)
 */
public class EnglishStemmer implements Stemmer {
    private Map<String, String> exceptions = new HashMap<String, String>();

    public EnglishStemmer() {
        exceptions.put("skis", "ski");
        exceptions.put("skies", "sky");
        exceptions.put("dying", "die");
        exceptions.put("lying", "lie");
        exceptions.put("tying", "tie");
        exceptions.put("idly", "idl");
        exceptions.put("gently", "gentl");
        exceptions.put("ugly", "ugli");
        exceptions.put("early", "earli");
        exceptions.put("only", "onli");
        exceptions.put("singly", "singl");
        exceptions.put("sky", "sky");
        exceptions.put("news", "news");
        exceptions.put("howe", "howe");
        exceptions.put("atlas", "atlas");
        exceptions.put("cosmos", "cosmos");
        exceptions.put("bias", "bias");
        exceptions.put("andes", "andes");
    }

    @Override
    public String stem(String string) {
        //System.out.println("stemming " + string);
        string = string.toLowerCase();

        if (exceptions.containsKey(string)) {
            return exceptions.get(string);
        }

        string = replaceY(string);
//        System.out.println(string);
        string = doStep1a(string);
        string = doStep1b(string);
        string = doStep1c(string);
        string = doStep2(string);
        string = doStep3(string);
        string = doStep4(string);
        string = doStep5(string);

        string = string.toLowerCase();
        return string;
    }

    private String replaceY(String string) {
        int i = 0, index;
        while ((index = string.indexOf('y', i)) >= 0) {
            if (index == 0 || isVowel(string.charAt(index - 1))) {
                String newStr = string.substring(0, index) + "Y";
                if (index < string.length()) {
                    newStr += string.substring(index + 1, string.length());
                }
                string = newStr;
            }
            i = index + 1;
        }
        return string;
    }

    private String doStep1a(String string) {
        if (string.endsWith("sses")) {
            string = replaceSuffix(string, "sses", "ss");
        } else if (string.endsWith("ied") || string.endsWith("ies")) {
            if (string.length() > 4) {
                string = replaceSuffix(string, 3, "i");
            } else {
                string = replaceSuffix(string, 3, "ie");
            }
        } else if (string.endsWith("s")) {
            if (containsVowel(string.substring(0, string.length() - 2))) {
                string = replaceSuffix(string, "s", "");
            }
        }
        return string;
    }

    private String doStep1b(String string) {
        if (string.endsWith("eedly") || string.endsWith("eed")) {
            String suffix;
            if (string.endsWith("eedly")) {suffix = "eedly";}
            else {suffix = "eed";}
            if (string.length() - suffix.length() >= getR1Position(string)) {
                string = replaceSuffix(string, suffix, "ee");
            }
        } else if (string.endsWith("edly") || string.endsWith("ingly")
                || string.endsWith("ed") || string.endsWith("ing")) {
            String suffix;
            if (string.endsWith("edly")) {suffix = "edly";}
            else if (string.endsWith("ingly")) {suffix = "ingly";}
            else if (string.endsWith("ed")) {suffix = "ed";}
            else {suffix = "ing";}
            String preceding = string.substring(0, string.length() - suffix.length());
            if (containsVowel(preceding)) {
                if (preceding.endsWith("at") || preceding.endsWith("bl") || preceding.endsWith("iz")
                        || preceding.endsWith("e")) {
                    string = replaceSuffix(string, suffix, "e");
                } else if (endsWithDouble(preceding)) {
                    string = replaceSuffix(string, suffix.length() + 1, "");
                } else if (isShortWord(preceding)) {
                    string = replaceSuffix(string, suffix, "e");
                } else {
                    string = replaceSuffix(string, suffix, "");
                }
            }
        }
        return string;
    }

    private String doStep1c(String string) {
        if (string.endsWith("y") || string.endsWith("Y")) {
            if(string.length() - 2 != 0 && !isVowel(string.charAt(string.length() - 2))) {
                string = replaceSuffix(string, 1, "i");
            }
        }
        return string;
    }

    private String doStep2(String string) {
        Map<String, String> step2Suffixes = new HashMap<String, String>();
        step2Suffixes.put("tional", "tion");
        step2Suffixes.put("enci", "ence");
        step2Suffixes.put("anci", "ance");
        step2Suffixes.put("abli", "able");
        step2Suffixes.put("entli", "ent");
        step2Suffixes.put("izer", "ize");
        step2Suffixes.put("ization", "ize");
        step2Suffixes.put("ational", "ate");
        step2Suffixes.put("ation", "ate");
        step2Suffixes.put("ator", "ate");
        step2Suffixes.put("alism", "al");
        step2Suffixes.put("aliti", "al");
        step2Suffixes.put("alli", "al");
        step2Suffixes.put("fulness", "ful");
        step2Suffixes.put("ousli", "ous");
        step2Suffixes.put("ousness", "ous");
        step2Suffixes.put("iveness", "ive");
        step2Suffixes.put("iviti", "ive");
        step2Suffixes.put("biliti", "ble");
        step2Suffixes.put("bli", "ble");
        step2Suffixes.put("ogi", "og");
        step2Suffixes.put("fulli", "ful");
        step2Suffixes.put("lessli", "less");
        step2Suffixes.put("li", "");
        for (String suffix : step2Suffixes.keySet()) {
            if (string.endsWith(suffix) && string.length() - suffix.length() >= getR1Position(string)) {
                if ((suffix.equals("ogi") && !string.endsWith("logi"))
                        || (suffix.equals("li") && isValidLiEnding(string.charAt(string.length() - suffix.length() - 1)))) {
                    continue;
                }
                return replaceSuffix(string, suffix, step2Suffixes.get(suffix));
            }
        }
        return string;
    }

    private String doStep3(String string) {
        Map<String, String> step3Suffixes = new HashMap<String, String>();
        step3Suffixes.put("tional", "tion");
        step3Suffixes.put("ational", "ate");
        step3Suffixes.put("alize", "al");
        step3Suffixes.put("icate", "ic");
        step3Suffixes.put("iciti", "ic");
        step3Suffixes.put("ical", "ic");
        step3Suffixes.put("ful", "");
        step3Suffixes.put("ness", "");
        step3Suffixes.put("ative", "");
        for (String suffix : step3Suffixes.keySet()) {
            if (string.endsWith(suffix) && string.length() - suffix.length() >= getR1Position(string)) {
                if (suffix.equals("ative") && string.length() - suffix.length() < getR2Position(string)) {
                    continue;
                }
                return replaceSuffix(string, suffix, step3Suffixes.get(suffix));
            }
        }
        return string;
    }

    private String doStep4(String string) {
        String[] step4Suffixes = new String[] {"al", "ance", "ence", "er", "ic", "able", "ible",
                "ant", "ement", "ment", "ent", "ism", "ate", "iti", "ous", "ive", "ize", "ion"};
        for (String suffix : step4Suffixes) {
            if (string.endsWith(suffix) && string.length() - suffix.length() >= getR2Position(string)) {
                if (suffix.equals("ion") && !string.endsWith("sion") && !string.endsWith("tion")) {
                    continue;
                }
                return replaceSuffix(string, suffix, "");
            }
        }
        return string;
    }

    private String doStep5(String string) {
        if ((string.endsWith("e")
                && (
                string.length() - 1 >= getR2Position(string)
                        || (string.length() - 1 >= getR1Position(string) && !isShortWord(string.substring(0, string.length() - 1)))
        ))
                ||
                (string.endsWith("l") && string.length() - 1 >= getR2Position(string) && string.endsWith("ll"))
                ) {
            string = string.substring(0, string.length() - 1);
        }
        return string;
    }

    private boolean isVowel(char ch) {
        String vowels = "aeiouy";
        return vowels.contains(String.valueOf(ch));
    }
    
    private boolean containsVowel(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (isVowel(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }
    
    private boolean endsWithDouble(String str) {
        if (str.length() < 2) {
            return false;
        }
        String[] doubles = new String[]{"bb", "dd", "ff", "gg", "mm", "nn", "pp", "rr", "tt"};
        String end = str.substring(str.length() - 2, str.length());
        for (String doubl : doubles) {
            if (end.equals(doubl)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidLiEnding(char ch) {
        String validLiEndings = "cdeghkmnrt";
        return validLiEndings.contains(String.valueOf(ch));
    }
    
    private int getR1Position(String str) {
        boolean afterVowel = false;
        for (int i = 0; i < str.length(); i++) {
            if (isVowel(str.charAt(i))) {
                afterVowel = true;
            } else {
                if (afterVowel) {
                    return i + 1;
                }
            }
        }
        return str.length();
    }

    private int getR2Position(String str) {
        int r1Pos = getR1Position(str);
        boolean afterVowel = false;
        for (int i = r1Pos; i < str.length(); i++) {
            if (isVowel(str.charAt(i))) {
                afterVowel = true;
            } else {
                if (afterVowel) {
                    return i + 1;
                }
            }
        }
        return str.length();
    }
    
    private boolean isShortSyllable(String str, int position) {
        return (str.length() == 3 && !isVowel(str.charAt(0)) && isVowel(str.charAt(1))
                && !isVowel(str.charAt(2)) && !"wxY".contains(str.substring(2,3)))
                ||
                (str.length() == 2 && position == 0 && isVowel(str.charAt(0)) && !isVowel(str.charAt(1)));
    }

    private boolean isShortWord(String str) {
        int len = str.length();
        return (getR1Position(str) == len)
                && (
                    (str.length() >= 3 && isShortSyllable(str.substring(len - 3, len), len - 3))
                    || (str.length() == 2 && isShortSyllable(str, 0))
                );
    }

    private String replaceSuffix(String str, int suffixLen, String newSuffix) {
        int strLen = str.length();
        return str.substring(0, strLen - suffixLen) + newSuffix;
    }

    private String replaceSuffix(String str, String suffix, String newSuffix) {
        return replaceSuffix(str, suffix.length(), newSuffix);
    }
}
