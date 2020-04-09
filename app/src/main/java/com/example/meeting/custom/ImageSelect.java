package com.example.meeting.custom;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.core.content.FileProvider;

public class ImageSelect {

    File cropFile;
    private final int FROM_CAMERA = 0;
    private final int FROM_ALBUM = 1;
    private Uri imgUri, photoURI, albumURI;
    private String mCurrentPhotoPath;
    File photoFile, tempFile;
    public Uri cropURI;
    Activity activity;

    Context context;

    public ImageSelect(Context context) {
        this.context = context;
        activity = (Activity) context;
    }

    public void selectImage() {

        final List<String> ListItems = new ArrayList<>();
        ListItems.add("카메라");
        ListItems.add("갤러리");
        ListItems.add("취소");
        final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("이미지 불러오기 방식");
        builder.setItems(items, (dialog, pos) -> {
            String selectedText = items[pos].toString();
            switch (selectedText) {
                case "카메라":
                    takePhoto();
                    break;
                case "갤러리":
                    selectAlbum();
                    break;
                case "취소":
                    break;
            }
        });
        builder.show();


    }

    private void selectAlbum() {
        //앨범 열기
        Log.v("알림", "앨범열기");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setType("image/*");

        activity.startActivityForResult(intent, FROM_ALBUM);
    }

    private void takePhoto() {
        // 촬영 후 이미지 가져옴
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (photoFile != null) {
                    Uri providerURI = FileProvider.getUriForFile(context, context.getPackageName(), photoFile);
                    imgUri = providerURI;
                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, providerURI);
                    activity.startActivityForResult(intent, FROM_CAMERA);
                }
            }
        } else {
            Log.v("알림", "저장공간에 접근 불가능");
            return;
        }
    }

    private File createImageFile() throws IOException {
        String imgFileName = System.currentTimeMillis() + ".jpg";
        File imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures", "ireh");
        if (!storageDir.exists()) {
            Log.v("알림", "storageDir 존재 x " + storageDir.toString());
            storageDir.mkdirs();
        }
        Log.v("알림", "storageDir 존재함 " + storageDir.toString());
        imageFile = new File(storageDir, imgFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    private void cropImage(Uri photoUri, File sendFile) {

        tempFile = sendFile;
        Uri savingUri = Uri.fromFile(tempFile);

        Log.v("알림", "크롭시작");
        Crop.of(photoUri, savingUri).asSquare().start(activity);
    }

    public void fromAlbum(Intent data) {
        if (data.getData() != null) {
            try {
                File albumFile = null;
                albumFile = createImageFile();
                photoURI = data.getData();
                Log.v("parkj", String.valueOf(photoURI));
                albumURI = Uri.fromFile(albumFile);
                galleryAddPic();
                Log.v("알림", "앨범에서 가져옴");
                // 선택한 이미지 자르기
                cropImage(photoURI, albumFile);
            } catch (Exception e) {
                e.printStackTrace();
                Log.v("알림", "앨범에서 가져오기 에러");
            }
        }
    }

    public void fromCamera(Intent data) {
        try {
            Log.v("알림", "FROM_CAMERA 처리");
            galleryAddPic();
            cropImage(imgUri, photoFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Uri fromCrop(Intent data) {
        Log.v("알림", "이미지 자르기");
        // 이미지 자르기
        cropFile = new File(Crop.getOutput(data).getPath());
        cropURI = Uri.fromFile(cropFile);
        return cropURI;
    }
}
