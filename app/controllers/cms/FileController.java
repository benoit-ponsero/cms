package controllers.cms;

import java.io.File;
import play.Logger;
import play.Play;
import play.mvc.Controller;
import plugins.router.Route;

/**
 * @author benoit
 */
public class FileController extends Controller {
    
    @Route("/public/files/{filepath}")
    public static void files(String filepath) {
        
        String rootPath = Play.applicationPath.getAbsolutePath();
        rootPath = rootPath.substring(0, rootPath.lastIndexOf("/"));
        rootPath += "/__files/" + Play.configuration.getProperty("application.name");
        
        File file = new File(rootPath, filepath);
        
        if (file.exists()){
            renderBinary(file);
        }
        else {
            Logger.error("NOT FOUND: " + rootPath + filepath);
            error(404, "File not Found");
        }
    }
}
