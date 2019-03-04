/*
 * Copyright (c) 2019. Edit By pompip.cn
 */

package cn.pompip.protocol;

import java.security.InvalidParameterException;

public class TextProtocol {
    public static class Header {
        public final static String M_WAIT = "M_WAIT";
        public final static String M_START = "M_START";
        public final static String M_WAITTING = "M_WAITTING";
        public final static String M_TOUCH = "M_TOUCH";
        public final static String M_KEYEVENT = "M_KEYEVENT";
        public final static String M_INPUT = "M_INPUT";
        public final static String M_PUSH = "M_PUSH";
        public final static String M_SHOT = "M_SHOT";
        public final static String M_DEVICES = "M_DEVICES";
        
        
        public final static String SM_OPENED = "SM_OPENED";
        public final static String SM_SERVICE_STATE = "SM_SERVICE_STATE";
        public final static String SM_MESSAGE = "SM_MESSAGE";
        public final static String SM_DISCONNECT = "SM_DISCONNECT";
        public final static String SM_DEVICES = "SM_DEVICES";
        public final static String SM_SHOT = "SM_SHOT";
        public final static String SM_JPG = "SM_JPG";
    }
    
    private String protocolHeader = "";
    private String protocolBody = "";
    
    private TextProtocol() {
        
    }
    
    public static TextProtocol newProtocol(String protocolHeader, String protocolBody) {
        TextProtocol protocol = new TextProtocol();
        protocol.protocolHeader = protocolHeader;
        protocol.protocolBody = protocolBody;
        return protocol;
    }
    

    /**
     * 将字符串解析为协议
     * @return TextProtocol
     */
    public static TextProtocol ParseWithString(String str) throws InvalidParameterException {
        
        int splitIndex = -1;
        if ((splitIndex = str.indexOf("://")) == -1) {
            throw new InvalidParameterException(str + " 不是一个合法的协议格式");
        }
        
        TextProtocol protocol = new TextProtocol();
        protocol.protocolHeader = str.substring(0, splitIndex);
        protocol.protocolBody = str.substring(splitIndex + 3);
        return protocol;
    }

    public String getProtocolHeader() {
        return protocolHeader;
    }

    public String getProtocolBody() {
        return protocolBody;
    }
}



