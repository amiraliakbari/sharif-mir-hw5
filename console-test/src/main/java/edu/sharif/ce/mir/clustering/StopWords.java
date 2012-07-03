package edu.sharif.ce.mir.clustering;

import java.io.InputStream;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Created by IntelliJ IDEA.
 * User: vahid
 * Date: 7/3/12
 * Time: 8:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class StopWords {
    private String StopWords;

    public StopWords() {
        InputStream stream = StopWords.class
                .getResourceAsStream("/StopWords.txt");
        Scanner sc = null;
        sc = new Scanner(stream);
        StringBuilder sb = new StringBuilder();
        while (sc.hasNext()) {
            String str = sc.nextLine();
            sb.append(str);
        }
        StopWords = sb.toString();
    }

    public boolean isStopWord(String str) {
        if (!StopWords.contains(str))
            return true;
        return false;
    }

    public String removeStopWords(String str) {
        StringTokenizer st = new StringTokenizer(str, " ");
        StringBuilder sb = new StringBuilder();
        while (st.hasMoreTokens()) {
            String t = st.nextToken();
            if (!StopWords.contains(t))
                sb.append(t);
            sb.append(" ");
        }
        return sb.toString();
    }
}
