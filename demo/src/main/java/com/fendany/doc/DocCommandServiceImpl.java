package com.fendany.doc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fendany.utils.unix.UnixSocker;

/**
 * Created by zuoer on 16-10-12.
 */
public class DocCommandServiceImpl implements DocCommandService {

    @Override
    public JSONArray getRunningContainers() throws Exception {
        String ps = "GET /containers/json HTTP/1.1\r\n" + DocCommandHelper.COMMAND + "\r\n";
        String result = UnixSocker.INSTANCE.invoke(ps);
        DocCommandResult docCommandResult = DocCommandHelper.parse(result);
        if (docCommandResult.isFix()) {
            return JSON.parseArray(docCommandResult.getOut());
        }
        return null;
    }

    @Override
    public JSONArray getAllContainers() throws Exception {
        String ps_a = "GET /containers/json?all=1 HTTP/1.1\r\n" + DocCommandHelper.COMMAND + "\r\n";
        String result = UnixSocker.INSTANCE.invoke(ps_a);
        DocCommandResult docCommandResult = DocCommandHelper.parse(result);
        if (docCommandResult.isFix()) {
            return JSON.parseArray(docCommandResult.getOut());
        }
        return null;
    }

    @Override
    public JSONArray getLastContainer() throws Exception {
        String ps_l = "GET /containers/json?limit=1 HTTP/1.1\r\n" + DocCommandHelper.COMMAND + "\r\n";
        String result = UnixSocker.INSTANCE.invoke(ps_l);
        DocCommandResult docCommandResult = DocCommandHelper.parse(result);
        if (docCommandResult.isFix()) {
            return JSON.parseArray(docCommandResult.getOut());
        }
        return null;
    }

    @Override
    public boolean stopContainerByNameOrId(String nameOrId) throws Exception {
        String stop = "POST /containers/" + nameOrId + "/stop?t=10 HTTP/1.1\r\n" +
                DocCommandHelper.COMMAND +
                "Content-Length: 0\r\n" +
                "Content-Type: text/plain\r\n" +
                "\r\n";
        String result = UnixSocker.INSTANCE.invoke(stop);
        DocCommandResult docCommandResult = DocCommandHelper.parse(result);
        return docCommandResult.isFix();
    }

    @Override
    public boolean startContainerByNameOrId(String nameOrId) throws Exception {
        String start = "POST /containers/" + nameOrId + "/start HTTP/1.1\r\n" +
                DocCommandHelper.COMMAND +
                "Content-Length: 0\r\n" +
                "Content-Type: text/plain\r\n" +
                "\r\n";
        String result = UnixSocker.INSTANCE.invoke(start);
        DocCommandResult docCommandResult = DocCommandHelper.parse(result);
        return docCommandResult.isFix();
    }

    @Override
    public boolean restartContainerByNameOrId(String nameOrId) throws Exception {
        String restart = "POST /containers/" + nameOrId + "/restart?t=10 HTTP/1.1\r\n" +
                DocCommandHelper.COMMAND +
                "Content-Length: 0\r\n" +
                "Content-Type: text/plain\r\n" +
                "\r\n";
        String result = UnixSocker.INSTANCE.invoke(restart);
        DocCommandResult docCommandResult = DocCommandHelper.parse(result);
        return docCommandResult.isFix();
    }

}
