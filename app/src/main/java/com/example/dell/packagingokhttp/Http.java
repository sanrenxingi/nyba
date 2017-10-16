package com.example.dell.packagingokhttp;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by dell on 2017/10/16.
 */

public class Http<T> {
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            T obj = (T) msg.obj;
            //接口回调

        }
    };
private OkHttpClient client;
    private Http() {
        client = new OkHttpClient.Builder()
                .addInterceptor(new HttpInterceptor())
                .build();
    }
   //get网络请求
   public<T> void getload(String url, final Map<String,String> map, final Class<T> clazz){
      Request.Builder builder=new Request.Builder();
      StringBuffer buffer=new StringBuffer(url);
      for (Map.Entry<String, String> entry : map.entrySet()) {
         buffer.append(entry.getKey()+"="+entry.getValue()+"&");
      }
      builder.url(buffer+"");

       Request request = builder.build();
       //异步请求
     client.newCall(request).enqueue(new Callback() {
         //失败
         @Override
         public void onFailure(Call call, IOException e) {

         }
         //成功
         @Override
         public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    String string = response.body().string();
                    Gson gson=new Gson();
                    T t = gson.fromJson(string, clazz);
                    Message message=new Message();
                    message.obj=t;
                    handler.sendMessage(message);
                }
         }
     });
   }
   //post网络请求
   public<T> void postload(String url, Map<String,String> map, final Class<T> clazz){
       FormBody.Builder builder=new FormBody.Builder();
       for (Map.Entry<String, String> entry : map.entrySet()) {
           builder.add(entry.getKey(),entry.getValue());
       }
       RequestBody requestBody = builder.build();
       Request request=new Request.Builder()
               .url(url)
               .post(requestBody)
               .build();
       client.newCall(request).enqueue(new Callback() {
           //失败
           @Override
           public void onFailure(Call call, IOException e) {

           }
           //成功
           @Override
           public void onResponse(Call call, Response response) throws IOException {
             if(response.isSuccessful()){
                 String string = response.body().string();
                 Gson gson=new Gson();
                 T t = gson.fromJson(string, clazz);
                 Message message=new Message();
                 message.obj=t;
                 handler.sendMessage(message);
             }
           }
       });
   }
}
