package com.example.cep.model;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cep.R;
import com.example.cep.api.CEPService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private Button btnResultado;
    private TextView textViewResult;
    private Retrofit retrofit;
    private EditText editTextCEP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnResultado = findViewById(R.id.btnResult);
        textViewResult = findViewById(R.id.textViewResult);
        editTextCEP = findViewById(R.id.editTextCEP);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://viacep.com.br/ws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        btnResultado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cep = editTextCEP.getText().toString();
                recuperarCEPRetrofit(cep);
            }
        });
    }

    private void recuperarCEPRetrofit(String cep) {

        CEPService cepService = retrofit.create(CEPService.class);
        Call<CEP> call = cepService.recuperarCEP(cep);

        call.enqueue(new Callback<CEP>() {
            @Override
            public void onResponse(Call<CEP> call, Response<CEP> response) {

                if (response.isSuccessful()) {
                    CEP cep = response.body();
                    textViewResult.setText("Cep: " + cep.getCep()
                            +"\nLogradouro: " + cep.getLogradouro()
                            +"\nComplemento: " + cep.getComplemento()
                            +"\nBairro: " + cep.getBairro()
                            +"\nLocalidade: " + cep.getLocalidade()
                            +"\nUF: " +cep.getUf()
                           );
                }
            }

            @Override
            public void onFailure(Call<CEP> call, Throwable t) {
                textViewResult.setText("Ops algo errado não esta certo!");
            }
        });

    }
}
