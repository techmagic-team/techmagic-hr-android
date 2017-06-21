package co.techmagic.hr.presentation.util;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by techmagic on 9/14/16.
 */
public class ImagePickerUtil {

    public static final String EXTENSION_JPEG = "image/jpeg";
    public static final String EXTENSION_JPG = "image/jpg";
    public static final String EXTENSION_PNG = "image/png";

    private static final String CHOOSER_TITLE = "Select image to upload";
    private static final String CAMERA_IMG_NAME = "cameraImage.jpeg";
    private static final int IMAGE_QUALITY = 80;
    private static final int IMAGE_HEIGHT = 1280;
    private static final int IMAGE_WIDTH = 720;

    public static Intent getPickImageChooserIntent(Context context) {

        Uri outputFileUri = getCaptureImageOutputUri(context);

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // the main intent is the last in the  list so pickup him up
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        Intent chooserIntent = Intent.createChooser(mainIntent, CHOOSER_TITLE);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }


    /**
     * Get URI to image received from capture  by camera.
     */
    public static Uri getCaptureImageOutputUri(Context context) {
        Uri outputFileUri = null;
        File getImage = context.getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), CAMERA_IMG_NAME));
        }
        return outputFileUri;
    }


    public static boolean deleteCameraImage(Context context) {
        File fileToDelete = new File(context.getExternalCacheDir().getPath() + "/" + CAMERA_IMG_NAME);
        if (fileToDelete.exists()) {
            return fileToDelete.delete();
        } else {
            return false;
        }
    }


    /**
     * Get the URI of the selected image from  {@link #getPickImageChooserIntent(Context context)}.<br/>
     * Will return the correct URI for camera  and gallery image.
     *
     * @param data the returned data of the  activity result
     */
    public static Uri getPickImageResultUri(Intent data, Context context) {
        boolean isCamera = true;
        if (data != null && data.getData() != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? getCaptureImageOutputUri(context) : data.getData();
    }


    public static String getMimeType(Context context, Uri uri) {
        String mimeType = null;

        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = context.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
        }
        return mimeType;
    }


    public static byte[] compressImage(Uri uri, Context context) {
        Bitmap bitmap = getBitmapFromUri(context, uri);

        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
            return stream.toByteArray();
        }

        return new byte[0];
    }


    public static byte[] compressImage(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, bos);
        return bos.toByteArray();
    }


    public static Bitmap getDecodedBitmapFromFile(File file) {
        try {
            // Decode image size
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(file), null, options);

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (options.outWidth / scale / 2 >= IMAGE_WIDTH && options.outHeight / scale / 2 >= IMAGE_HEIGHT) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options slaceOptions = new BitmapFactory.Options();
            slaceOptions.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(file), null, slaceOptions);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static Bitmap getBitmapFromUri(Context context, Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}