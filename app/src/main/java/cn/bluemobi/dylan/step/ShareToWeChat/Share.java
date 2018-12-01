package cn.bluemobi.dylan.step.ShareToWeChat;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import cn.bluemobi.dylan.step.R;

public class Share {
    private static boolean checkInstallation(Context context, String packageName) {
        try {
            context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public  void shareToWeChat( Context context) {
        // TODO: 2015/12/13 将需要分享到微信的图片准备好
        try {
            if (!checkInstallation(context, "com.tencent.mm")) {
                Log.d("WXShare", "shareToWeChat: 未安装微信");
                return;
            }
            Intent intent = new Intent();
            //分享精确到微信的页面，朋友圈页面，或者选择好友分享页面
            ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
            intent.setComponent(comp);
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent.setType("image/*");
//        intent.setType("text/plain");
            //添加Uri图片地址
//        String msg=String.format(getString(R.string.share_content), getString(R.string.app_name), getLatestWeekStatistics() + "");
            String msg = context.getString(R.string.share_content);
            intent.putExtra("Kdescription", msg);
            ArrayList<Uri> imageUris = new ArrayList<Uri>();
            // TODO: 2016/3/8 根据不同图片来设置分享
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            //File dir = context.getExternalFilesDir(null);
            if (dir == null || dir.getAbsolutePath().equals("")) {
                dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());

            }
            Log.d("WXFILE", "shareToWeChat: "+ dir);
            File pic = new File(dir, "bigbang.jpg");
            pic.deleteOnExit();
            BitmapDrawable bitmapDrawable;
            if (Build.VERSION.SDK_INT < 22) {
                bitmapDrawable = (BitmapDrawable) context.getResources().getDrawable(R.mipmap.bannar);
            } else {
                bitmapDrawable = (BitmapDrawable) context.getDrawable(R.mipmap.bannar);
            }
            try {
                bitmapDrawable.getBitmap().compress(Bitmap.CompressFormat.JPEG, 75, new FileOutputStream(pic));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                imageUris.add(Uri.fromFile(pic));
            } else {
                //修复微信在7.0崩溃的问题
                Uri uri = Uri.parse(android.provider.MediaStore.Images.Media.insertImage(context.getContentResolver(), pic.getAbsolutePath(), "bigbang.jpg", null));
                imageUris.add(uri);
            }

            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
            ((Activity) context).startActivityForResult(intent, 1000);
        } catch (Throwable e) {
            //SnackBarUtil.show(view,R.string.share_error);
            Log.d("WXShare", "shareToWeChat:分享失败 ");
        }
    }
}
