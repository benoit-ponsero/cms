package controllers.press;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import play.exceptions.UnexpectedException;
import play.mvc.Controller;
import plugins.press.CSSCompressor;
import plugins.press.JSCompressor;
import plugins.press.PluginConfig;
import plugins.press.io.CompressedFile;
import plugins.press.io.FileIO;
import plugins.router.Route;

public class Press extends Controller {

    @Route("/--press/js/{key}")
    public static void getCompressedJS(String key) {
        key = FileIO.unescape(key);
        CompressedFile compressedFile = JSCompressor.getCompressedFile(key);
        renderCompressedFile(compressedFile, "JavaScript");
    }

    @Route("/--press/css/{key}")
    public static void getCompressedCSS(String key) {
        key = FileIO.unescape(key);
        CompressedFile compressedFile = CSSCompressor.getCompressedFile(key);
        renderCompressedFile(compressedFile, "CSS");
    }

    @Route("/--press/js/single/{key}")
    public static void getSingleCompressedJS(String key) {
        key = FileIO.unescape(key);
        CompressedFile compressedFile = JSCompressor.getSingleCompressedFile(key);
        renderCompressedFile(compressedFile, "JavaScript");
    }

    @Route("/--press/css/single/{key}")
    public static void getSingleCompressedCSS(String key) {
        key = FileIO.unescape(key);
        CompressedFile compressedFile = CSSCompressor.getSingleCompressedFile(key);
        renderCompressedFile(compressedFile, "CSS");
    }

    @Route("/--press/js/clear")
    public static void clearJSCache() {
        if (!PluginConfig.cacheClearEnabled) {
            forbidden();
        }

        int count = JSCompressor.clearCache();
        renderText("Cleared " + count + " JS files from cache");
    }

    @Route("/--press/css/clear")
    public static void clearCSSCache() {
        if (!PluginConfig.cacheClearEnabled) {
            forbidden();
        }

        int count = CSSCompressor.clearCache();
        renderText("Cleared " + count + " CSS files from cache");
    }

    private static void renderCompressedFile(CompressedFile compressedFile, String type) {
        if (compressedFile == null) {
            renderBadResponse(type);
        }

        response.setContentTypeIfNotSet("text/" + type.toLowerCase());
        
        renderBinary(compressedFile.inputStream(), compressedFile.length());
    }
    
    private static void renderBadResponse(String fileType) {
        String response = "/*\n";
        response += "The compressed " + fileType + " file could not be generated.\n";
        response += "This can occur in two situations:\n";
        response += "1. The time between when the page was rendered by the ";
        response += "server and when the browser requested the compressed ";
        response += "file was greater than the timeout. (The timeout is ";
        response += "currently configured to be ";
        response += PluginConfig.compressionKeyStorageTime + ")\n";
        response += "2. There was an exception thrown while rendering the ";
        response += "page.\n";
        response += "*/";
        renderBinaryResponse(response);
    }

    private static void renderBinaryResponse(String response) {
        try {
            renderBinary(new ByteArrayInputStream(response.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            throw new UnexpectedException(e);
        }
    }
}