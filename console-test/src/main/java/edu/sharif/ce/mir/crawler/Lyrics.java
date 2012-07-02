package edu.sharif.ce.mir.crawler;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: vahid
 * Date: 6/27/12
 * Time: 10:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class Lyrics {
    private Pattern p = Pattern.compile("text/html;\\s+charset=([^\\s]+)\\s*");
    private Pattern txtPatern1 = Pattern.compile("</a></div>&#.+<!--");
    private Pattern charPatern = Pattern.compile("&#\\d+;");
    private final String baseUrl = "http://lyrics.wikia.com/";

    public String getHtml(String strUrl) throws IOException {
        URL url = null;
        url = new URL(strUrl.replaceAll(" ","_"));
        URLConnection con = url.openConnection();
        Matcher m = p.matcher(con.getContentType());
/* If Content-Type doesn't match this pre-conception, choose default and
 * hope for the best. */
        String charset = m.matches() ? m.group(1) : "ISO-8859-1";
        Reader r = new InputStreamReader(con.getInputStream(), charset);
        StringBuilder buf = new StringBuilder();
        while (true) {
            int ch = r.read();
            if (ch < 0)
                break;
            buf.append((char) ch);
        }
        return buf.toString();
    }

    public String getLyric(String artist, String songName) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(baseUrl);
        sb.append(artist);
        sb.append(":");
        sb.append(songName);

        String html = getHtml(sb.toString());
        return getTxt(html);  //To change body of created methods use File | Settings | File Templates.
    }


    public String getTxt(String html) {
//        System.out.println(html);
        Matcher m1 = txtPatern1.matcher(html);
        String texts = null;
        if (m1.find()) {
//            System.out.println("-------------------------------------------------------");
            String txt = m1.group(0);
            txt = txt.substring(10, txt.length() - 4);
            char[] text = new char[txt.length() / 4];
            Matcher nums = charPatern.matcher(txt);
            int i = 0;
            while (nums.find()) {
                String tmp = nums.group();
                text[i] = (char) Integer.parseInt(tmp.substring(2, tmp.length() - 1));
                i++;
            }
           texts = new String(text);
//            System.out.println(text);
//            System.out.println(texts);
//            System.out.println("-------------------------------------------------------");
        }
        return texts;  //To change body of created methods use File | Settings | File Templates.
    }

}
