package com.iaai.chaoxing.chaoxingsignweb.model;

import org.apache.commons.codec.digest.DigestUtils;

public class stringToMD5 {
    public String input;
    public stringToMD5(String input) {
        this.input = DigestUtils.md5Hex(input);
    }
}
