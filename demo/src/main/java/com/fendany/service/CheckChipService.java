package com.fendany.service;

import com.fendany.utils.file.FileListener;
import com.fendany.utils.file.FileMonitor;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.io.File;
import java.util.Map;

/**
 * Created by moilions on 2016/11/15.
 */
public class CheckChipService implements InitializingBean{

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckChipService.class);

    // save the mapping info
    private static final Map<String,String> mappingCache = new HashedMap<>();

//    private static final Map

    /**
     * 坚挺目录，包含所有信息地址
     */
    private String listenerPath ;

    private FileListener fileListener = new FileListener() {

        @Override
        public void onStart(FileAlterationObserver observer) {
            LOGGER.info(" 【CCS】: listener on [{}] " , observer.getDirectory());
        }

        @Override
        public void onDirectoryCreate(File directory) {
            LOGGER.info(" 【CCS】: NO CARE DirectoryCreate [{}] " , directory.getAbsolutePath());
        }

        @Override
        public void onDirectoryChange(File directory) {
            LOGGER.info(" 【CCS】: NO CARE DirectoryChange [{}] " , directory.getAbsolutePath());
        }

        @Override
        public void onDirectoryDelete(File directory) {
            LOGGER.info(" 【CCS】: NO CARE DirectoryDelete [{}] " , directory.getAbsolutePath());
        }

        @Override
        public void onFileCreate(File file) {

        }

        @Override
        public void onFileChange(File file) {

        }

        @Override
        public void onFileDelete(File file) {

        }

        @Override
        public void onStop(FileAlterationObserver observer) {
            LOGGER.info(" 【CCS】: stop listener on [{}] " , observer.getDirectory());
        }
    };



    @Override
    public void afterPropertiesSet() throws Exception {
        //启动文件坚挺 1.xlsx 2.JSON 3.property
        FileMonitor fileMonitor = new FileMonitor();
        fileMonitor.init(fileListener,listenerPath,5000);
        fileMonitor.start();
    }

}
