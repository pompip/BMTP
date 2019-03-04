/*
 * Copyright (c) 2019. Edit By pompip.cn
 */

package cn.pompip.protocol;

public class BinaryProtocol {
    public static class Header {
        public final static short SM_SHOT = 0x0010;
        public final static short SM_JPG = 0x0011;
    }

    private short protocolHeader = 0;
    private byte[] protocolBody = new byte[0];

    public static BinaryProtocol newProtocol(short protocolHeader, byte[] protocolBody) {
        BinaryProtocol protocol = new BinaryProtocol();
        protocol.protocolHeader = protocolHeader;
        protocol.protocolBody = protocolBody;
        return protocol;
    }
    
}
