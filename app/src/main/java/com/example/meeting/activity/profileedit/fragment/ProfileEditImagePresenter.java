package com.example.meeting.activity.profileedit.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.meeting.R;
import com.example.meeting.model.userData;
import com.example.meeting.model.writingUserData;
import com.example.meeting.recyclerview.ProfileImageAdapter;
import com.example.meeting.recyclerview.ProfileImageEditAdapter;
import com.example.meeting.retrofit.NetRetrofit;
import com.google.gson.Gson;
import com.soundcloud.android.crop.Crop;

import org.json.JSONException;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.xml.transform.Result;

import androidx.core.content.FileProvider;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileEditImagePresenter {
    ProfileEditImageView view;

    private writingUserData writingUserData;
    private userData userdata;

    File cropFile;
    private final int FROM_CAMERA = 0;
    private final int FROM_ALBUM = 1;
    private Uri imgUri, photoURI, albumURI;
    private String mCurrentPhotoPath;
    File photoFile, tempFile;
    public Uri cropURI;

    private Mat matResult;


    public native long loadCascade(String cascadeFileName);
    public native int detect(long cascadeClassifier_face, long cascadeClassifier_eye, long matAddrInput, long matAddrResult);

    static {
        System.loadLibrary("opencv_java4");
        System.loadLibrary("native-lib");
    }

    public long cascadeClassifier_face = 0;
    public long cascadeClassifier_eye = 0;

    public ProfileEditImagePresenter(ProfileEditImageView view){
        this.view = view;
        writingUserData = new writingUserData(view.getActivityFragment());
        userdata = new userData(view.getActivityFragment());
    }

    void getData() throws JSONException {
        if (writingUserData.writingImage().equals("")){
            userData userdata = new userData(view.getActivityFragment());
            String addImage = "http://13.209.4.115/profileimage/add.png";
            ArrayList<String> userImage = userdata.getImage();
            userImage.add(addImage);
            view.getImage(userImage);
        }else{
            view.getImage(writingUserData.getImage());
        }


    }

    void writingChange(String image){

        writingUserData.setImage(image);
    }

    private void copyFile(String filename) {
        String baseDir = Environment.getExternalStorageDirectory().getPath();
        String pathDir = baseDir + File.separator + filename;

        AssetManager assetManager = view.getActivityFragment().getAssets();

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = assetManager.open(filename);
            outputStream = new FileOutputStream(pathDir);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            inputStream.close();
            inputStream = null;
            outputStream.flush();
            outputStream.close();
            outputStream = null;
        } catch (Exception e) {

        }

    }

    public void read_cascade_file() {
        copyFile("haarcascade_frontalface_alt.xml");
        copyFile("haarcascade_eye_tree_eyeglasses.xml");
        cascadeClassifier_face = loadCascade("haarcascade_frontalface_alt.xml");
        cascadeClassifier_eye = loadCascade("haarcascade_eye_tree_eyeglasses.xml");
    }

    public void selectImage(boolean add, int position) {
        if (add) {
            final List<String> ListItems = new ArrayList<>();
            ListItems.add("카메라");
            ListItems.add("갤러리");
            ListItems.add("취소");
            final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);

            AlertDialog.Builder builder = new AlertDialog.Builder(view.getActivityFragment());
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
        } else {
            final List<String> ListItems = new ArrayList<>();
            ListItems.add("삭제");
            ListItems.add("취소");
            final CharSequence[] items = ListItems.toArray(new String[ListItems.size()]);

            AlertDialog.Builder builder = new AlertDialog.Builder(view.getActivityFragment());
            builder.setTitle("이미지 불러오기 방식");
            builder.setItems(items, (dialog, pos) -> {
                String selectedText = items[pos].toString();
                switch (selectedText) {
                    case "삭제":

                        view.removeImage(position);
                        break;
                    case "취소":
                        break;
                }
            });
            builder.show();
        }

    }

    private void selectAlbum() {
        //앨범 열기
        Log.v("알림", "앨범열기");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setType("image/*");
        view.getActivityFragment().startActivityForResult(intent, FROM_ALBUM);
    }

    private void takePhoto() {
        // 촬영 후 이미지 가져옴
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(view.getActivityFragment().getPackageManager()) != null) {
                photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (photoFile != null) {
                    Uri providerURI = FileProvider.getUriForFile(view.getActivityFragment(), view.getActivityFragment().getPackageName(), photoFile);
                    imgUri = providerURI;
                    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, providerURI);
                    view.getActivityFragment().startActivityForResult(intent, FROM_CAMERA);
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
        view.getActivityFragment().sendBroadcast(mediaScanIntent);
    }

    private void cropImage(Uri photoUri, File sendFile) {

        tempFile = sendFile;
        Uri savingUri = Uri.fromFile(tempFile);

        Log.v("알림", "크롭시작");
        Crop.of(photoUri, savingUri).asSquare().start(view.getActivityFragment());
    }

    void fromAlbum(Intent data) {
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

    void fromCamera(Intent data) {
        try {
            Log.v("알림", "FROM_CAMERA 처리");
            galleryAddPic();
            cropImage(imgUri, photoFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void fromCrop(Intent data) {
        Log.v("알림", "이미지 자르기");
        // 이미지 자르기
        cropFile = new File(Crop.getOutput(data).getPath());
        cropURI = Uri.fromFile(cropFile);
        Bitmap bitmap = BitmapFactory.decodeFile(cropURI.getPath());
        int ret = getFace(bitmap);
        if(ret==0){
            Toast.makeText(view.getActivityFragment(), "얼굴이 나온사진으로 설정해주세요", Toast.LENGTH_SHORT).show();
        }else{
            view.addImage(String.valueOf(cropURI));
        }

    }

    private int getFace(Bitmap bitmap){

        OpenCVLoader.initDebug();
        Mat input = new Mat();

        Utils.bitmapToMat(bitmap, input);

        matResult = new Mat(input.rows(), input.cols(), input.type());
        int ret = detect(cascadeClassifier_face, cascadeClassifier_eye, input.getNativeObjAddr(),matResult.getNativeObjAddr());
        return ret;
    }

    void Save(ProfileImageEditAdapter adapter, String email){
        ArrayList<String> selectedItems = new ArrayList();

        if((adapter.getListData().size()-1)<3){
            Toast.makeText(view.getActivityFragment(), "사진을 3장이상 설정해주세요", Toast.LENGTH_SHORT).show();
        }else{
            for(int i=0; i<(adapter.getListData().size()-1); i++){

                if(adapter.getListData().get(i).getUrl().substring(0, 4).equals("file")){
                    File imageFile = new File(Objects.requireNonNull(Uri.parse(adapter.getListData().get(i).getUrl()).getPath()));
                    String imgFileName = System.currentTimeMillis()+"-"+email+"-";

                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
                    MultipartBody.Part body = MultipartBody.Part.createFormData("uploaded_file", imgFileName+imageFile.getName(), requestFile);

                    Call<Result> resultCall = NetRetrofit.getInstance().getService().profileImageUpload(body);
                    resultCall.enqueue(new Callback<Result>() {
                        @Override
                        public void onResponse(Call<Result> call, Response<Result> response) {
                            Log.d("Retrofit", response.toString());
                        }
                        @Override
                        public void onFailure(Call<Result> call, Throwable t) {
                            Log.d("Retrofit", "asd");
                        }
                    });
                    selectedItems.add("http://13.209.4.115/profileimage/"+imgFileName+imageFile.getName());
                }else{
                    selectedItems.add(adapter.getListData().get(i).getUrl());
                }

            }

            Gson gson = new Gson();
            String jsonPlace = gson.toJson(selectedItems);
            userdata.setImage(jsonPlace);
            Call<ResponseBody> res = NetRetrofit.getInstance().getService().profileImageEdit(email,jsonPlace);
            res.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String result = response.body().string();
                        if(result.equals("succes")){

                        }else{
                            Toast.makeText(view.getActivityFragment(), "error", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("Err", t.getMessage());
                }
            });
        }


    }
}
