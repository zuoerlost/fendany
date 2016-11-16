package com.fendany.utils.file;

import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;

import java.io.File;

/**
 * Created by moilions on 2016/11/15.
 */
public class FileMonitor {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(FileMonitor.class);

    private FileAlterationMonitor monitor ;

    public void init(FileListener fileListener , String filePath, long interval) throws Exception {
        monitor = new FileAlterationMonitor(interval);
        FileAlterationObserver observer = new FileAlterationObserver(new File(filePath));
        if (fileListener == null) throw new Exception("fileListener is null, please check");
        observer.addListener(fileListener);
        monitor.addObserver(observer);
    }

    public void init(FileListener fileListener , String filePath) throws Exception {
        init(fileListener,filePath,5000);
    }

    public void start(){
        try {
            monitor.start();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }
    }

    public void stop(){
        try {
            monitor.stop();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }
    }

}
