package com.example.eforezan.kuevents;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class second extends AppCompatActivity {
    private DatabaseReference mDatabase;
    EditText name;
    RadioGroup radio_g;
    RadioButton radio_b;
    Button button_sbm;
    Button button_showdb;
    int attempt_counter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        String post_key = getIntent().getExtras().getString("blog_id");

        radio_g = (RadioGroup) findViewById(R.id.radio_group);
        int selected_id = radio_g.getCheckedRadioButtonId();
        radio_b = (RadioButton) findViewById(selected_id);

        button_sbm = (Button) findViewById(R.id.button);
        button_showdb=(Button) findViewById(R.id.button_showdb);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Attendee");
        AddData();
        showdb();
    }

    public void AddData() {
        button_sbm.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (attempt_counter != 0) {
                            int selected_id = radio_g.getCheckedRadioButtonId();
                            radio_b = (RadioButton) findViewById(selected_id);
                            String value = name.getText().toString().trim();
                            String status = radio_b.getText().toString();

                            Toast.makeText(second.this, value + " is " + status, Toast.LENGTH_SHORT).show();

                            if (status.equals("Going")) {
                                mDatabase.push().setValue(value).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(second.this, "Data inserted", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(second.this, "Error", Toast.LENGTH_LONG).show();
                                        }


                                    }
                                });
                            } else {
                                Toast.makeText(second.this, "not inserted", Toast.LENGTH_SHORT).show();
                            }
                            attempt_counter--;
                        } else {
                            button_sbm.setEnabled(false);
                            Toast.makeText(second.this, "you can only poll once", Toast.LENGTH_LONG).show();
                        }
                    }
                }

        );

    }

    public void showdb(){
        button_showdb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                Intent intent2 = new Intent("com.example.eforezan.kuevents.display_data");
                startActivity(intent2);
            }
        });

    }
}

