package com.shenzhou.intelligenceordering.print;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.shenzhou.intelligenceordering.bean.OrderBean;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by ww on 2018/9/10.
 * 打印服务：
 * 1.连接打印机
 * 2.免丢单实现
 * 3.提供打印机状态和返回成功失败信息
 */

public class PrintService extends Service {

    private String mPrinterIP;
    private int mPrinterPort;
    private int mConnectTimeOut = 5000;


    private InputStream mInStream = null;
    private OutputStream mOutStream = null;
    private BufferedWriter mBufferedWriter = null;
    private Socket mSocket = null;
    /*默认重连*/
    private boolean isReConnect = true;
    private ConnectThread mConnectThread = null;
    /*是否退出接收线程*/
    private volatile boolean isCancel = false;
    /*是否打印票面数据*/
    private volatile boolean isPrintFlag = false;

    /*重连次数*/
    private int mConnectCount = 1;


    private boolean mConnState = false;
    public static final  boolean CONNECTED_SUCCESS = true;
    public static final  boolean CONNECTED_FAILE = false;

    private SocketBinder sockerBinder = new SocketBinder();

    private PrintStatusListener printStatusListener;

    public PrintService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sockerBinder;
    }

    public class SocketBinder extends Binder {
        public PrintService getService() {
            return PrintService.this;
        }
    }

    public void setmConnectCount(int mConnectCount) {
        this.mConnectCount = mConnectCount;
    }

    /*初始化socket*/
    public void initSocket(String ip, int port) {
        if(mSocket == null){
            isCancel = false;
            mConnectThread = new ConnectThread(ip, port);
            mConnectThread.start();
        }
    }

    public void setPrintStatusListener(PrintStatusListener printStatusListener) {
        this.printStatusListener = printStatusListener;
    }

    /**
     * 连接线程
     */
    private class ConnectThread extends Thread{

        public ConnectThread(String ip, int port){
            mPrinterIP = ip;
            mPrinterPort = port;
        }

        @Override
        public void run() {
            super.run();
            try {
                mSocket = new Socket();
                mSocket.connect(new InetSocketAddress(mPrinterIP, mPrinterPort), mConnectTimeOut);

                if (mSocket.isConnected()) {
                    mOutStream= mSocket.getOutputStream();
                    mBufferedWriter = new BufferedWriter(new OutputStreamWriter(mOutStream, "GBK"));
                    mInStream = mSocket.getInputStream();
                    SetState(CONNECTED_SUCCESS);

                    printStatusListener.onMessageListener(Constants.RESP_MSG_CONNECT_SUCCESS, "");


                    //开启读取线程
                    new ReadThread().start();
                    //开启心跳线程
//                    new HeartBeatThread().start();
                    //开启自动状态返回
                    sendData(Constants.COMM_BACK_STATUS_ON, false);
                }
            }catch (Exception e){
                SetState(CONNECTED_FAILE);
                if (e instanceof SocketTimeoutException) {
                    printStatusListener.onMessageListener(Constants.RESP_MSG_SOCKET_TIMEOUT, "");
                    releaseSocket();
                } else if (e instanceof NoRouteToHostException) {
                    Log.i("aaaaaaaaaaaaaaaa", "---ConnectThread--NoRouteToHostException---" );
                    printStatusListener.onMessageListener(Constants.RESP_MSG_HOST_ERROR, "");
                } else if (e instanceof ConnectException) {
                    printStatusListener.onMessageListener(Constants.RESP_MSG_CONNECT_EXCEPTION, "");
                    releaseSocket();
                }
            }
        }
    }

    /**
     * 接收指令线程
     */
    private class ReadThread extends Thread{
        @Override
        public void run() {
            super.run();
            while(!isCancel){
                if(!isConnected()){
                    break;
                }
                Log.i("aaaaaaaaaaaaaaaa", "-----ReadThread--try-: "  );
                try {
                    if(mInStream != null){
                        int size;
                        byte[] initBuffer = new byte[4];
                        size = mInStream.read(initBuffer);
                        Log.i("aaaaaaaaaaaaaaaa", "-----ReadThread---size: " + size );
                        if(size>0){
                            StringBuilder stringBuilder = new StringBuilder();

                            for(int i=0;i<size;i++){
                                stringBuilder.append(Byte2String(initBuffer[i]));
                            }
                            Log.i("aaaaaaaaaaaaaaaa", "-----ReadThread---"+ stringBuilder.toString()+"" );
                            backPrinterStatus(stringBuilder.toString());
                        }
                    }
                    Thread.sleep(500);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.i("aaaaaaaaaaaaaaaa", "-----ReadThread--Exception-" );
                    releaseSocket();
                }
            }
        }
    }

    public String Byte2String(byte b){
        String hex = Integer.toHexString(b & 0xFF);
        if (hex.length() == 1) {
            hex = '0' + hex;
        }
        return hex;
    }

    public void backPrinterStatus(String status){
        if (status.equalsIgnoreCase(PrintStatus.PS_OK.getValue())) {
            //状态正常
            if(isPrintFlag){
                printStatusListener.onMessageListener(Constants.RESP_MSG_PRINT_SUCCESS, "");
            }else{
                printStatusListener.onPrinterListener(PrintStatus.PS_OK.getCode());
            }

        }else if (status.equalsIgnoreCase(PrintStatus.PS_PRINTING.getValue())) {
            //正在打印
            printStatusListener.onPrinterListener(PrintStatus.PS_PRINTING.getCode());

        }else if (getStatusSymbol(status, 0, 2).equalsIgnoreCase("3C")) {
            //打印机盖未关闭
            printStatusListener.onPrinterListener(PrintStatus.PS_OPEN_TOP.getCode());

        }else if (getStatusSymbol(status, 5, 6).equalsIgnoreCase("C")) {
            //缺纸
            printStatusListener.onPrinterListener(PrintStatus.PS_OUT_OF_PAPER.getCode());
            isPrintFlag = false;

        }else if(status.equalsIgnoreCase(PrintStatus.PS_NOCOMPLETE_OFF_PRINT.getValue())){
            //错误恢复返回未完成并禁止打印，需人工初始化，才能往下进行
            printStatusListener.onPrinterListener(PrintStatus.PS_NOCOMPLETE_OFF_PRINT.getCode());
            sendData(Constants.COMM_INIT_PRINTER, false);

        }else if(status.equalsIgnoreCase(PrintStatus.PS_NOCOMPLETE_CLS_OFF_PRINT.getValue())){
            //清除禁止打印状态
            sendData(Constants.COMM_CLEAR_OFF_PRINT, false);

        }else if(status.equalsIgnoreCase(PrintStatus.PS_NOCOMPLETE_NORMAL_PRINT.getValue())){
            //清除未完成状态
            sendData(Constants.COMM_CLEAR_NOCOMPLETE, false);
            //打印未完成，打印失败
            printStatusListener.onMessageListener(Constants.RESP_MSG_PRINT_FAILED, "");

        }else if(getStatusSymbol(status, 6, 8).equalsIgnoreCase("4F")){
            //其他错误后禁止打印，需人工初始化，才能往下进行
            printStatusListener.onPrinterListener(PrintStatus.PS_OTHER_OFF_PRINT.getCode());
            sendData(Constants.COMM_INIT_PRINTER, false);
        }
    }

    /**
     * 在返回状态中获取状态符号
     * @return
     */
    private String getStatusSymbol(String status, int start, int end){
        if(status != null && !status.equals("")){
            return status.substring(start, end);
        }
       return "";
    }

    /**
     * 发送指令线程
     */
    private class WriteThread extends Thread{

        private String data;

        public WriteThread(String data, boolean isPrint){
            this.data = data;
            isPrintFlag = isPrint;
        }

        @Override
        public void run() {
            super.run();
            if (!isConnected()) {
                releaseSocket();
            }
            try {
                if(mBufferedWriter != null){
                    Log.i("aaaaaaaaaaaaaaaa", isPrintFlag + "-----WriteThread---"+ data );
                    mBufferedWriter.write(data);
                    mBufferedWriter.flush();
                }

                if(isPrintFlag){
                    //票面打印完成，切纸
                    mBufferedWriter.write(new String(Constants.COMM_CUT_PAPER));
                    mBufferedWriter.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("aaaaaaaaaaaaaaaa", "-----WriteThread-Exception--"+ e.getMessage() );
                releaseSocket();
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.i("aaaaaaaaaaaaaaaa", "-----WriteThread-sleep--"+ e.getMessage() );
            }
        }
    }

    /**
     * 打印菜单
     * @param bean
     */
    public void printMenu(OrderBean bean){
        sendData(PrintFormatUtil.getFoodMenuTemplate(bean), true);
    }

    /**
     * 发送数据、指令
     * @param data
     * @param isPrint 是否打印
     */
    public void sendData(byte[] data, boolean isPrint){
        sendData(new String(data), isPrint);
    }

    /**
     * 发送数据、指令
     * @param data
     * @param isPrint 是否打印
     */
    public void sendData(String data, boolean isPrint){
        new WriteThread(data, isPrint).start();
    }

    public void SetState(Boolean state)
    {
        mConnState = state;
    }

    private class HeartBeatThread extends Thread{

        public HeartBeatThread() {

        }

        @Override
        public void run() {
            super.run();
            while(!isCancel){
                try {
                    if(!isConnected()){
                        break;
                    }

                    mSocket.sendUrgentData(0xFF);
                    Log.i("aaaaaaaaaaaaaaaa", "-----HeartBeatThread---");
    //                SetState(true);
                    Thread.sleep(1000);
                } catch (Exception e) {
    //                SetState(false);
                    Log.i("aaaaaaaaaaaaaaaa", "-----IOException---");
                    releaseSocket();
                }
            }
        }
    }

    /**
     * 判断本地socket连接状态
     */
    private boolean isConnected() {
        if (mSocket.isClosed() || !mSocket.isConnected() ||
                mSocket.isInputShutdown() || mSocket.isOutputShutdown()) {
            return false;
        }
        return true;
    }

    /*释放资源*/
    private void releaseSocket() {
        isCancel = true;
        if(mOutStream != null){
            try {
                mOutStream.close();
                mOutStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(mInStream != null){
            try {
                mInStream.close();
                mInStream = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(mSocket != null){
            try {
                mSocket.close();
                mSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mConnectThread != null) {
            mConnectThread = null;
        }

        if(isReConnect){
            if(mConnectCount <= 3) {
                initSocket(mPrinterIP, mPrinterPort);
                printStatusListener.onMessageListener(Constants.RESP_MSG_RECONNECT, "第"+ mConnectCount +"次重连");
                mConnectCount++;
            }
        }
    }

    @Override
    public void onDestroy() {
        isReConnect = false;
        releaseSocket();
        super.onDestroy();
    }
}
