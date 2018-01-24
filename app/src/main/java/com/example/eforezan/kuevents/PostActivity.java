package com.example.eforezan.kuevents;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import static android.Manifest.permission_group.CALENDAR;

public class PostActivity extends AppCompatActivity {

    private ImageButton mSelectImage;
    private EditText mTitle;
    private EditText mDesc;
    private TextView mDate;
    private TextView mTime;
    private Button mPost;
    private Uri mimageUri = null;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;

    private static final int GALLERY_REQUEST=1;

    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgress;

    private static final String TAG = "PostActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mSelectImage=(ImageButton) findViewById(R.id.imageSelect);
        mTitle=(EditText) findViewById(R.id.TitleField);
        mDesc=(EditText) findViewById(R.id.DescField);
        mDate = (TextView) findViewById(R.id.dateField);
        mPost = (Button) findViewById(R.id.post_btn);

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Events");
        mProgress= new ProgressDialog(this);

        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent( Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog= new DatePickerDialog(
                        PostActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month +1;
                Log.d(TAG, "onDateSet: dd/mm/yy " + day + "/" + month + "/" + year );
                String date = day + "/" + month + "/" + year;
                mDate.setText(date);

            }
        };


        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });
    }

    private void startPosting(){
        mProgress.setMessage("Posting...");


        final String title_value = mTitle.getText().toString().trim();
        final String desc_value = mDesc.getText().toString().trim();
        final String date_value = mDate.getText().toString().trim();

        if (!TextUtils.isEmpty(title_value) && !TextUtils.isEmpty(desc_value) && mimageUri!=null){
            mProgress.show();
            StorageReference filepath = mStorage.child("Event Images").child(mimageUri.getLastPathSegment());
            filepath.putFile(mimageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl=taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost = mDatabase.push();

                    newPost.child("title").setValue(title_value);
                    newPost.child("desc").setValue(desc_value);
                    newPost.child("image").setValue(downloadUrl.toString());
                    newPost.child("date").setValue(date_value);


                    mProgress.dismiss();
                    startActivity(new Intent(PostActivity.this, HomePage.class));
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GALLERY_REQUEST && resultCode==RESULT_OK){
            mimageUri = data.getData();
            mSelectImage.setImageURI(mimageUri);
        }

    }
}
