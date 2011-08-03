package elfinder;

import com.google.gson.Gson;
import elfinder.ElfinderException._403;
import elfinder.ElfinderException._404;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilePermission;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import play.Logger;

/**
 * @author benoit
 */
public class Elfinder {
    
    public Elfinder.options opts;
    
    public static class options {
        
        public String       root            = "";                      // path to root directory
        public String       URL             = "";                      // root directory URL
        public String       rootAlias       = "Home";                  // display this instead of root directory name
        public List<String> disabled        = new ArrayList<String>(); // list of not allowed commands
        public boolean      dotFiles        = false;                   // display dot files
        public boolean      dirSize         = false;                   // count total directories sizes
        public String       fileMode        = "0666";                  // new files mode
        public String       dirMode         = "0777";                  // new folders mode
        public String       mimeDetect      = "auto";                  // files mimetypes detection method (finfo, mime_content_type, linux (file -ib), bsd (file -Ib), internal (by extensions))
        public List<String> uploadAllow     = new ArrayList<String>(); // mimetypes which allowed to upload
        public List<String> uploadDeny      = new ArrayList<String>(); // mimetypes which not allowed to upload
        public String       uploadOrder     = "deny,allow";            // order to proccess uploadAllow and uploadAllow options
        public String       imgLib          = "auto";                  // image manipulation library (imagick, mogrify, gd)
        public String       tmbDir          = ".tmb";                  // directory name for image thumbnails. Set to "" to avoid thumbnails generation
	public int          tmbCleanProb    = 1;                       // how frequiently clean thumbnails dir (0 - never, 200 - every init request)
	public int          tmbAtOnce       = 5;                       // number of thumbnails to generate per request
	public int          tmbSize         = 48;                      // images thumbnails size (px)
	public boolean      tmbCrop         = true;                    // crop thumbnails (true - crop, false - scale image to fit thumbnail size)
	public String       tmbBgColor      = "#ffffff";               // thumbnail background color
	public boolean      fileURL         = true;                    // display file URL in "get info"
	public String       dateFormat      = "dd-MM-yyyy HH:mm";      // file modification date format
	public Object       logger          = null;                    // object logger
	public Object       aclObj          = null;                    // acl object (not implemented yet)
	public String       aclRole         = "user";                  // role for acl
        public boolean      debug           = false;                   // send debug to client
        
        public Map<String, Boolean> perms       = new HashMap<String, Boolean>();   // individual folders/files permisions 
	public Map<String, Boolean> defaults    = new HashMap<String, Boolean>(){{  // default permisions
            put("read",  true);
            put("write", true);
            put("rm",    true);
        }};
        
	public Map<String, String> archiveMimes = new HashMap<String, String>();    // allowed archive's mimetypes to create. Leave empty for all available types.
	public Object archivers   = null;                                           // info about archivers to use. See example below. Leave empty for auto detect
    }
    
    public Map<String,String> _commands = new HashMap<String, String>(){{
        put("open"      , "_open");
        put("reload"    , "_reload");
        put("mkdir"     , "_mkdir");
        put("mkfile"    , "_mkfile");
        put("rename"    , "_rename");
        put("upload"    , "_upload");
        put("paste"     , "_paste");
        put("rm"        , "_rm");
        put("duplicate" , "_duplicate");
        put("read"      , "_fread");
        put("edit"      , "_edit");
        put("archive"   , "_archive");
        put("extract"   , "_extract");
        put("resize"    , "_resize");
        put("tmb"       , "_thumbnails");
        put("ping"      , "_ping");
    }};

    public static Map<String,String> _mimeTypes = new HashMap<String, String> (){{
        
        //applications
        put("ai"    , "application/postscript");
        put("eps"   , "application/postscript");
        put("exe"   , "application/octet-stream");
        put("doc"   , "application/vnd.ms-word");
        put("xls"   , "application/vnd.ms-excel");
        put("ppt"   , "application/vnd.ms-powerpoint");
        put("pps"   , "application/vnd.ms-powerpoint");
        put("pdf"   , "application/pdf");
        put("xml"   , "application/xml");
        put("odt"   , "application/vnd.oasis.opendocument.text");
        put("swf"   , "application/x-shockwave-flash");
        // archives
        put("gz"    , "application/x-gzip");
        put("tgz"   , "application/x-gzip");
        put("bz"    , "application/x-bzip2");
        put("bz2"   , "application/x-bzip2");
        put("tbz"   , "application/x-bzip2");
        put("zip"   , "application/zip");
        put("rar"   , "application/x-rar");
        put("tar"   , "application/x-tar");
        put("7z"    , "application/x-7z-compressed");
        // texts
        put("txt"   , "text/plain");
        put("php"   , "text/x-php");
        put("html"  , "text/html");
        put("htm"   , "text/html");
        put("js"    , "text/javascript");
        put("css"   , "text/css");
        put("rtf"   , "text/rtf");
        put("rtfd"  , "text/rtfd");
        put("py"    , "text/x-python");
        put("java"  , "text/x-java-source");
        put("rb"    , "text/x-ruby");
        put("sh"    , "text/x-shellscript");
        put("pl"    , "text/x-perl");
        put("sql"   , "text/x-sql");
        // images
        put("bmp"   , "image/x-ms-bmp");
        put("jpg"   , "image/jpeg");
        put("jpeg"  , "image/jpeg");
        put("gif"   , "image/gif");
        put("png"   , "image/png");
        put("tif"   , "image/tiff");
        put("tiff"  , "image/tiff");
        put("tga"   , "image/x-targa");
        put("psd"   , "image/vnd.adobe.photoshop");
        //audio
        put("mp3"   , "audio/mpeg");
        put("mid"   , "audio/midi");
        put("ogg"   , "audio/ogg");
        put("mp4a"  , "audio/mp4");
        put("wav"   , "audio/wav");
        put("wma"   , "audio/x-ms-wma");
        // video
        put("avi"   , "video/x-msvideo");
        put("dv"    , "video/x-dv");
        put("mp4"   , "video/mp4");
        put("mpeg"  , "video/mpeg");
        put("mpg"   , "video/mpeg");
        put("mov"   , "video/quicktime");
        put("wm"    , "video/x-ms-wmv");
        put("flv"   , "video/x-flv");
        put("mkv"   , "video/x-matroska");
        
    }};
    
    
    
    /**
     * Additional data about error
     **/
    public Object   _errorData;
    
    public String   _fakeRoot   = "";
    
    /**
     * Command result to send to client
     **/
    public Map<String, Object> _result = new HashMap<String, Object>();
    
    public long     _time      = 0;
    public long     _today     = 0; 
    public long     _yesterday = 0;

    
    public Elfinder() {
        
        __construct(new Elfinder.options());
    }

    public Elfinder(options options) {
        
        __construct(options);
    }
    
    private void __construct(options customOptions){
        
        if (customOptions == null){
            
            customOptions = new options();
        }
        
        opts = customOptions;
        
        if (opts.root.endsWith(File.separator)){
            
            opts.root = opts.root.substring(0, opts.root.length()-1);
        }
        
        Date now = new Date();
        _time     = (opts.debug) ? now.getTime() : 0;
        _fakeRoot = (opts.rootAlias == null || opts.rootAlias.isEmpty()) 
                ? opts.root 
                : new File(opts.root, opts.rootAlias).toString();
        
        if (!opts.disabled.isEmpty()){
            
            List<String> no = Arrays.asList("open", "reload", "tmb", "ping");
            Iterator<String> it = opts.disabled.iterator();
            while (it.hasNext()){
                
                String cmd = it.next();
                if (! _commands.containsKey(cmd) || no.contains(cmd)){
                    
                    it.remove();
                }
                else {
                    _commands.remove(cmd);
                }
            }
        }
        
        if (opts.tmbDir != null && !opts.tmbDir.isEmpty()){
            
            File tmbDir = new File(opts.root ,opts.tmbDir);
            try {
                if (!tmbDir.exists()){
                    
                    tmbDir.mkdirs();
                }
                
                opts.tmbDir = tmbDir.getAbsolutePath();
            }
            catch(Exception e){
                opts.tmbDir = "";
            }
        }
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR, 0);
        _today = cal.getTimeInMillis();
        
        cal.set(Calendar.DATE, -1);
        _yesterday = cal.getTimeInMillis();
        
    }
    
    /**
     * 
     * @param httpParams
     * @return Object File.class or String.class 
     * always test like this : 
     * 
     * Object o = elfinder.run();
     * if (o.getClass == File.class){
     *    File f = (File) o;
     *    // handle the file 
     * }
     * else {
     *    //put json in http response;
     * }
     */
    public Object run(Map<String,String> httpParams) throws  _404, _403, Exception{
        
        String  cmd  = httpParams.get("cmd");
        boolean init = (httpParams.get("init") != null);
        
        
        if (opts.root == null || opts.root.isEmpty() || !is_dir(opts.root)){
            
            return "{error:'Invalid backend configuration'}";
        }
        if (!_isAllowed(opts.root, "read")){
            
            return "{error:'Access denied'}";
        }
        if (cmd == null){
            
            return "{error:'Command not specified'}";
        }
        
        cmd = cmd.trim();
        if (!_commands.containsKey(cmd)){
            
            return "{error:'Unknown command'}";
        }
        
        if (init){
            
            long ts = new Date().getTime();
            
            _result.put("disabled", opts.disabled);
            
            Map<String,Object> params = new HashMap<String, Object>();
            params.put("dotFiles", opts.dotFiles);
            params.put("uplMaxSize", 0);
            
            if (_commands.containsKey("archive") || _commands.containsKey("extract")){
                
                //checkArchivers();
                String[] archives = {""};
                if (_commands.containsKey("archive")){
                    
                    params.put("archives", archives);
                }
                if (_commands.containsKey("extract")){
                    
                }
            }
            
            if (opts.tmbDir != null && !opts.tmbDir.isEmpty()){
                
                if (Math.random() * 200 <= opts.tmbCleanProb){
                    
                    File tmbDir = new File(opts.tmbDir);
                    for (File f : tmbDir.listFiles()){
                        f.delete();
                    }
                }
            }
            
            _result.put("params", params);
        }
        
        if (opts.debug){
            
            Map<String,Object> debug = new HashMap<String, Object>();
            
            debug.put("time", new Date().getTime() - _time);
            debug.put("mimeDetect" , opts.mimeDetect);
            debug.put("imgLib"     , opts.imgLib);
            
            if (opts.dirSize){
                
                debug.put("dirSize", true);
                debug.put("du", "?");
            }
            
            _result.put("debug", debug);
        }
        
        Object o = null;
        
        try {
            
            Method m = this.getClass().getMethod(_commands.get(cmd), Map.class);
            o = m.invoke(this, httpParams);
        }
        catch(Exception ex){
            
            Class[] exceptions = {NoSuchMethodException.class, SecurityException.class, 
                                  IllegalAccessException.class, IllegalArgumentException.class,
                                  IllegalArgumentException.class
                                };
            
            Class klass = ex.getClass();
            
            if (Arrays.asList(exceptions).contains(klass)){
                return "{error:'not yet implemented'}";
            }
            
            throw ex;
        }
        
        if (o != null){
            
            return o;
        }
        
        
        Gson gson = new Gson();
        return gson.toJson(_result);
    }
    
    // elfinder commands
    
    public File _open(Map<String,String> httpParams) throws IOException, _404, _403 {
        
        String current = trim(httpParams.get("current"));
        String target  = trim(httpParams.get("target"));
        
        if (current != null){
            
            String dir    = _findDir(trim(current), null);
            String file   = _find(trim(target), dir);
            
            if (current.isEmpty()
                || target.isEmpty()
                || dir == null
                || file == null
                || is_dir(file)){
                
                throw new ElfinderException._404("File not found");
            }
            
            if (! _isAllowed(dir, "read") || _isAllowed(file, "read")){
                
                throw new ElfinderException._403("Access denied");
            }
            
            if (isSymbolicLink(file)){
                
                File f = _readlink(file); 
                
                if (f == null || f.isDirectory()){
                    
                    throw new ElfinderException._404("File not found");
                }
                if (! _isAllowed(f.getParentFile().getAbsolutePath(), "read") || _isAllowed(f.getAbsolutePath(), "read")){
                
                    throw new ElfinderException._403("Access denied");
                }
            }
            
            return new File(file);
        }
        else {
            
            String path = opts.root;
            String init = httpParams.get("init");
            
            if (target != null && !target.isEmpty()){
                
                String p = _findDir(target, null);
                if (p == null){
                    
                    if (init == null){ 
                        _result.put("error", "Invalid parameters");
                    }
                    
                }
                else if (!_isAllowed(p, "read")){
                    
                    if (init == null){ 
                        _result.put("error", "Access denied");
                    }
                }
                else {
                    path = p;
                }
            }
            
            _content(path, (httpParams.get("tree") != null));
        }
        
        return null;
    }
    
    private void _rename(Map<String,String> httpParams) throws IOException{
        
        String current = trim(httpParams.get("current"));
        String target  = trim(httpParams.get("target"));
            
        String dir    = _findDir(trim(current));
        String file   = _find(trim(target), dir);

        String name   = _checkName(httpParams.get("name"));

        if (current == null || current.isEmpty()
            || target == null || target.isEmpty()
            || dir == null
            || file == null){

            _result.put("error", "File not found");
        }
        else if (name == null){
            _result.put("error", "Invalid name");
        }
        else if (!_isAllowed(dir, "write")){
            _result.put("error", "Access denied");
        }
        else if (new File(dir, name).exists()){
            _result.put("error", "File or folder with the same name already exists");
        }
        else {

            File targ = new File(dir, file);
            File dest = new File(dir, name);

            if (! targ.renameTo(dest)){
                _result.put("error", "Unable to rename file");
            }
            else {
                _rmTmb(target);
                List<String> tmp = new ArrayList<String>();
                tmp.add(_hash(dir + File.separator + name));
                _result.put("select", tmp);
                _content(dir, dest.isDirectory());
            }
        }
    }
    
    
    private void _mkdir(Map<String,String> httpParams) throws IOException{
        
        String current = trim(httpParams.get("current"));
        String dir     = _findDir(trim(current));
        String name    = _checkName(httpParams.get("name"));
        
        File dest = new File(dir, name);
        
        
        if (current == null || current.isEmpty()
            || dir == null){
            _result.put("error", "Invalid parameters");
        }
        else if (!_isAllowed(dir, "write")){
            _result.put("error", "Access denied");
        }
        else if (dest.exists()){
            _result.put("error", "File or folder with the same name already exists");
        }
        else if (!dest.mkdirs()){
            _result.put("error", "Unable to create folder");
        }
        else {
            _chmod(dest, opts.dirMode);
            List<String> tmp = new ArrayList<String>();
            tmp.add(_hash(dir + File.separator + name));
                
            _result.put("select", tmp);
            _content(dir, true);
        }
    }
    
    private void _chmod(File file, String mod){
        
        if (file.isDirectory()){
            file.setReadable(true);
            file.setWritable(true);
            file.setExecutable(true);
        }
        else {
            file.setReadable(true);
            file.setWritable(true);
            file.setExecutable(false);
        }
    }
    
    private String _findDir(String hash) throws IOException {
        
        return _findDir(hash, null);
    }


    private String _findDir(String hash, String path) throws IOException {
        
        if (path == null){
            
            path = opts.root;
            
            if (_hash(path).equals(hash)){
                
                return path;
            }
        }
        
        File dir = new File(path);
        for (File f : dir.listFiles()){
            
            String p = path + File.separator + f.getName();
            
            if (isSymbolicLink(f)){
                //_readlink(f);
            }
            else if (_isAccepted(f.getName()) && f.isDirectory() ){
                
                if (_hash(p).equals(hash)){
                    
                    return p;
                }
                
                p = _findDir(hash, p);
                if (null != p){
                    
                    return p;
                }
            }
        }
        
        return null;
    }
    
    private String _find(String hash, String path){
        
        File dir = new File(path);
        for (File f : dir.listFiles()){
            
            if (_isAccepted(f.getName())){
                
                String p = path + File.separator + f.getName();
                if (_hash(p).equals(hash)){
                    return p;
                }
            }
        }
        
        return null;
    }
    
    private void _rmTmb(String path){
        
        File img = new File(_tmbPath(path));
        
        if(img.exists()){
            
            img.delete();
        }
    }
    
    private String _checkName(String path){
        
        return null;
    }

    private File _readlink(String path){
        
        return null;
    }
    
    private String _hash(String path){
        //return path;
        return DigestUtils.md5Hex(path);
    }
    
    private boolean is_dir(String path){
        
        try {
            File f = new File(path);
            boolean exist = f.exists();
            return exist;
        } catch (Exception ex){}
        
        return false;
    }
    
    private boolean _isAllowed(String path, String action){
        
        File f = new File(path);
        
        if ("read".equals(action)
               && !f.canRead()){
            
            return false;
        }
        else if ("write".equals(action)
                && !f.canWrite()){
            
            return false;
        }
        else if ("rm".equals(action)
                && !f.getParentFile().canWrite()){
            
            return false;
        }
        
        return true;
    }
    
    private boolean _isAccepted(String filename){
        
        if (!opts.dotFiles && filename.startsWith(".")){
            
            return false;
        }
        
        return true;
    }
    
    private void _content(String path, boolean tree) throws IOException{
        
        _cwd(path);
        _cdc(path);
        
        if (tree){
            
            _result.put("tree", _tree(opts.root));
        }
    }
    
    private void _cwd(String path){
        
        String name = "";
        String rel = (opts.rootAlias != null) ? opts.rootAlias : basename(path);
        if (path.equals(opts.root)){
            
            name = rel;    
        }
        else {
            name = basename(path);
            rel += File.separator + path.substring(path.indexOf(opts.root)+1);
        }
        
        Map<String,Object> cwd = new HashMap<String, Object>();
        cwd.put("hash", _hash(path));
        cwd.put("name", name);
        cwd.put("mime", "directory");
        cwd.put("rel",  rel);
        cwd.put("size", 0);
        cwd.put("date", lastModified(path, opts.dateFormat));
        cwd.put("read", true);
        cwd.put("write", _isAllowed(path, "write"));
        cwd.put("rm",   (path.equals(opts.root))?false : _isAllowed(path, "rm"));
        
        _result.put("cwd", cwd);
    }
    
    private void _cdc(String path) throws IOException {
        
        //List<Map<String,Object>> dirs  = new ArrayList<Map<String, Object>>();
        List<Map<String,Object>> files = new ArrayList<Map<String, Object>>();
        
        File dir = new File(path);
        for (File file : dir.listFiles()){
            
            Map<String,Object> info;
            if (_isAccepted(file.getName())){
                
                info = _info(path + File.separator + file.getName());
                
                // -> wtf ?
                //if (info.get("mime").equals("directory")){
                //    dirs.add(info);
                //}
                //else {
                    files.add(info);
                //}
            }
        }
        
        _result.put("cdc", files);
    }
    
    private Map<String,Object> _info(String path) throws IOException{
        
        String symPath = null;
        
        File f = new File (path);
        long lastmodified = f.lastModified();
        
        String d = "";
        if (lastmodified > _today){
            d = "Today " + formatDate(lastmodified, "HH:mm");
        }
        else if (lastmodified > _yesterday){
            d = "Yesterday " + formatDate(lastmodified, "HH:mm");
        }
        else {
            d = formatDate(lastmodified, opts.dateFormat);
        }
        
        Map<String,Object> info = new HashMap<String, Object>();
        
        info.put("name", htmlspecialcars(f.getName()));
        info.put("hash", _hash(path));
        info.put("mime", f.isDirectory() ? "directory" : _mimetype(path));
        info.put("data", d);
        info.put("size", f.isDirectory() ? _dirSize(path) : f.getTotalSpace());
        info.put("read", _isAllowed(path, "read"));
        info.put("write",_isAllowed(path, "write"));
        info.put("rm",   _isAllowed(path, "rm"));
        
        
        if (isSymbolicLink(f)){
            
            File symLink = _readlink(path);
            
            if (symLink == null){
                info.put("mime", "symlink-broken");
                return info;
            }
            symPath = symLink.getAbsolutePath();
            
            if (symLink.isDirectory()){
                
                info.put("mime", _hash(symLink.getName()));
            } else {
                info.put("parent", "directory");
                //info.put("mime", $this->_mimetype($lpath);
            }
            info.put("link",   _hash(path));
            info.put("linkTo", (opts.rootAlias != null && !opts.rootAlias.isEmpty()) ? opts.rootAlias : basename(opts.root) + symPath.substring(opts.root.length()));
            info.put("read", _isAllowed(symPath, "read"));
            info.put("write",_isAllowed(symPath, "write"));
            info.put("rm",   _isAllowed(symPath, "rm"));
        }
        
        String mime = (String) info.get("mime");
        if (! "directory".equals(mime)){
            
            Boolean read = (Boolean) info.get("read");
            
            if (opts.fileURL && read){
                info.put("url", _path2url((symPath != null) ? symPath : path));
            }
            
            if (mime.startsWith("image")){
                
                Integer[] size = getimagesize(path);   
                
                if (size != null){
                    info.put("dim", size[0] +"x"+size[1]);
                }
                
                if (read){
                    info.put("resize",  size != null && _canCreateTmb(mime) );
                    
                    String tmb = _tmbPath(path);
                    
                    if (new File(tmb).exists()){
                        info.put("tmb", _path2url(tmb) );
                    } else if (size != null) {
                        _result.put("tmb", true);
                    }
                }
            }
        }
        
        return info;
    }
    
    private boolean _canCreateTmb(String mime){
        
        if (opts.tmbDir.startsWith("image")){
            
            return ("image/jpeg".equals(mime) || "image/png".equals(mime) || "image/gif".equals(mime));
        }
        return false;
    }
    
    private String _tmbPath(String path){
        
        String tmb = "";
        if (!opts.tmbDir.isEmpty()){
            
            File f = new File(path);
            tmb = (f.getName().equals(opts.tmbDir)) 
                    ? opts.tmbDir + File.separator + _hash(path) + ".png"
                    : path;
        }
        
        return tmb;
    }
    
    private Map<String, Object> _tree(String path) throws IOException{
        
        Map<String, Object> tree = new HashMap<String, Object>();
        
        tree.put("hash", _hash(path));
        tree.put("name", (path.equals(opts.root) && !opts.rootAlias.isEmpty()) ? opts.rootAlias : basename(path));
        tree.put("read", _isAllowed(path, "read"));
        tree.put("write",_isAllowed(path, "write"));
        
        List<Map<String, Object>> dirs = new ArrayList<Map<String, Object>>();
        tree.put("dirs", dirs);
        
        File dir = new File(path);
        for (File file : dir.listFiles()){
            
            String p = path + File.separator + file.getName();

            if (_isAccepted(file.getName()) && file.isDirectory() && !isSymbolicLink(file)){
                
                dirs.add(_tree(p));
            }
        }
        
        return tree;
    }
    
    private long _dirSize(String path){
        
        return FileUtils.sizeOfDirectory(new File(path));
    }
    
    private String _mimetype(String path){
        
        return new MimetypesFileTypeMap().getContentType(path);
    }
    
    private String _path2url(String path) throws UnsupportedEncodingException {
        
        File f = new File (path);

        String parent = f.getParent();
        
        String dir  = (f.getAbsolutePath().substring(opts.root.length()+1));
        String file = URLEncoder.encode(f.getName(), "UTF-8");
        
        return opts.URL + (!dir.isEmpty() ? dir.replace(File.separator, "/")+"/" : "") + file;
     }
    
    private String htmlspecialcars (String content){
        
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<content.length(); i++) {
          char c = content.charAt(i);

          switch (c) {
            case '<' : 
              sb.append("&lt;");
              break;
            case '>' : 
              sb.append("&gt;");
              break;

            case '&' :
              sb.append("&amp;");
              break;
            case '"' :
              sb.append("&quot;");
              break;
            case '\'' :

              sb.append("&apos;");
              break;
            default:
              sb.append(c);
            }
        }

        return sb.toString();
    }
    
    private String basename(String path){
        
        int pos = path.lastIndexOf("/");
        
        return path.substring(pos+1);
    }
    
    private String lastModified(String path, String pattern){
        
        File dir = new File(path);
        if (!dir.exists()){
            return "";
        }
        
        long time = dir.lastModified();
        
        return formatDate(time, pattern);
    }
    
    private String formatDate(long time, String pattern){
        
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date(time));
    }
    
    private Integer[] getimagesize(String path){
        
        Integer[] size = {0,0};
        
        try {
            BufferedImage image = ImageIO.read(new File(path));
            
            size[0] = image.getWidth();
            size[1] = image.getHeight();
        }
        catch(Exception ex) {
            
            return null;
        }
        
        return size;
    }
    
    public boolean isSymbolicLink(File file) throws IOException {
        return isSymbolicLink(file.getParentFile(), file.getName());
    }
    
    public boolean isSymbolicLink(String name) throws IOException {
        return isSymbolicLink(new File(name));
    }
    
    private boolean isSymbolicLink(File parent, String name) throws IOException {
        
        File toTest = parent != null
            ? new File(parent.getCanonicalPath(), name)
            : new File(name);
        return !toTest.getAbsolutePath().equals(toTest.getCanonicalPath());
    }
    
    private String trim(String string){
        
        if (string != null){
            
            return string.trim();
        }
        
        return null;
    }
}
