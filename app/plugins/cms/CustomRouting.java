package plugins.cms;


import plugins.cms.navigation.NavigationCache;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import models.cms.NavigationItem;
import models.cms.NavigationMappedItem;
import models.cms.VirtualPage;
import play.Logger;
import play.PlayPlugin;
import play.mvc.Http.Request;
import play.mvc.Router;
import play.mvc.Router.Route;
import play.mvc.Scope.RenderArgs;


/**
 * @author benoit
 */
public class CustomRouting extends PlayPlugin {

    
    @Override
    public void routeRequest(Request request) {
        
        String lang     = "fr";//Lang.get();
        String resource = request.path;
        
       
        VirtualPage virtualPage = NavigationCache.getVirtualPage(resource);
        if (virtualPage != null){
            
            // handle virtual page
            request.path = Router.reverse("cms.CmsController.virtualPage").url;
            return;
        }
        
        NavigationMappedItem mappedItem = NavigationCache.getMappedItem(lang, resource);
        if (mappedItem != null){
            
            if (mappedItem.redirect){
                
                request.path = Router.reverse("cms.CmsController.redirectMappedItem").url;
                return;
            }
            else {
                
                request.path = mappedItem.source;
            }
        }
    }

    
    @Override
    public void beforeActionInvocation(Method method) {
        
        String lang     = "fr";//Lang.get();
        String resource = Request.current().path;
        
        NavigationMappedItem mappedItem = NavigationCache.getMappedItem(lang, resource);
        if (mappedItem != null && !mappedItem.redirect){
            
            resource = mappedItem.source;
        }
        RenderArgs.current().put("__REQUESTED_RESOURCE", resource);
        
        NavigationItem item = NavigationCache.get(resource);
        if (item != null){
            RenderArgs.current().put("__CURRENT_NAVIGATION_ITEM", item);
        }
    }
    
    

    @Override
    public void onRequestRouting(Route route) {
        
        
        Request request = Request.current();
        
        try {
            
            String uri      = request.url;
            String encoding = request.encoding;
            
            final int i = uri.indexOf("?");
            
            String path = URLDecoder.decode(uri, encoding);
            if (i != -1) {
                path = URLDecoder.decode(uri.substring(0, i), encoding);   
            }

            request.path = path;
            //Logger.info("reset path to :" + path);
            
        } catch (UnsupportedEncodingException ex) {
            
            Logger.error("[CustomRoutingPlugin] Unable to reset request.path");
        }
    }
}
