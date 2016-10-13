package com.fendany.doc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import static com.fendany.doc.DocCommandHelper.*;

/**
 * Created by zuoer on 16-10-12.
 * docker
 * 1.查看当前docker信息
 *
 * container
 * 1.运行容器查看
 * 2.所有容器查看
 * 3.最后一次操作的容器查看
 * 4.容器启动,停止，重启
 * 5.容器KILL
 * 6.查看单一容器完整信息
 *
 * images
 * 1.所有镜像的查看
 */
public class DocCommandServiceImpl implements DocCommandService {

    @Override
    public JSONObject getDockerInfo() throws Exception {
        DocCommandResult docCommandResult = invoke(buildGetInfo());
        if (docCommandResult.isFix() && ("200").equals(docCommandResult.httpCode)) {
            return JSON.parseObject(docCommandResult.getOut());
        }
        return null;
    }

    @Override
    public JSONArray getRunningContainers() throws Exception {
        DocCommandResult docCommandResult = invoke(buildGetContainers(""));
        if (docCommandResult.isFix() && ("200").equals(docCommandResult.httpCode)) {
            return JSON.parseArray(docCommandResult.getOut());
        }
        return null;
    }

    @Override
    public JSONArray getAllContainers() throws Exception {
        DocCommandResult docCommandResult = invoke(buildGetContainers("?all=1"));
        if (docCommandResult.isFix() && ("200").equals(docCommandResult.httpCode))
            return JSON.parseArray(docCommandResult.getOut());
        return null;
    }

    @Override
    public JSONArray getLastContainer() throws Exception {
        DocCommandResult docCommandResult = invoke(buildGetContainers("?limit=1"));
        if (docCommandResult.isFix() && ("200").equals(docCommandResult.httpCode))
            return JSON.parseArray(docCommandResult.getOut());
        return null;
    }

    @Override
    public JSONObject getContainerInfoByNameOrId(String nameOrId) throws Exception {
        DocCommandResult docCommandResult = invoke(buildGetContainers(nameOrId, "/json"));
        if (docCommandResult.isFix() && ("200").equals(docCommandResult.httpCode))
            return JSON.parseObject(docCommandResult.getOut());
        return null;
    }

    @Override
    public boolean stopContainerByNameOrId(String nameOrId) throws Exception {
        DocCommandResult docCommandResult = invoke(buildPostContainers(nameOrId, "/stop?t=10"));
        return checkHttpStatus(docCommandResult.getHttpCode());
    }

    @Override
    public boolean startContainerByNameOrId(String nameOrId) throws Exception {
        DocCommandResult docCommandResult = invoke(buildPostContainers(nameOrId, "/start"));
        return checkHttpStatus(docCommandResult.getHttpCode());
    }

    @Override
    public boolean restartContainerByNameOrId(String nameOrId) throws Exception {
        DocCommandResult docCommandResult = invoke(buildPostContainers(nameOrId, "/restart?t=10"));
        return checkHttpStatus(docCommandResult.getHttpCode());
    }

    @Override
    public boolean killContainerByNameOrId(String nameOrId) throws Exception {
        DocCommandResult docCommandResult = invoke(buildPostContainers(nameOrId, "/kill?signal=KILL"));
        return checkHttpStatus(docCommandResult.getHttpCode());
    }

    @Override
    public JSONArray getImages() throws Exception {
        DocCommandResult docCommandResult = invoke(buildGetImages(""));
        if (docCommandResult.isFix())
            return JSON.parseArray(docCommandResult.getOut());
        return null;
    }

}
