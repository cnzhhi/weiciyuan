package me.ivps.android.myweibo.support.asyncdrawable;

import java.lang.ref.WeakReference;

import me.ivps.android.myweibo.R;
import me.ivps.android.myweibo.support.utils.ThemeUtility;

import android.graphics.drawable.ColorDrawable;

/**
 * User: qii Date: 12-9-5
 */
public class PictureBitmapDrawable extends ColorDrawable {
    private final WeakReference<IPictureWorker> bitmapDownloaderTaskReference;
    
    public PictureBitmapDrawable(IPictureWorker bitmapDownloaderTask) {
        super(ThemeUtility.getColor(R.attr.listview_pic_bg));
        bitmapDownloaderTaskReference = new WeakReference<IPictureWorker>(
                bitmapDownloaderTask);
    }
    
    public IPictureWorker getBitmapDownloaderTask() {
        return bitmapDownloaderTaskReference.get();
    }
}
