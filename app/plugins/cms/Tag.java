package plugins.cms;

import java.util.List;
import models.cms.Editor;
import models.cms.NavigationItem;
import models.cms.Role;
import models.cms.User;
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
        
        CmsContext cmsRequest = CmsContext.current();
        if (cmsRequest.isCmsEditingAuthorized == null){
            
            cmsRequest.isCmsEditingAuthorized = false;
            
            String userid = Scope.Session.current().get("cms_userid");
            if (userid != null){
                
                User user       = User.findById(Long.parseLong(userid));
                Role cmsEditor  = Role.find("byName", "cms_editor").first();
                
                if (user.roles.contains(cmsEditor)){
                    
                    cmsRequest.isCmsEditingAuthorized = true;
                }
            }
        }
                
        if (cmsRequest.isCmsEditingAuthorized && !cmsRequest._tagGenerated ){
            
            content += generateCommon();
            cmsRequest._tagGenerated = true;
        }
        
        Editor editor = Editor.findByPathAndCodeAndLanguage(path, code, lang);
        
        content += "<div class=\"cms_editor\" data-code=\""+code+"\">";        
        
        if (cmsRequest.isCmsEditingAuthorized
                && (editor == null || editor.content.isEmpty())){
            content += "Type your text here";   
        }
        else{
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

        value.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"/public/javascripts/elrte-1.3/css/elrte.min.css\" media=\"screen\" charset=\"utf-8\"/>");
        value.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"/public/javascripts/elfinder-1.2/css/elfinder.css\" media=\"screen\" charset=\"utf-8\"/>");
        value.append("<script type=\"text/javascript\" src=\"/public/javascripts/elrte-1.3/js/elrte.min.js\"></script>");
        value.append("<script type=\"text/javascript\" src=\"/public/javascripts/elfinder-1.2/js/elfinder.min.js\"></script>");
        value.append("<script type=\"text/javascript\" src=\"/public/javascripts/elfinder-1.2/js/i18n/elfinder.fr.js\"></script>");
        value.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"/public/cms/cms.css\"/>");
        value.append("<script type=\"text/javascript\" src=\"/public/cms/depend.js\"></script>");
        value.append("<script type=\"text/javascript\" src=\"/public/cms/cms.js\"></script>");
        value.append("<script type=\"text/javascript\">");

        value.append("cms.requestedResource = '").append(path).append("';");
        value.append("cms.language = '").append(lang).append("';");
        //value.append("nemo.logged = ").append(logged).append(";");
        value.append("</script>");
        
        return value.toString();
    }
    
    
}
