package ru.protasov_dev.letmecodeinterviewtask;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;

public class SavePictures {

    private Context context;
    private View view;
    private Activity activity;
    private String imageName;
    private ImageView imageView;

    public SavePictures(Context context,
                        View view,
                        Activity activity,
                        String imageName,
                        ImageView imageView){
        this.context = context;
        this.view = view;
        this.activity = activity;
        this.imageName = imageName;
        this.imageView = imageView;
        save();
    }

    private void save(){
        if(checkPermission()){
            OutputStream outputStream = null;
            long currentTime = Calendar.getInstance().getTimeInMillis();
            File folderToSave = Environment.getExternalStorageDirectory();

            try{
                File file = new File(folderToSave, imageName + currentTime + ".jpg");
                outputStream = new FileOutputStream(file);
                try {
                    Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

                    outputStream.flush();
                    outputStream.close();

                    MediaStore.Images.Media.insertImage(context.getContentResolver(),
                            file.getAbsolutePath(), file.getName(), file.getName());

                    Snackbar.make(view, "Image saved!", Snackbar.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar.make(view, "File not available or not found!", Snackbar.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Snackbar.make(view, "There is no write access!", Snackbar.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else {
            Snackbar.make(view, context.getString(R.string.try_again), Snackbar.LENGTH_SHORT).show();
        }
    }

    private boolean checkPermission(){
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            return true;
        }
    }
}
