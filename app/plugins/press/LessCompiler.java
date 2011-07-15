/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plugins.press;

import com.asual.lesscss.LessEngine;
import com.asual.lesscss.LessException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import org.mozilla.javascript.WrappedException;
import play.Logger;

/**
 *
 * @author benoit
 */
public class LessCompiler {

    LessEngine lessEngine;
    Boolean devMode;

    LessCompiler(Boolean devMode) {
        lessEngine = new LessEngine();
        this.devMode = devMode;
    }

    public String compile(File lessFile) {
        try {
            return lessEngine.compile(lessFile);
        } catch (LessException e) {
            return handleException(lessFile, e);
        }
    }

    public String compile(String lessContent) {
        try {
            return lessEngine.compile(lessContent);
        } catch (LessException e) {
            return handleException(null, e);
        }
    }
    
    public BufferedReader compile(BufferedReader in) throws IOException {
        
        StringBuilder sb = new StringBuilder();
        int c;
        while ((c = in.read()) != -1) {
            sb.append((char) c);
        }

        String css = compile(sb.toString());
        
        return new BufferedReader(new StringReader(css));
    }

    public String handleException(File lessFile, LessException e) {
        Logger.warn(e, "Less exception");

        String filename = e.getFilename();
        List<String> extractList = e.getExtract();
        String extract = null;
        if (extractList != null) {
            extract = extractList.toString();
        }

        // LessEngine reports the file as null when it's not an @imported file
        if (filename == null && lessFile != null) {
            filename = lessFile.getName();
        }

        // Try to detect missing imports (flaky)
        if (extract == null && e.getCause() instanceof WrappedException) {
            WrappedException we = (WrappedException) e.getCause();
            if (we.getCause() instanceof FileNotFoundException) {
                FileNotFoundException fnfe = (FileNotFoundException) we.getCause();
                extract = fnfe.getMessage();
            }
        }

        return formatMessage(filename, e.getLine(), e.getColumn(), extract, e.getErrorType());
    }

    public String formatMessage(String filename, int line, int column, String extract, String errorType) {
        return "body:before {display: block; color: #c00; white-space: pre; font-family: monospace; background: #FDD9E1; border-top: 1px solid pink; border-bottom: 1px solid pink; padding: 10px; content: \"[LESS ERROR] "
                + String.format("%s:%s: %s (%s)", filename, line, extract, errorType)
                + "\"; }";
    }
}