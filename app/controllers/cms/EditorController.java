package controllers.cms;

import play.mvc.Controller;

/**
 * @author benoit
 */
//@WebServlet("/--editor/*")
public class EditorController extends Controller {

//    public static final String TMP_LOCATION = "/tmp/nemo-upload";
//
//    private static final String TYPE_UNKNOWN = "UNKNOWN";
//    private static final String TYPE_IMAGE = "IMAGE";
//
//    @Route("save")
//    public void save(String path, String lang) throws IOException {
//
//        String contextPath = request.getContextPath();
//        String language    = request.getParameter("language");
//        String path        = request.getParameter("path");
//        int    i           = 0;
//
//        while (true) {
//            
//            String code     = request.getParameter("editors[" + i + "][code]");
//            String content  = request.getParameter("editors[" + i + "][content]");
//            String isStatic = request.getParameter("editors[" + i + "][isStatic]");
//
//            i++;
//
//            if (code == null) {
//                break;
//            }
//
//            String currentPath = ("1".equals(isStatic)) ? null : path;
//            Editor editor      = editorFacade.findByPathAndCodeAndLanguageAndContextPath(currentPath, code, language, contextPath);
//
//            if (editor == null) {
//
//                editor = new Editor();
//
//                editor.setPath(currentPath);
//                editor.setCode(code);
//                editor.setLanguage(language);
//                editor.setContextPath(contextPath);
//            }
//
//            editor.setContent(content);
//            editorFacade.save(editor);
//        }
//
//        PrintWriter writer = response.getWriter();
//
//        writer.print("OK");
//        writer.flush();
//        writer.close();
//    }
//
//    @Action("browser")
//    public View browser(HttpServletRequest request, HttpServletResponse response) {
//
//        String type = request.getParameter("type");
//
//        request.setAttribute("type", type);
//
//        return View.go("/nemo/browser.xhtml");
//    }
//
//    @Action("listFolders")
//    public void listFolders(HttpServletRequest request, HttpServletResponse response) throws Exception {
//
//        String rootPath = getAbsoluteRoot(request);
//        String path = request.getParameter("parent");
//        boolean isRoot = (path == null);
//        if (isRoot) {
//            path = "";
//        }
//
//        File root = new File(rootPath, path);
//        if (!root.exists()) {
//            root.mkdirs();
//        }
//
//        File tmpRoot = root;
//        boolean isReadOnly = false;
//
//        while (!isReadOnly && !tmpRoot.getAbsolutePath().equals(getAbsoluteRoot(request))) {
//            isReadOnly = isPathReadOnly(tmpRoot);
//            tmpRoot = tmpRoot.getParentFile();
//        }
//
//
//        File[] files;
//        if (!isRoot) {
//            files = root.listFiles(new FileFilter() {
//
//                public boolean accept(File file) {
//                    return file.isDirectory() && !file.isHidden();
//                }
//            });
//        } else {
//            files = new File[1];
//            files[0] = root;
//        }
//
//        PrintWriter writer = response.getWriter();
//
//        if (!isRoot) {
//            writer.print("<ul>");
//        }
//
//        for (File file : files) {
//
//            String klass = (isPathReadOnly(file) || isReadOnly) ? " class=\"readonly\"" : "";
//
//            writer.print("<li" + klass + "><a href=\"#\" " + ((isRoot) ? "data-nodelete=\"true\" data-noedit=\"true\"" : "")  +" data-path=\"" + ((isRoot) ? "" : file.getPath().replace(rootPath, "")) + "\">" + file.getName() + "</a></li>");
//        }
//
//        if (!isRoot) {
//            writer.print("</ul>");
//        }
//
//        writer.flush();
//        writer.close();
//    }
//
//    @Action("listFiles")
//    public void listFiles(HttpServletRequest request, HttpServletResponse response) throws Exception {
//
//        String rootPath = getAbsoluteRoot(request);
//
//        String requestedType = request.getParameter("type");
//        String folderPath = request.getParameter("path");
//
//        folderPath = rootPath + folderPath;
//
//        File folder = new File(folderPath);
//        File[] files = folder.listFiles(new FileFilter() {
//
//            public boolean accept(File file) {
//                return !file.isDirectory() && !file.isHidden();
//            }
//        });
//        
//        if (files == null){
//            return;
//        }
//
//        PrintWriter writer = response.getWriter();
//        String root = getRoot(request);
//        int count = 0;
//        for (File file : files) {
//
//            String type = detectFileType(file);
//            String name = file.getName();
//            String path = file.getPath().replace(rootPath, "");
//            String src = root + path;
//
//            boolean skip = ("image".equals(requestedType) && !type.equals(TYPE_IMAGE));
//
//            if (skip) {
//                continue;
//            }
//
//            if ((count++ % 4) == 0) {
//                writer.print("<hr />");
//            }
//
//            if (type.equals(TYPE_IMAGE)) {
//
//                writer.print("<div><img data-path=\"" + path + "\" src=\"" + src + "\" /><span>" + name + "</span></div>");
//            } else {
//                String contextPath = request.getContextPath();
//                writer.print("<div><img data-path=\"" + path + "\"  src=\"" + contextPath + "/nemo/img/file.png\" /><span>" + name + "</span></div>");
//            }
//        }
//
//        writer.flush();
//        writer.close();
//    }
//
//    @Action("newFolder")
//    public String newFolder(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String rootPath = getAbsoluteRoot(request);
//
//        String folderName = request.getParameter("folderName");
//        String path       = request.getParameter("path");
//
//        String currentPath = rootPath + path;
//
//        File parentFolder = new File(currentPath);
//        File newFolder = new File(parentFolder, folderName);
//
//        newFolder.mkdir();
//
//        return path + "/" + folderName;
//    }
//
//    @Action("renameFolder")
//    public String renameFolder(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String rootPath = getAbsoluteRoot(request);
//
//        String folderName = request.getParameter("folderName");
//        String path       = request.getParameter("path");
//
//        String currentPath = rootPath + path;
//
//        File folder = new File(currentPath);
//        File parentFolder = folder.getParentFile();
//        File renamedFolder = new File(parentFolder.getAbsolutePath(), folderName);
//
//        folder.renameTo(renamedFolder);
//
//        
//        String newpath = path.substring(0, path.lastIndexOf("/")) + "/"+ folderName;
//        
//        return newpath;
//    }
//
//    @Action("renameFile")
//    public void renameFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
//        String rootPath = getAbsoluteRoot(request);
//        String fileName = request.getParameter("fileName");
//        String currentPath = request.getParameter("path");
//
//        currentPath = rootPath + currentPath;
//
//        File f = new File(currentPath);
//        File parentFolder = f.getParentFile();
//        File renamedFile = new File(parentFolder.getAbsolutePath(), fileName);
//
//        f.renameTo(renamedFile);
//
//        PrintWriter writer = response.getWriter();
//
//        writer.print("OK");
//        writer.flush();
//        writer.close();
//    }
//
//    @Action("upload")
//    public void upload(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//
//        File tmp     = new File(TMP_LOCATION);
//        if (!tmp.exists()){
//            tmp.mkdirs();
//        }
//
//
//        String rootPath = getAbsoluteRoot(request);
//
//        PrintWriter writer = null;
//        try {
//            writer = response.getWriter();
//        } catch (IOException ex) {
//            Logger.getLogger(EditorController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//
//        if (request.getContentType() != null &&
//                request.getContentType().toLowerCase().indexOf("multipart/form-data") > -1 )
//        {
//
//            MultipartRequest multipartRequest = new MultipartRequest(request);
//
//            String currentPath  = multipartRequest.getParam("currentPath");
//            if (currentPath == null){
//                currentPath = "";
//            }
//            currentPath = rootPath + currentPath;
//
//            File folder = new File(currentPath);
//            if (!folder.exists()) {
//                folder.mkdirs();
//            }
//
//            Part part = multipartRequest.getFilePart("qqfile");
//            if (part != null){
//
//                String filename = multipartRequest.getFileName(part);
//                
//                File target  = new File(folder, filename);
//
//                multipartRequest.writePartToFile(part, TMP_LOCATION, target);
//
//                writer.print("{success: true}");
//            }
//            else {
//                writer.print("{success: false}");
//            }
//        }
//        else {
//            
//            
//
//            String currentPath  = request.getParameter("currentPath");
//            String filename     = request.getHeader("X-File-Name");
//            currentPath = rootPath + currentPath;
//
//
//            InputStream is = null;
//            FileOutputStream fos = null;
//
//            File folder = new File(currentPath);
//            if (!folder.exists()) {
//                folder.mkdirs();
//            }
//
//            try {
//                filename = URLDecoder.decode(filename, "UTF-8");
//            } catch (UnsupportedEncodingException ex) {
//            }
//
//            try {
//                File target = new File(folder, filename);
//
//                is  = request.getInputStream();
//                fos = new FileOutputStream(target);
//
//                FileUtil.copyStream(is, fos);
//                response.setStatus(response.SC_OK);
//                writer.print("{success: true}");
//            } catch (Exception ex) {
//                response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
//                writer.print("{success: false}");
//                Logger.getLogger(EditorController.class.getName()).log(Level.SEVERE, null, ex);
//            } finally {
//                try {
//                    if (fos != null) {
//                        fos.close();
//                    }
//                    if (is != null) {
//                        is.close();
//                    }
//                } catch (IOException ex) {
//                }
//            }
//
//
//        }
//
//        writer.flush();
//        writer.close();
//    }
//
//    @Action("removeFile")
//    public void removeFile(HttpServletRequest request, HttpServletResponse response) {
//        String rootPath = getAbsoluteRoot(request);
//        PrintWriter writer = null;
//        InputStream is = null;
//        FileOutputStream fos = null;
//        try {
//            writer = response.getWriter();
//        } catch (IOException ex) {
//            Logger.getLogger(EditorController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        String[] files = request.getParameterValues("file[]");
//        try {
//            if (files != null) {
//                for (String file : files) {
//                    File f = new File(rootPath, file);
//                    f.delete();
//                }
//            }
//            response.setStatus(response.SC_OK);
//            writer.print("{success: true}");
//        } catch (Exception ex) {
//            response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
//            writer.print("{success: false}");
//            Logger.getLogger(EditorController.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//            try {
//                if (fos != null) {
//                    fos.close();
//                }
//                if (is != null) {
//                    is.close();
//                }
//            } catch (IOException ex) {
//            }
//        }
//
//        writer.flush();
//        writer.close();
//    }
//
//    @Action("removeFolder")
//    public void removeFolder(HttpServletRequest request, HttpServletResponse response) {
//        String rootPath = getAbsoluteRoot(request);
//        String folderPath = rootPath + request.getParameter("path");
//
//        PrintWriter writer = null;
//        InputStream is = null;
//        FileOutputStream fos = null;
//        try {
//            writer = response.getWriter();
//        } catch (IOException ex) {
//            Logger.getLogger(EditorController.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        try {
//            File folder = new File(folderPath);
//            deleteDir(folder);
//            response.setStatus(response.SC_OK);
//            writer.print("{success: true}");
//        } catch (Exception ex) {
//            response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
//            writer.print("{success: false}");
//            Logger.getLogger(EditorController.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//            try {
//                if (fos != null) {
//                    fos.close();
//                }
//                if (is != null) {
//                    is.close();
//                }
//            } catch (IOException ex) {
//            }
//        }
//
//        writer.flush();
//        writer.close();
//
//    }
//
//    public boolean deleteDir(File dir) {
//        if (dir.isDirectory()) {
//            String[] children = dir.list();
//            for (int i = 0; i < children.length; i++) {
//                boolean success = deleteDir(new File(dir, children[i]));
//                if (!success) {
//                    return false;
//                }
//            }
//        }
//
//        // The directory is now empty so delete it
//        return dir.delete();
//    }
//
//    private String getRoot(HttpServletRequest request) {
//
//        String contextPath = request.getContextPath();
//        return contextPath + "-files";
//    }
//
//    private String getAbsoluteRoot(HttpServletRequest request) {
//
//        String root = getRoot(request);
//        ServletContext servletContext = getServletContext();
//        ServletContext uploadContext = servletContext.getContext("/");
//
//        return uploadContext.getRealPath(root);
//    }
//
//    private String detectFileType(File file) {
//
//        String name = file.getName();
//        int sep = name.lastIndexOf(".");
//
//        if (sep == -1) {
//            return TYPE_UNKNOWN;
//        }
//
//        String ext = name.substring(sep + 1, name.length());
//        ext = ext.toLowerCase();
//
//        if (ext.equals("png") || ext.equals("jpg") || ext.equals("jpeg") || ext.equals("gif")) {
//            return TYPE_IMAGE;
//        }
//
//        return TYPE_UNKNOWN;
//    }
//
//    private boolean isPathReadOnly(File path) {
//        File tmp = new File(path.getAbsolutePath(), ".readonly");
//        return (tmp.exists());
//    }

}