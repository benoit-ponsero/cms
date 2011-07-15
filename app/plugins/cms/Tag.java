package plugins.cms;

import play.mvc.Router.ActionDefinition;

/**
 * @author benoit
 */
public class Tag {
    
    public static String url(ActionDefinition actionDefinition){
        
        String url = actionDefinition.url;
        
        return url;
    }
    
    public static String url(String path){
        
        return path;
    }
}
