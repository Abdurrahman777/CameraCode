package com.ps.cameracode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.bumptech.glide.Glide;
import com.ps.cameracode.databinding.ActivityMainBinding;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    Context mContext;
    private Uri image_profile_uri;
    private Bitmap bitmapProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        init();
        ClickEvent();
    }

    private void init() {
        mContext=this;
    }

    private void ClickEvent() {
        binding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });
    }

    private void checkPermission() {
        String[] perms = {/*Manifest.permission.ACCESS_FINE_LOCATION,*/ Manifest.permission.READ_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON).start(this);
        } else {
            EasyPermissions.requestPermissions(this, "We need permissions",
                    123, perms);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                image_profile_uri = result.getUri();
                binding.image.setImageURI(image_profile_uri);
                try {
                    bitmapProfilePic = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), image_profile_uri);
                    Glide.with(mContext).load(bitmapProfilePic).into(binding.image);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}