package com.wstv.pad.util;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * <p>Description: </p>
 * LoadingInterface
 *
 * @author lilibin
 * @createDate 2019/1/29 15:57
 */

public interface LoadingInterface {

    public void showLoadingDialog();

    public MaterialDialog getLoadingDialog();

    public void dismissLoadingDialog();
}
