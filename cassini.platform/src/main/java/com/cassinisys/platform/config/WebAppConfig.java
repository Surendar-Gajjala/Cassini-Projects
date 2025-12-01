package com.cassinisys.platform.config;

import com.cassinisys.platform.model.plugin.Plugin;
import com.cassinisys.platform.security.PropertiesLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by reddy on 12/8/14.
 */

public abstract class WebAppConfig implements WebApplicationInitializer {
    public static long systemStartupTime;
    private Map<String, File> loadedJars = new HashMap<>();

    protected abstract Class getConfigClass();

    public void onStartup(ServletContext servletContext) throws ServletException {
        scanMainWebappJars(servletContext.getRealPath("/"));

        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        systemStartupTime = runtimeBean.getStartTime();

        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(getConfigClass());

        initPlugins(ctx);

        loadEntityAndEnumProperties(servletContext);

        ctx.setServletContext(servletContext);
        servletContext.addListener(new ContextLoaderListener(ctx));

        ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher", new DispatcherServlet(ctx));
        servlet.addMapping("/api/*");
    }

    private void scanMainWebappJars(String path) {
        File libDir = new File(new File(path), "WEB-INF" + File.separator + "lib");
        if(libDir.exists()) {
            File[] children = libDir.listFiles();
            if (children != null) {
                for (File child : children) {
                    if (child.getName().toLowerCase().endsWith(".jar")) {
                        loadedJars.put(child.getName(), child);
                    }
                }
            }
        }
    }

    private void initPlugins(AnnotationConfigWebApplicationContext ctx) {
        try {
            Resource resource = new ClassPathResource("/application.properties");
            Properties props = PropertiesLoaderUtils.loadProperties(resource);
            String pluginsPath = props.getProperty("cassini.plugins.dir");
            if(pluginsPath != null) {
                File pluginsDir = new File(pluginsPath);
                if (pluginsDir.exists()) {
                    File[] children = pluginsDir.listFiles();
                    if (children != null) {
                        for (File child : children) {
                            addPluginToClassPath(ctx, child);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addPluginToClassPath(AnnotationConfigWebApplicationContext ctx, File pluginDir) {
        try {
            ClassLoader cl = AnnotationConfigWebApplicationContext.class.getClassLoader();
            if (cl instanceof URLClassLoader) {
                Method m = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
                m.setAccessible(true);
                File classesDir = new File(pluginDir, "WEB-INF" + File.separator + "classes");
                File libDir = new File(pluginDir, "WEB-INF" + File.separator + "lib");

                if(libDir.exists()) {
                    File[] children = libDir.listFiles();
                    if (children != null) {
                        for (File child : children) {
                            if (child.getName().toLowerCase().endsWith(".jar")
                                    && loadedJars.get(child.getName()) == null) {
                                m.invoke(cl, (Object) child.toURI().toURL());
                                loadedJars.put(child.getName(), child);
                            }
                        }
                    }
                }

                if(classesDir.exists()) {
                    m.invoke(cl, (Object) classesDir.toURI().toURL());
                }

                loadPluginManifest(ctx, pluginDir);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void loadPluginManifest(AnnotationConfigWebApplicationContext ctx, File dir) {
        try {
            File manifestFile = new File(dir, "manifest.json");
            if(manifestFile.exists()) {
                ObjectMapper mapper = new ObjectMapper();
                Plugin plugin = mapper.readValue(manifestFile, Plugin.class);
                if(plugin != null) {
                    plugin.setDirectory(dir.getName());
                    String pluginClass = plugin.getPluginClass();
                    if(pluginClass != null && !pluginClass.trim().isEmpty()) {
                        try {
                            ctx.register(Class.forName(pluginClass.trim()));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadEntityAndEnumProperties(ServletContext servletContext){
        BufferedWriter bw = null;
        try  {
            String path = servletContext.getRealPath("/")+"entityProperties.json";
            File file = new File(path);
            if(!file.exists()) file.createNewFile();
            FileWriter fw = new FileWriter(file);
            bw = new BufferedWriter(fw);
            PropertiesLoader propertiesLoader = new PropertiesLoader();
            String list = propertiesLoader.getAllProperties(servletContext);
            bw.write(list);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                if(bw!=null)
                    bw.close();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }

    }

}
