package com.example.ido.appex2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;



import java.io.IOException;
import java.util.UUID;

public class UserActivity extends AppCompatActivity
{
    public static final String             TAG ="User Activity:";
    private FirebaseAuth m_Auth;
    private TextView m_Status;
    private ImageView m_ProfileImage;
    private TextView m_Email;
    private TextView m_UserName;
    private Button m_Logout_btn;

    private Button m_Upload_btn;

    private Uri m_PhotoUri;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    private boolean m_imagePicked = false;

    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        m_Auth = FirebaseAuth.getInstance();
        m_Status = findViewById(R.id.tvStatusUser);
        m_UserName = findViewById(R.id.tvUserNameFacebook);
        m_Email =  findViewById(R.id.tvEmailFacebook);
        m_ProfileImage = findViewById(R.id.ivFacebook);
        m_Logout_btn = findViewById(R.id.btn_Logout);
        m_Upload_btn = findViewById(R.id.btn_UploadPic);
        m_Logout_btn.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                m_Auth.signOut();
                LoginManager.getInstance().logOut();
                Intent intent_LogedIn = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent_LogedIn);
                finish();
            }
        });

        m_Upload_btn.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                Log.v(TAG, " click Upload profile pic");
                chooseAndUploadImage();
                uploadImage();
            }
        });

        updateLoginStatus("kaki");

    }
    
    private void updateLoginStatus(String details)
    {
        String profilePicUrl ="";
        FirebaseUser user = m_Auth.getCurrentUser();
        m_UserName.setVisibility(View.VISIBLE);
        if (user == null || user.isAnonymous())
        {
            m_Status.setText("Anonymous signed");
            m_UserName.setText("Name: Anoni Mos");
            m_Email.setText("email: mos@ano.ni" );
            m_ProfileImage.setImageResource(R.drawable.com_facebook_profile_picture_blank_portrait);
        }
        else
        {
            for(String a: user.getProviders() )
            {
                Toast.makeText(getApplicationContext(), a, Toast.LENGTH_SHORT).show();
                if (a.contains("facebook"))
                {
                    profilePicUrl = user.getPhotoUrl().toString() + "/picture?type=large";
                }

                else if (a.contains("google"))
                {
                    profilePicUrl = user.getPhotoUrl().toString();
                }
                else
                {
                    if(user.getPhotoUrl() != null) {
                        profilePicUrl = user.getPhotoUrl().toString()
                                + "picture?width=50&height=50";
                    }
                        else {
                            m_ProfileImage.setImageResource(R.drawable.com_facebook_profile_picture_blank_portrait);
                        }

                    }
                }
            }

            m_Status.setText("signed in");
            m_UserName.setText("Name: " + user.getDisplayName());
            m_Email.setText("email: " + user.getEmail());

           if(user.getPhotoUrl() != null)
           {
               /*Glide.with(this)
                       .load(profilePicUrl)
                       .into(m_ProfileImage);*/
               updateProfilePicInTheActivityView(profilePicUrl);
           }
           /*else {
               m_ProfileImage.setImageResource(R.drawable.upload_icon_orange);
               m_ProfileImage.setOnClickListener(new View.OnClickListener() {
                   //@Override
                   public void onClick(View v) {
                       Log.v(TAG, " click");
                       chooseAndUploadImage();
                       uploadImage();
                   }
               });
           }
           */
        Log.e(TAG, "updateLoginStatus() <<");
    }

    private void updateProfilePicInTheActivityView(String i_ProfilePicURL)
    {
        Glide.with(this)
                .load(i_ProfilePicURL)
                .into(m_ProfileImage);
    }


    private void chooseAndUploadImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                uploadImage();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            //

        }
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            Log.e(TAG, "-->>>>>>>>>filePath = "+ filePath.toString());
            final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                             progressDialog.dismiss();

                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final Uri downloadUrl = uri;
                                    Log.d(TAG, "onSuccess: uri= "+ uri.toString());
                                    updateUserPhotoInDB(downloadUrl);
                                }
                            });

                             Toast.makeText(getApplicationContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                             progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                       public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                             progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    private void updateUserPhotoInDB(Uri uri)
    {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest
                .Builder().setPhotoUri(uri).build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile pic updated.");
                            String newPicURI = user.getPhotoUrl().toString();
                            updateProfilePicInTheActivityView(newPicURI);
                            //finish();
                            //startActivity(getIntent());
                        }
                    }
                });

    }


}
