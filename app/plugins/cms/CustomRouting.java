package plugins.cms;


import plugins.cms.navigation.NavigationCache;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import models.cms.NavigationMappedItem;
import models.cms.VirtualPage;
import play.Logger;
import play.PlayPlugin;
import play.i18n.Lang;
import play.mvc.Http.Request;
import play.mvc.Router;
import play.mvc.Router.Route;

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
