package com.example.greenhouse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;


public class Menu extends AppCompatActivity  {

    private Button buttonIniciar;
    private Intent myIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_menu);

        buttonIniciar =	(Button)	findViewById(R.id.buttonIniciar);


        buttonIniciar.setOnClickListener	(new View.OnClickListener(){
            @Override
            public	void	onClick(View v)	{
                myIntent	=	new	Intent(Menu.this, MainActivity.class);
                myIntent.putExtra("firstKeyName",	"FirstKeyValue");
                startActivity(myIntent);
            }
        });
    }
}