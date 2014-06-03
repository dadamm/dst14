package dst.ass3.aop.impl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import dst.ass3.aop.IPluginExecutable;
import dst.ass3.aop.IPluginExecutor;

public class PluginExecutor implements IPluginExecutor {
	
	private ExecutorService executorService;
	private FileAlterationMonitor dirMonitor;
	private DirectoryListener directoryListener;
	
	public PluginExecutor() {
		this.executorService = Executors.newCachedThreadPool();
		this.dirMonitor = new FileAlterationMonitor();
		this.directoryListener = new DirectoryListener(this);
	}

	@Override
	public void monitor(File dir) {
		FileAlterationObserver observer = new FileAlterationObserver(dir);
		observer.addListener(directoryListener);
		dirMonitor.addObserver(observer);
	}

	@Override
	public void stopMonitoring(File dir) {
		for(FileAlterationObserver observer : dirMonitor.getObservers()) {
			if(observer.getDirectory().equals(dir)) {
				dirMonitor.removeObserver(observer);
			}
		}
	}

	@Override
	public void start() {
		try {
			dirMonitor.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void stop() {
		try {
			dirMonitor.stop();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void executeClass(final Class<?> clazz) {
		executorService.execute(new Runnable() {
			
			@Override
			public void run() {
				try {
					IPluginExecutable plugin = (IPluginExecutable) clazz.newInstance();
					plugin.execute();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
	protected synchronized void executeJar(File jar) {
		try {
			URLClassLoader classLoader = new URLClassLoader(new URL[] {new URL("jar:file:" + jar.getCanonicalPath() + "!/")});
			JarFile jarFile = new JarFile(jar);
			Enumeration<JarEntry> jarEntries = jarFile.entries();
			
			while (jarEntries.hasMoreElements()) {
				JarEntry jarEntry = jarEntries.nextElement();
				if(!jarEntry.isDirectory() && jarEntry.getName().endsWith(".class")) {
					String classname = jarEntry.getName().replace(".class", "").replace("/", ".");
					Class<?> clazz = classLoader.loadClass(classname);
					if(IPluginExecutable.class.isAssignableFrom(clazz)) {
						executeClass(clazz);
					}
				}
			}
			
			jarFile.close();
			classLoader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
