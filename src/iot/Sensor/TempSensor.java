package iot.Sensor;

import iot.util.Checkout;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.Scanner;

public class TempSensor {
    public final static String HEX = "0123456789ABCDEF";
    private String mac; //传感器网关mac地址
    private int nodeType;  //传感器无线通信类型
    private String addr; //传感器节点地址
    MqttClient client;

    public TempSensor(MqttClient client,String mac, int nodeType, String addr) {
        this.client = client;
        this.mac = mac;
        this.nodeType = nodeType;
        this.addr = addr;
    }

    public void upload(float value) {
        byte[] payload = new byte[22];
        payload[0] = 0x55; //header
        payload[1] = 0x17; //length
        payload[2] = 0x01; //消息类型，0x0001
        payload[3] = 0x00;
        payload[4] = (byte) this.nodeType; //节点类型，蓝牙为4
        for (int i = 0; i < 6; i++) {  //节点地址，按顺序
            payload[5 + i] = (byte) (HEX.indexOf(this.addr.charAt(i * 2)) * 16 + HEX.indexOf(this.addr.charAt(i * 2 + 1)));
        }
        for (int i = 0; i < 5; i++) { //节点LED和KEY的状态值
            payload[11 + i] = 0x00;
        }
        payload[16] = 0x02; //传感器类型，0x0102
        payload[17] = 0x01;
        System.arraycopy(floatToBytes(value, true), 0, payload, 18, 4);//传感器数值
        System.out.println(payload.toString());
        byte[] xorPayload = Checkout.xor(payload); //异或校验
        String topic = "IOTV3-GW/GW{" + this.mac + "}";  //组合消息主题
        MqttMessage message = new MqttMessage(xorPayload);
        message.setQos(2);
        try {
            client.publish(topic, message); //发布消息
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * float转字节数组
     *
     * @param f   float值
     * @param lsb 是否低字节在前
     * @return
     */
    public static byte[] floatToBytes(float f, boolean lsb) {
        int intNum = Float.floatToIntBits(f);
        String hexStr = Integer.toHexString(intNum).toUpperCase();
        int len = hexStr.length() / 2;
        byte[] bt = new byte[len];
        for (int i = 0; i < len; i++) {
            if (lsb) {
                bt[len - i - 1] += HEX.indexOf(hexStr.charAt(i * 2)) << 4;
                bt[len - i - 1] += HEX.indexOf(hexStr.charAt(i * 2 + 1));
            } else {
                bt[i] += HEX.indexOf(hexStr.charAt(i * 2)) << 4;
                bt[i] += HEX.indexOf(hexStr.charAt(i * 2 + 1));
            }
        }
        return bt;
    }

    public static String getHexString(byte[] bt, boolean lsb) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0, len = bt.length; i < len; i++) {
            int num = 0;
            if (lsb) {
                num = (bt[len - i - 1] & 0xFF);
            } else {
                num = (bt[i] & 0xFF);
            }
            sb.append(HEX.charAt(num >> 4));
            sb.append(HEX.charAt(num % 16));
        }
        return sb.toString();
    }
}