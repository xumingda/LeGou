package com.xq.LegouShop.socket.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.xq.LegouShop.socket.common.Constants;
import com.xq.LegouShop.util.LogUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpService extends Service {
    public static final String TAG = "xmd_TcpService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ClientBinder();
    }

    public class ClientBinder extends Binder {
        private int mHeart_spacetime = 3 * 1000; //心跳间隔时间
        private BufferedInputStream bis;
        private BufferedOutputStream bos;
        private ReadThread mReadThread;
        private Handler mHandler = new Handler();
        private Socket mSocket;
        private ExecutorService mExecutorService;
        private int tryCount = 0;//重试次数

        public void startConnect() {
            //在子线程进行网络操作
            // Service也是运行在主线程，千万不要以为Service意思跟后台运行很像，就以为Service运行在后台子线程
            if (mExecutorService == null) {
                mExecutorService = Executors.newCachedThreadPool();
            }
            mExecutorService.execute(connectRunnable);
        }

        private Runnable connectRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    // 建立Socket连接
                    mSocket = new Socket();
                    mSocket.connect(new InetSocketAddress(Constants.INTENT_IP, 8082), 2000);
                    bis = new BufferedInputStream(mSocket.getInputStream());
                    bos = new BufferedOutputStream(mSocket.getOutputStream());
                    // 创建读取服务器心跳的线程
                    mReadThread = new ReadThread();
                    mReadThread.start();
                    //开启心跳,每隔15秒钟发送一次心跳
                    mHandler.post(mHeartRunnable);
                    tryCount = 1;
                } catch (Exception e) {
                    tryCount ++ ;
                    e.printStackTrace();
                    Log.d(TAG, "Socket连接建立失败,正在尝试第"+ tryCount + "次重连"+e.getMessage());
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mExecutorService.execute(connectRunnable);
                        }
                    },mHeart_spacetime);
                }
            }
        };

        public class ReadThread extends Thread {
            @Override
            public void run() {


                int size;
//                byte[] buffer = new byte[1024];
                try {
                    // 客户端接收服务器端的响应，读取服务器端向客户端的输入流
                    InputStream isRead = mSocket.getInputStream();
                    // 缓冲区
                    byte[] buffer = new byte[isRead.available()];
                    if(true == mSocket.isConnected() && false == mSocket.isClosed()) {
                        while ((size = bis.read(buffer)) != -1) {
                            String str = new String(buffer, 0, size);
                            Log.d(TAG, "我收到来自服务器的消息: " + str);
                            //收到心跳消息以后，首先移除断连消息，然后创建一个新的60秒后执行断连的消息。
                            //这样每次收到心跳后都会重新创建一个60秒的延时消息，在60秒后还没收到心跳消息，表明服务器已死，就会执行断开Socket连接
                            //在60秒钟内如果收到过一次心跳消息，就表明服务器还活着，可以继续与之通讯。
                            mHandler.removeCallbacks(disConnectRunnable);
                            mHandler.postDelayed(disConnectRunnable, mHeart_spacetime * 40);


                        }

//                        // 读取缓冲区
//                        isRead.read(buffer);
//                        // 转换为字符串
//                        String responseInfo = new String(buffer);
//                        mHandler.removeCallbacks(disConnectRunnable);
//                        mHandler.postDelayed(disConnectRunnable, mHeart_spacetime * 40);
//                        Log.d(TAG, "我收到来自服务器的消息: " + responseInfo);
                    }else{
                        Log.d(TAG,"我收到来自服务器的消息: 关闭" );
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG,"我收到来自服务器的消息错误: " +e.getMessage());
                }
            }
        }

        private Runnable mHeartRunnable = new Runnable() {
            @Override
            public void run() {
                sendData();
            }
        };

        private void sendData() {
            mExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        //向服务器发送数据
                        PrintWriter send = new PrintWriter(new BufferedWriter(new OutputStreamWriter(bos,"utf-8")));
                        send.println("你个吊毛！");
                        send.flush();
//                        bos.write("555555555555555！".getBytes());
//                        //一定不能忘记这步操作
//                        bos.flush();
                        send.close();
                        //发送成功以后，重新建立一个心跳消息
                        mHandler.postDelayed(mHeartRunnable, mHeart_spacetime);
                        Log.d(TAG, "我发送给服务器的消息: 你个吊毛！");
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, "心跳任务发送失败，正在尝试第"+ tryCount + "次重连");
                        //mExecutorService.schedule(connectRunnable,mHeart_spacetime, TimeUnit.SECONDS);
                        mExecutorService.execute(connectRunnable);
                    }
                }
            });
        }

        private Runnable disConnectRunnable = new Runnable() {
            @Override
            public void run() {
                disConnect();
            }
        };

        private void disConnect() {
            mExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d(TAG, "正在执行断连: disConnect");
                        //执行Socket断连
                        mHandler.removeCallbacks(mHeartRunnable);
                        if (mReadThread != null) {
                            mReadThread.interrupt();
                        }

                        if (bos != null) {
                            bos.close();
                        }

                        if (bis != null) {
                            bis.close();
                        }

                        if (mSocket != null) {
                            mSocket.shutdownInput();
                            mSocket.shutdownOutput();
                            mSocket.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
