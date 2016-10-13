package com.fendany.doc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by zuoer on 16-10-11.
 *
 */
public interface DocCommandService {

    /** Docker Info */
    JSONObject getDockerInfo() throws Exception;

    /** Containers 基本操作 */
    JSONArray getRunningContainers() throws Exception;

    JSONArray getAllContainers() throws Exception;

    JSONArray getLastContainer() throws Exception;

    JSONObject getContainerInfoByNameOrId(String nameOrId) throws Exception;

    boolean stopContainerByNameOrId(String nameOrId) throws Exception;

    boolean startContainerByNameOrId(String nameOrId) throws Exception;

    boolean restartContainerByNameOrId(String nameOrId) throws Exception;

    boolean killContainerByNameOrId(String nameOrId) throws Exception;

    /** images 基本操作 */
    JSONArray getImages() throws Exception;

}
