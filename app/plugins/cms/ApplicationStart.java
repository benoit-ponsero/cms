package plugins.cms;

import plugins.cms.navigation.NavigationCache;
import play.jobs.Job;
import play.jobs.OnApplicationStart;

/**
 * @author benoit
 */
@OnApplicationStart
public class ApplicationStart extends Job {

    public void doJob() {
        
        NavigationCache.loadPlugins();
        
        NavigationCache.init();
    }
}
