package Client.mqtt;

import java.io.*;
import java.net.*;

class MqttClient {
    public static void printHexString(byte[] b, int k) {
        for (int i = 0; i < k; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            System.out.print(hex.toUpperCase());
        }
        System.out.println();
    }

    public static void main(String args[]) throws Exception {
        String sentence;
        String modifiedSentence;
        byte[] conn = {0x10, 0x0e, 0x00, 0x04, 0x4d, 0x51, 0x54, 0x54, 0x04, /*to add */};
        //byte[] pub = {0x30, 0x07, 0x00, 0x04, 0x74, 0x65, 0x73, 0x74, 0x31};
        byte[] pub = {0x32, 0x09, 0x00, 0x04, 0x74, 0x65, 0x73, 0x74, 0x00, 0x01, 0x31};
        byte[] sub = {(byte) 0x82, 0x09, 0x00, 0x01, 0x00, 0x04, 0x74, 0x65, 0x73, 0x74, 0x00};

        byte[] receiveData = new byte[1024];
        if (args.length != 1)
            System.out.println("Usage: java TCPclient host ");
        else {

            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

            Socket clientSocket = new Socket(args[0], 1883);

            DataOutputStream outToServer =
                    new DataOutputStream(clientSocket.getOutputStream());
            DataInputStream dis = new DataInputStream(clientSocket.getInputStream());

            outToServer.write(conn, 0, conn.length);

            int i = dis.read(receiveData);
            //System.out.print("FROM SERVER: ");
            printHexString(receiveData, i);
            Thread.sleep(1000);
            outToServer.write(sub, 0, sub.length);
            i = dis.read(receiveData);
            //System.out.print("FROM SERVER: ");
            printHexString(receiveData, i);

            Thread.sleep(1000);
            outToServer.write(pub, 0, pub.length);
            i = dis.read(receiveData);
            printHexString(receiveData, i);

            clientSocket.close();
        }

    }
} 

