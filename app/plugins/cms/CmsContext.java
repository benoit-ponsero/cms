package plugins.cms;

import models.cms.NavigationItem;

/**
 * @author benoit
 */
public class CmsContext {
    
    public static class Constant {
        
        public static final String CMS_USER = "cms_userid";
    }
    
    public String           requestedResource;
    public NavigationItem   currentNavigationItem;
    public Boolean          isCmsEditingAuthorized;
    
    public boolean _tagGenerated = false;
    
    public static ThreadLocal<CmsContext> current = new ThreadLocal<CmsContext>();
    
    public static CmsContext current() {
        return current.get();
    }
    
    
}
