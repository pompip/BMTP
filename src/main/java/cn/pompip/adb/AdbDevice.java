/*
 * Copyright (c) 2019. Edit By pompip.cn
 */

package cn.pompip.adb;

import com.android.ddmlib.IDevice;
import com.google.common.base.Strings;
import cn.pompip.util.Constant;
import org.apache.log4j.Logger;

import javax.usb.UsbDevice;
import javax.usb.UsbException;
import javax.usb.UsbInterface;
import javax.usb.UsbInterfaceDescriptor;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdbDevice {
    
    private static Logger logger = Logger.getLogger(AdbDevice.class);
    
    /** Constant for ADB class. */
    private static final byte ADB_CLASS = (byte) 0xff;
    
    /** Constant for ADB sub class. */
    private static final byte ADB_SUBCLASS = 0x42;
    
    /** Constant for ADB protocol. */
    private static final byte ADB_PROTOCOL = 1;
    
    /** PropertyCahe KEY for serialNumber */
    public static final String SERIAL_NUMBER = "sn";
    
    /** PropertyCahe KEY for screenSize 获取的是 widthxheight的字符串 */
    public static final String SCREEN_SIZE = "SCREEN_SIZE";
    
    /** The claimed USB ADB interface. */
    private final UsbInterface iface;
    
    /** The in endpoint address. */
    private final byte inEndpoint;
    
    /** The out endpoint address. */
    private final byte outEndpoint;
    
    /** Reference From UsbDevice */
    private final UsbDevice usbDevice;
    
    /**  */
    Map<String, String> propertyCahe = new HashMap<>();
    
    /** Device Type */
    public enum Type {
        USBDevice,
        Other
    }
    
    private final Type type;
    
    
    /** IDevice */
    private IDevice iDevice;
    
    /**
     * Constructs a new ADB interface.
     *
     * @param iface
     *            The USB interface. Must not be null.
     * @param inEndpoint
     *            The in endpoint address.
     * @param outEndpoint
     *            THe out endpoint address.
     */
    public AdbDevice(UsbDevice usbDevice, UsbInterface iface, byte inEndpoint, byte outEndpoint)
    {
        if (iface == null)
            throw new IllegalArgumentException("iface must be set");
        this.type = Type.USBDevice;
        this.usbDevice = usbDevice;
        this.iface = iface;
        this.inEndpoint = inEndpoint;
        this.outEndpoint = outEndpoint;
    }
    
    public AdbDevice(IDevice iDevice) {
        this.type = Type.Other;
        this.usbDevice = null;
        this.iface = null;
        this.inEndpoint = 0;
        this.outEndpoint = 0;
        setIDevice(iDevice);
    }
    
    /**
     * Checks if the specified vendor is an ADB device vendor.
     *
     * @param vendorId
     *            The vendor ID to check.
     * @return True if ADB device vendor, false if not.
     */
    public static boolean isAdbVendor(short vendorId) {
        for (short adbVendorId: Vendors.VENDOR_IDS)
            if (adbVendorId == vendorId) return true;
        return false;
    }
    
    /**
     * Checks if the specified USB interface is an ADB interface.
     *
     * @param iface
     *            The interface to check.
     * @return True if interface is an ADB interface, false if not.
     */
    public static boolean isAdbInterface(UsbInterface iface) {
        UsbInterfaceDescriptor desc = iface.getUsbInterfaceDescriptor();
        return desc.bInterfaceClass() == ADB_CLASS &&
                desc.bInterfaceSubClass() == ADB_SUBCLASS &&
                desc.bInterfaceProtocol() == ADB_PROTOCOL;
    }
    
    /**
     * 获取SerialNumber，由于使用频繁，故特增加此接口
     * @return
     */
    public String getSerialNumber() {
        String serialNUmber = findPropertyCahe(SERIAL_NUMBER);
        if (serialNUmber == null) {
            if (iDevice != null) {
                serialNUmber = iDevice.getSerialNumber();
            } else if (usbDevice != null) {
                try {
                    serialNUmber = usbDevice.getSerialNumberString();
                } catch (UsbException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            propertyCahe.put(SERIAL_NUMBER, serialNUmber);
        }
        return serialNUmber;
    }
    
    public UsbDevice getUsbDevice() {
        return usbDevice;
    }
    
    public IDevice getIDevice() {
        return iDevice;
    }
    
    /**
     * 设置IDevice，同时会获取常用信息放到缓存中
     * @param iDevice
     */
    public void setIDevice(IDevice iDevice) {
        this.iDevice = iDevice;
        fillPropertyCahe();
    }
    
    public String getProperty(String name) {
        String s = findPropertyCahe(name);
        if (Strings.isNullOrEmpty(s)) {
            s = iDevice.getProperty(name);
        }
        return s;
    }
    
    /** 获取常用信息加入到缓存 */
    private void fillPropertyCahe() {
    
        // serialNumber
        propertyCahe.put(SERIAL_NUMBER, iDevice.getSerialNumber());

        // abi & sdk
        String abi = iDevice.getProperty(Constant.PROP_ABI);
        propertyCahe.put(Constant.PROP_ABI, abi);
        String sdk = iDevice.getProperty(Constant.PROP_SDK);
        propertyCahe.put(Constant.PROP_SDK, sdk);
        
        // android 4.3 以下没有 displays
        int sdkv = Integer.parseInt(sdk);
        String shellCmd = sdkv > 17 ? "dumpsys window displays | sed -n '3p'" : "dumpsys window";
        String str = AdbServer.executeShellCommand(iDevice, shellCmd);
        if (str != null && !str.isEmpty()) {
            Pattern pattern =  Pattern.compile("init=(\\d+x\\d+)");
            Matcher m = pattern.matcher(str);
            if (m.find()) {
                propertyCahe.put(SCREEN_SIZE, m.group(1));
            }
        }
    }
    
    /**
     * 在缓存中查找属性值
     * @param key
     */
    public String findPropertyCahe(String key) {
        return propertyCahe.get(key);
    }
    
}
