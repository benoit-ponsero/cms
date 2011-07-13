/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plugins.cms;

import play.mvc.Router.ActionDefinition;

/**
 *
 * @author benoit
 */
public class Tags {
    
    public static String url(ActionDefinition actionDefinition){
        
        String url = actionDefinition.url;
        
        return url;
    }
    
    public static String url(String path){
        
        return path;
    }
}
