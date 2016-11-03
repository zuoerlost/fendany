package com.fendany.web.controller;

import com.fendany.utils.cmd.RuntimeCmd;
import com.fendany.utils.cmd.RuntimeCmdBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zuoer on 16-9-27.
 */
//@RestController
public class WebController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebController.class);

    @Autowired
    private RuntimeCmd runtimeCmd;

    @RequestMapping("/restart/{pid}")
    public RuntimeCmdBean restart(@PathVariable String pid) {
        LOGGER.info(" ==== PID [{}] ==== ", pid);
        String cmd =
//                "kill " + pid + " && " +
                " cd /fs/iws/demo/build/libs && java -jar demo-1.0-SNAPSHOT.jar &";
        String[] cmd_a = new String[3];
        cmd_a[0] = "/bin/sh";
        cmd_a[1] = "-c";
        cmd_a[2] = cmd;
        LOGGER.info(" ==== CMD [{}] ==== ", cmd);
        return runtimeCmd.execute(cmd_a);
    }

}
