package com.wstv.pad;

/**
 * <p>Description: </p>
 * AppConstant
 *
 * @author lilibin
 * @createDate 2019/3/8 13:29
 */

public interface AppConstant {

    String KEY_USER_BEAN = "key.user.bean";

    String ROOT_URL = "http://152.136.34.178:8890/";

//    String ROOT_URL = "http://192.168.1.120:8890/";

    String SEND_SMS_TIME_STAMP = "send.sms.time.stamp";

    String KEY_PARAM_USER_ID = "userID";

    String KEY_PARAM_TOKEN = "token";

    String W_X_APP_ID = "wx3ad2675417a0ad7d";

    int DEFAULT_PAGE_SIZE = 10;

    int BEAUTY_STYLE = 0; // 0 ：光滑  1：自然  2：朦胧

    int BEAUTY_LEVEL = 6; // 取值为0-9

    int WHITENING_LEVEL = 6; // 取值为0-9

    int RUDDY_LEVEL = 6; // 取值为0-9

    int[] levelArr = {R.drawable.level1, R.drawable.level2, R.drawable.level3,
            R.drawable.level4, R.drawable.level5, R.drawable.level6,
            R.drawable.level7, R.drawable.level8};

    String KEY_W_BIND = "isWBind";

    interface UploadInfo{
        String APP_ID = "1258568523";
        String SECRET_ID = "AKIDgAYhbFjIhrodDYyT6f065pKabOyJkM5Y";
        String SECRET_KEY = "NVO2QYWFivsaWCettd7MM7cS9amjQs65";
        String REGION = "ap-beijing"; //北京（中国大陆）
    }
}
