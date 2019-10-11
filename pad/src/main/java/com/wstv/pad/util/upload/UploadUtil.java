package com.wstv.pad.util.upload;

import android.app.Activity;
import android.content.Context;

import com.libin.mylibrary.base.util.Trace;
import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.model.bucket.PutBucketRequest;
import com.tencent.cos.xml.model.object.PutObjectRequest;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;
import com.wstv.pad.AppConstant;

/**
 * <p>Description: </p>
 * UploadUtil
 *
 * @author lilibin
 * @createDate 2019/5/23 09:52
 */

@SuppressWarnings("deprecation")
public class UploadUtil {

    private static CosXmlServiceConfig serviceConfig;

    private static QCloudCredentialProvider credentialProvider;

    public static void init(Context context){
        if (null == serviceConfig) {
            //创建 CosXmlServiceConfig 对象，根据需要修改默认的配置参数
            serviceConfig = new CosXmlServiceConfig.Builder()
                    .setRegion(AppConstant.UploadInfo.REGION)
                    .isHttps(true) // 使用 https 请求, 默认 http 请求
                    .setDebuggable(true)
                    .builder();

            /**
             * 初始化 {@link QCloudCredentialProvider} 对象，来给 SDK 提供临时密钥。
             */
            credentialProvider = new ShortTimeCredentialProvider(AppConstant.UploadInfo.SECRET_ID,
                    AppConstant.UploadInfo.SECRET_KEY, 300);
            createBucket(context, "avatar-" + AppConstant.UploadInfo.APP_ID);
        }
    }

    public static void createBucket(Context context, final String bucket){

        CosXmlService cosXml = new CosXmlService(context, serviceConfig, credentialProvider);
        PutBucketRequest putBucketRequest = new PutBucketRequest(bucket);
        cosXml.putBucketAsync(putBucketRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                Trace.d("createBucket", bucket + " crate success !");
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                Trace.d("createBucket", bucket + " crate failed caused by client :" + exception.getMessage());
                Trace.d("createBucket", bucket + " crate failed caused by service : " + serviceException.getMessage());
            }
        });
    }

    public static void upload(Context context, String path, String key){
        CosXmlService cosXmlService = new CosXmlService(context, serviceConfig, credentialProvider);
        // 初始化 TransferConfig
        TransferConfig transferConfig = new TransferConfig.Builder().build();
        /**
         若有特殊要求，则可以如下进行初始化定制。如限定当对象 >= 2M 时，启用分片上传，且分片上传的分片大小为 1M, 当源对象大于 5M 时启用分片复制，且分片复制的大小为 5M。
         TransferConfig transferConfig = new TransferConfig.Builder()
         .setDividsionForCopy(5 * 1024 * 1024) // 是否启用分片复制的最小对象大小
         .setSliceSizeForCopy(5 * 1024 * 1024) //分片复制时的分片大小
         .setDivisionForUpload(2 * 1024 * 1024) // 是否启用分片上传的最小对象大小
         .setSliceSizeForUpload(1024 * 1024) //分片上传时的分片大小
         .build();
         */

        //初始化 TransferManager
        TransferManager transferManager = new TransferManager(cosXmlService, transferConfig);

        String bucket = "avatar-" + AppConstant.UploadInfo.APP_ID;
//        String cosPath = "对象键"; //即对象到 COS 上的绝对路径, 格式如 cosPath = "text.txt";
//        String srcPath = "本地文件的绝对路径"; // 如 srcPath=Environment.getExternalStorageDirectory().getPath() + "/text.txt";
        String uploadId = null; //若存在初始化分片上传的 UploadId，则赋值对应 uploadId 值用于续传，否则，赋值 null。
        //上传对象
        COSXMLUploadTask cosxmlUploadTask = transferManager.upload(bucket, key, path, uploadId);

        /**
         * 若是上传字节数组，则可调用 TransferManager 的 upload(string, string, byte[]) 方法实现;
         * byte[] bytes = "this is a test".getBytes(Charset.forName("UTF-8"));
         * cosxmlUploadTask = transferManager.upload(bucket, cosPath, bytes);
         */

        /**
         * 若是上传字节流，则可调用 TransferManager 的 upload(String, String, InputStream) 方法实现；
         * InputStream inputStream = new ByteArrayInputStream("this is a test".getBytes(Charset.forName("UTF-8")));
         * cosxmlUploadTask = transferManager.upload(bucket, cosPath, inputStream);
         */


        //设置上传进度回调
        cosxmlUploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long complete, long target) {
                float progress = 1.0f * complete / target * 100;
                Trace.d("TEST",  String.format("progress = %d%%", (int)progress));
            }
        });
        //设置返回结果回调
        cosxmlUploadTask.setCosXmlResultListener(new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                COSXMLUploadTask.COSXMLUploadTaskResult cOSXMLUploadTaskResult = (COSXMLUploadTask.COSXMLUploadTaskResult)result;
                Trace.d("TEST",  "Success: " + cOSXMLUploadTaskResult.printResult());
            }

            @Override
            public void onFail(CosXmlRequest request, CosXmlClientException exception, CosXmlServiceException serviceException) {
                Trace.d("TEST",  "Failed: " + (exception == null ? serviceException.getMessage() : exception.toString()));
            }
        });
    }

    /**
     * 采用异步回调操作
     */
    public static void uploadAsync(final Activity activity, String srcPath, String cosPath, final UploadCallback callback) {
        String bucket = "avatar-" + AppConstant.UploadInfo.APP_ID;
//        String cosPath = qServiceCfg.getUploadCosPath();
//        String srcPath = qServiceCfg.getUploadFileUrl();
        CosXmlService cosXmlService = new CosXmlService(activity, serviceConfig, credentialProvider);

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket,cosPath,
                srcPath);
        putObjectRequest.setProgressListener(new CosXmlProgressListener() {
            @Override
            public void onProgress(long progress, long max) {
                float result = (float) (progress * 100.0 / max);
                Trace.w("XIAO", "progress =" + (long) result + "%");
            }
        });
        putObjectRequest.setSign(600, null, null);
        cosXmlService.putObjectAsync(putObjectRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest cosXmlRequest, CosXmlResult cosXmlResult) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(cosXmlResult.printResult());
                Trace.w("XIAO", "success = " + stringBuilder.toString());
//                show(activity, stringBuilder.toString());
                callback.onSuccess(cosXmlResult.accessUrl);
            }

            @Override
            public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException qcloudException, CosXmlServiceException qcloudServiceException) {
                StringBuilder stringBuilder = new StringBuilder();
                if(qcloudException != null){
                    stringBuilder.append(qcloudException.getMessage());
                }else {
                    stringBuilder.append(qcloudServiceException.toString());
                }
                Trace.w("XIAO", "failed = " + stringBuilder.toString());
//                show(activity, stringBuilder.toString());
                callback.onError();
            }
        });
    }

    public interface UploadCallback{
        void onSuccess(String path);

        void onError();
    }
}
