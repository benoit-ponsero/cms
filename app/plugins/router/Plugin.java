package plugins.router;

import play.PlayPlugin;
import play.Play;
import play.classloading.ApplicationClasses;
import play.mvc.Router;
import play.utils.Java;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import java.util.Map;
import play.vfs.VirtualFile;

public class Plugin extends PlayPlugin {

    @Override
    public void detectChange() {
        
        //computeRoutes();
    }

    @Override
    public void onConfigurationRead() {
        
        //computeRoutes();
    }

    @Override
    public void onRoutesLoaded() {
        
        //computeRoutes();
    }

    @Override
    public void onApplicationStart() {
        
        computeRoutes();
    }

    protected void computeRoutes() {

        List<Class> controllerClasses = getControllerClasses();
        Map<String, VirtualFile> modules = Play.modules;

        List<Method> methods = Java.findAllAnnotatedMethods(controllerClasses, Route.class);

        for (Method method : methods) {

            Route annotation = method.getAnnotation(Route.class);
            if (annotation != null) {

                Class  controller       = method.getDeclaringClass();
                Route  controllerRoute  = (Route) controller.getAnnotation(Route.class);
                
//                String className    = controller.getName();
//                String moduleName   = "";
//
//                for (String name : modules.keySet()) {
//
//                    if (className.startsWith("controllers." + name)) {
//
//                        moduleName = name +".";
//                        break;
//                    }
//                }
                
                
                String uri      = annotation.value();
                if (controllerRoute != null) {
                    
                    String controller_uri = controllerRoute.value();
                    if (!controller_uri.endsWith("/") && !uri.isEmpty()){
                        
                        controller_uri += "/";
                    }
                    
                    uri = controller_uri + uri;
                }
                
                
                String target = controller.getName().replace("controllers.", "") + "." + method.getName();

 
                if (annotation.priority() != -1) {
                    Router.addRoute(annotation.priority(), annotation.method(), uri, target, getFormat(annotation.format()), annotation.accept());
                } else {
                    Router.prependRoute(annotation.method(), uri, target, getFormat(annotation.format()), annotation.accept());
                }
            }
        }
    }

    public List<Class> getControllerClasses() {
        List<Class> returnValues = new ArrayList<Class>();
        List<ApplicationClasses.ApplicationClass> classes = Play.classes.all();
        for (ApplicationClasses.ApplicationClass clazz : classes) {
            if (clazz.name.startsWith("controllers.")) {
                if (clazz.javaClass != null && !clazz.javaClass.isInterface() && !clazz.javaClass.isAnnotation()) {
                    returnValues.add(clazz.javaClass);
                }
            }
        }
        return returnValues;
    }

    private String getFormat(String format) {
        if (format == null || format.length() < 1) {
            return null;
        }
        return "(format:'" + format + "')";
    }
}