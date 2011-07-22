package controllers.cms;

import models.cms.NavigationMappedItem;
import models.cms.VirtualPage;
import play.mvc.Controller;
import plugins.cms.navigation.NavigationCache;
import plugins.router.Route;

/**
 * @author benoit
 */
@Route("/--cms")
public class CmsController extends Controller {
    
    @Route("/clearcache")
    public static void ClearCache(){
        
        NavigationCache.init();
    }
    
    public static void redirectMappedItem(){
        
        String lang     = "fr";//Lang.get();
        String resource = request.path;
        
        NavigationMappedItem mappedItem = NavigationCache.getMappedItem(lang, resource);
        
        if (mappedItem != null){
            
            redirect(mappedItem.source);
        }
    }
    
    public static void virtualPage(){
        
        String resource = request.path;
        
        VirtualPage virtualPage = NavigationCache.getVirtualPage(resource);
        if (virtualPage != null){
            
            renderTemplate(virtualPage.view);
        }
    }
    
}
