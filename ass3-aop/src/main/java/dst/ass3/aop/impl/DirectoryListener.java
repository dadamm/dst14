package dst.ass3.aop.impl;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;

public class DirectoryListener implements FileAlterationListener {
	
	private PluginExecutor pluginExecutor;
	
	public DirectoryListener(PluginExecutor pluginExecutor) {
		this.pluginExecutor = pluginExecutor;
	}

	@Override
	public void onStart(FileAlterationObserver observer) {
//		for(File file : observer.getDirectory().listFiles()) {
//			if(file.isFile() && file.getName().endsWith("jar")) {
//				pluginExecutor.executeJar(file);
//			}
//		}
	}

	@Override
	public void onDirectoryCreate(File directory) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDirectoryChange(File directory) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDirectoryDelete(File directory) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFileCreate(File file) {
		if(file.getName().endsWith("jar")) {
			pluginExecutor.executeJar(file);
		}
		
	}

	@Override
	public void onFileChange(File file) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFileDelete(File file) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStop(FileAlterationObserver observer) {
		// TODO Auto-generated method stub
		
	}

}
