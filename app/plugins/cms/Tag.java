package plugins.cms;

import java.util.List;
import models.cms.Editor;
import models.cms.NavigationItem;
import play.mvc.Http;
import play.mvc.Router.ActionDefinition;
import play.mvc.Scope;

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
    
    public static String editor(String code){
        
        Http.Request request = Http.Request.current();
        
        String path = request.path;
        String lang = "fr";
        
        String content = "";
        
        Scope.Params scopeRequest = Scope.Params.current();
        
        if (scopeRequest.get("__CMS_TAG_GENERATED") == null ){
            
            content += generateCommon();
            scopeRequest.put("__CMS_TAG_GENERATED", "");
        }
        
        content += "<div class=\"cms_editor\">";        
        Editor editor = Editor.findByPathAndCodeAndLanguage(path, code, lang);
        if (editor != null){
            
            content += editor.content;
        }
        content += "</div>";
        
        return content;
    }
    
    public static String translate(String code){
        
        return code;
    }
    
    public static NavigationItem rootNavigationItem(){
        
        List<NavigationItem> items = NavigationItem.findByParent(null);
        
        if (items.isEmpty()){
            return null;
        }
        
        return items.get(0);
    }
    
    private static String generateCommon(){
        
        Http.Request request = Http.Request.current();
        
        String path     = request.path;
        String lang     = "fr";
        //String logged   = "true";
        
        StringBuilder value = new StringBuilder();

        value.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"/public/cms/cms.css\"/>");
        value.append("<script type=\"text/javascript\" src=\"/public/cms/cms.js\"></script>");
        value.append("<script type=\"text/javascript\">");

        value.append("cms.requestedResource = '").append(path).append("';");
        value.append("cms.language = '").append(lang).append("';");
        //value.append("nemo.logged = ").append(logged).append(";");
        value.append("</script>");
        
        return value.toString();
    }
    
    
}
