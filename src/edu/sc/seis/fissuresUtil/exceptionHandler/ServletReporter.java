/**
 * ServletReporter.java
 *
 * @author Created by Omnicore CodeGuide
 */

package edu.sc.seis.fissuresUtil.exceptionHandler;
import edu.sc.seis.fissuresUtil.exceptionHandler.GUIReporter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import org.apache.log4j.Logger;



public class ServletReporter implements ExceptionReporter{
    
    public void report(String message, Throwable e, Map parsedContents) {
        try {
            URL url = new URL(System.getProperty("errorHandlerServlet"));
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            BufferedWriter out = new BufferedWriter( new OutputStreamWriter(http.getOutputStream()));
            out.write("bugreport="+ message);
            out.write(ExceptionReporterUtils.getTrace(e));
            out.write(ExceptionReporterUtils.getSysInfo());
            out.write("\r\n");
            out.close();
            http.connect();
            BufferedReader read = new BufferedReader(new InputStreamReader(http.getInputStream()));
            String s;
            while ((s = read.readLine()) != null) {
                logger.debug(s);
            }
            read.close();
        } catch (IOException ex) {
            logger.error("Problem sending error to server", ex);
        }
    }
    
    private static Logger logger = Logger.getLogger(ServletReporter.class);
}
