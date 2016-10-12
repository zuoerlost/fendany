package com.fendany.doc;

import com.alibaba.fastjson.JSONArray;

/**
 * Created by zuoer on 16-10-11.
 */
public interface DocCommandService {

    JSONArray getRunningContainers() throws Exception;

    JSONArray getAllContainers() throws Exception;

    JSONArray getLastContainer() throws Exception;

    boolean stopContainerByNameOrId(String nameOrId) throws Exception;

    boolean startContainerByNameOrId(String nameOrId) throws Exception;

    boolean restartContainerByNameOrId(String nameOrId) throws Exception;


}
