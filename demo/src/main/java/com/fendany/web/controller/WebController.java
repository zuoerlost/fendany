package com.fendany.web.controller;

import com.fendany.utils.cmd.RuntimeCmd;
import com.fendany.utils.cmd.RuntimeCmdBean;
import com.fendany.utils.security.Des3Pro;
import com.fendany.web.vo.SecurityVo;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

/**
 * Created by zuoer on 16-9-27.
 */
@RestController
public class WebController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebController.class);

    private static final String KEY = "@pingan@test@3des@key@00";

    private static final String DECODE = "3des简单加解密";

    private static final String CHARSET = "UTF-8";

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

    @RequestMapping(value = "/3des/simple", method = RequestMethod.POST)
    public SecurityVo simple3DES(@RequestBody SecurityVo securityVo) {
        if (securityVo == null) {
            return new SecurityVo(-1, "securityVo is null!pls check out!");
        }

        String key = securityVo.getKey();
        if (StringUtils.isEmpty(key)) {
            LOGGER.info(" Key no found!use default ");
            try {
                key = Base64.encodeBase64String(KEY.getBytes(CHARSET));
                securityVo.setKey(key);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                return new SecurityVo(-1, "default key is error!");
            }
        }

        String decode = securityVo.getDecode();
        String encode = securityVo.getEncode();

        try {
            byte[] key_byte = Base64.decodeBase64(key);
            if (StringUtils.isEmpty(decode) && StringUtils.isEmpty(encode)) {
                securityVo.setDecode(DECODE);
                byte[] enc = Des3Pro.encrypt(key_byte, DECODE.getBytes(CHARSET));
                securityVo.setEncode(Base64.encodeBase64String(enc));
                securityVo.setCode(1);
                securityVo.setMessage(" No Found Decode && Encode！Use Default ");
            } else if (StringUtils.isNotEmpty(decode) && StringUtils.isEmpty(encode)) {
                byte[] enc = Des3Pro.encrypt(key_byte, DECODE.getBytes(CHARSET));
                securityVo.setEncode(Base64.encodeBase64String(enc));
                securityVo.setCode(1);
                securityVo.setMessage(" Encode mode is work! ");
            } else if (StringUtils.isEmpty(decode) && StringUtils.isNotEmpty(encode)) {
                securityVo.setDecode(new String(Des3Pro.decrypt(key_byte, Base64.decodeBase64(encode.getBytes(CHARSET))), CHARSET));
                securityVo.setCode(1);
                securityVo.setMessage(" Decode mode is work! ");
            } else if (StringUtils.isNotEmpty(decode) && StringUtils.isNotEmpty(encode)) {
                String pa_encode =  new String(Des3Pro.decrypt(key_byte, Base64.decodeBase64(encode.getBytes(CHARSET))), CHARSET);
                if (decode.equals(pa_encode)) {
                    securityVo.setMessage(" Check mode is work & success");
                } else {
                    securityVo.setMessage(" Check mode is work & fail");
                }
                securityVo.setCode(1);
            } else {
                return new SecurityVo(-1, "I don't known why? can you tell me!");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return new SecurityVo(-1, "decode or encode is error!pls check out!");
        }

        return securityVo;
    }

}
