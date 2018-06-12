package com.weekend.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.util.Util;
import com.quickblox.sample.core.CoreApp;
import com.quickblox.sample.core.utils.constant.MimeType;
import com.weekend.R;
import com.weekend.base.WeekendApplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hb on 7/23/2016.
 */
public class ImageUtil {

    public static final int GALLERY_REQUEST_CODE = 183;
    public static final int CAMERA_REQUEST_CODE = 212;

    private static final String CAMERA_FILE_NAME_PREFIX = "CAMERA_";

    public static void loadImage(Context context, String url, ImageView imageView) {
        if (!TextUtils.isEmpty(url) && imageView != null /*&& !url.contains("noimage")*/) {
        /*do not load app if activity destroyed*/
            if (Util.isOnMainThread() && !(context instanceof Application)) {
                if (context instanceof FragmentActivity && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && ((FragmentActivity) context).isDestroyed()) {
                    return;
                } else if (context instanceof Activity && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && ((Activity) context).isDestroyed()) {
                    return;
                }
            }

            Glide.with(context)
                    .load(Uri.parse(url.replace("\\/", "/")))
                    .asBitmap()
                    .placeholder(R.mipmap.loading_img_square)
                    .into(new ViewTarget<ImageView, Bitmap>(imageView) {
                        @Override
                        public void onLoadStarted(Drawable placeholder) {
                            super.onLoadStarted(placeholder);
                            this.view.setImageDrawable(placeholder);
                        }

                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            this.view.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                        }
                    });
        } else {
            imageView.setImageResource(R.mipmap.loading_img_square);//launcher?
        }
    }

    public static File getLastUsedCameraFile() {
        File dataDir = new File(getAppExternalDataDirectoryPath());
        File[] files = dataDir.listFiles();
        List<File> filteredFiles = new ArrayList<>();
        for (File file : files) {
            if (file.getName().startsWith(CAMERA_FILE_NAME_PREFIX)) {
                filteredFiles.add(file);
            }
        }

        Collections.sort(filteredFiles);
        if (!filteredFiles.isEmpty()) {
            return filteredFiles.get(filteredFiles.size() - 1);
        } else {
            return null;
        }
    }

    public static String getAppExternalDataDirectoryPath() {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory())
                .append(File.separator)
                .append("Android")
                .append(File.separator)
                .append("data")
                .append(File.separator)
                .append(WeekendApplication.getInstance().getPackageName())
                .append(File.separator);

        return sb.toString();
    }

    public static String saveUriToFile(Uri uri) throws Exception {
        ParcelFileDescriptor parcelFileDescriptor = CoreApp.getInstance().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

        InputStream inputStream = new FileInputStream(fileDescriptor);
        BufferedInputStream bis = new BufferedInputStream(inputStream);

        File parentDir = new File(getAppExternalDataDirectoryPath());
        String fileName = String.valueOf(System.currentTimeMillis()) + ".jpg";
        File resultFile = new File(parentDir, fileName);

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(resultFile));

        byte[] buf = new byte[2048];
        int length;

        try {
            while ((length = bis.read(buf)) > 0) {
                bos.write(buf, 0, length);
            }
        } catch (Exception e) {
            throw new IOException("Can\'t save Storage API bitmap to a file!", e);
        } finally {
            parcelFileDescriptor.close();
            bis.close();
            bos.close();
        }

        return resultFile.getAbsolutePath();
    }

    public static void startImagePicker(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(MimeType.IMAGE_MIME);
        activity.startActivityForResult(Intent.createChooser(intent, activity.getString(com.quickblox.sample.core.R.string.dlg_choose_image_from)), GALLERY_REQUEST_CODE);
    }

    public static void startImagePicker(Fragment fragment) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(MimeType.IMAGE_MIME);
        fragment.startActivityForResult(Intent.createChooser(intent, fragment.getString(com.quickblox.sample.core.R.string.dlg_choose_image_from)), GALLERY_REQUEST_CODE);
    }

    public static void startCameraForResult(Activity activity) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(activity.getPackageManager()) == null) {
            return;
        }

        File photoFile = getTemporaryCameraFile();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        activity.startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    public static void startCameraForResult(Fragment fragment) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(CoreApp.getInstance().getPackageManager()) == null) {
            return;
        }

        File photoFile = getTemporaryCameraFile();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getValidUri(photoFile, fragment.getContext()));
        fragment.startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    private static Uri getValidUri(File file, Context context) {
        Uri outputUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String authority = context.getPackageName() + ".provider";
            outputUri = FileProvider.getUriForFile(context, authority, file);
        } else {
            outputUri = Uri.fromFile(file);
        }
        return outputUri;
    }

    public static File getTemporaryCameraFile() {
        File storageDir = new File(getAppExternalDataDirectoryPath());
        File file = new File(storageDir, getTemporaryCameraFileName());
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private static String getTemporaryCameraFileName() {
        return CAMERA_FILE_NAME_PREFIX + System.currentTimeMillis() + ".jpg";
    }

    public static void loadPropertyImage(Context context, String url, ImageView imageView) {
        if (!TextUtils.isEmpty(url) && imageView != null /*&& !url.contains("noimage")*/) {
        /*do not load app if activity destroyed*/
            if (Util.isOnMainThread() && !(context instanceof Application)) {
                if (context instanceof FragmentActivity && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && ((FragmentActivity) context).isDestroyed()) {
                    return;
                } else if (context instanceof Activity && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && ((Activity) context).isDestroyed()) {
                    return;
                }
            }

            Glide.with(context)
                    .load(Uri.parse(url.replace("\\/", "/")))
                    .asBitmap()
                    .placeholder(R.mipmap.loading_img)
                    .into(new ViewTarget<ImageView, Bitmap>(imageView) {
                        @Override
                        public void onLoadStarted(Drawable placeholder) {
                            super.onLoadStarted(placeholder);
                            this.view.setImageDrawable(placeholder);
                        }

                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            this.view.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                        }
                    });
        } else {
            imageView.setImageResource(R.mipmap.loading_img);//launcher?
        }
    }

    public static void loadImage1(Context context, Uri uri, ImageView imageView) {
        if (uri != null && imageView != null) {
        /*do not load app if activity destroyed*/
            if (Util.isOnMainThread() && !(context instanceof Application)) {
                if (context instanceof FragmentActivity && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && ((FragmentActivity) context).isDestroyed()) {
                    return;
                } else if (context instanceof Activity && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && ((Activity) context).isDestroyed()) {
                    return;
                }
            }

            Glide.with(context)
                    .load(uri)
                    .asBitmap()
                    .placeholder(R.mipmap.image_logo)
                    .into(new ViewTarget<ImageView, Bitmap>(imageView) {
                        @Override
                        public void onLoadStarted(Drawable placeholder) {
                            super.onLoadStarted(placeholder);
                            this.view.setImageDrawable(placeholder);
                        }

                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            this.view.setImageBitmap(resource);
                        }


                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                        }
                    });
        }
    }

    public static void loadImageYoutube(Context context, String url, ImageView imageView) {
        if (!TextUtils.isEmpty(url) && imageView != null /*&& !url.contains("noimage")*/) {
        /*do not load app if activity destroyed*/
            if (Util.isOnMainThread() && !(context instanceof Application)) {
                if (context instanceof FragmentActivity && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && ((FragmentActivity) context).isDestroyed()) {
                    return;
                } else if (context instanceof Activity && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && ((Activity) context).isDestroyed()) {
                    return;
                }
            }

            Glide.with(context)
                    .load(Uri.parse(url.replace("\\/", "/")))
                    .asBitmap()
                    .placeholder(R.mipmap.icon_youtube_new)
                    .into(new ViewTarget<ImageView, Bitmap>(imageView) {
                        @Override
                        public void onLoadStarted(Drawable placeholder) {
                            super.onLoadStarted(placeholder);
                            this.view.setImageDrawable(placeholder);
                        }

                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            this.view.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                        }
                    });
        } else {
            imageView.setImageResource(R.mipmap.icon_youtube_new);//launcher?
        }
    }


}
