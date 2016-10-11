package com.fendany.doc;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * Created by zuoer on 16-10-11.
 */
public interface DocCommandService {

    List<JSONObject> getRunningContainers();

    List<JSONObject> getAllContainers();

    JSONObject getLastContainer();

    boolean stopContainerByNameOrId(String nameOrId);

    boolean startContainerByNameOrId(String nameOrId);

    boolean restartContainerByNameOrId(String nameOrId);


}
