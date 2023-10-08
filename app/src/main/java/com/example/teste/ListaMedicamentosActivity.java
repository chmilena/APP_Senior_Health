package com.example.teste;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ListaMedicamentosActivity extends AppCompatActivity {

    private Button buttonVoltar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_medicamentos); // Atualize o layout conforme necessário

        // Receba a lista de produtos da Intent
        ArrayList<String> produtos = getIntent().getStringArrayListExtra("produtos");

        // Configure um ArrayAdapter para preencher a ListView com os produtos
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, produtos);

        // Obtenha a referência para a ListView no layout
        ListView listViewProdutos = findViewById(R.id.listViewMedicamentos); // Atualize o ID conforme necessário

        // Defina o adaptador na ListView
        listViewProdutos.setAdapter(adapter);

        // Configure o botão "Voltar" para retornar à tela de cadastro e limpar campos
        buttonVoltar = findViewById(R.id.buttonVoltar);
        buttonVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Voltar à tela de cadastro de produtos
                onBackPressed(); // Isso irá executar o comportamento padrão de voltar
            }
        });
    }
}
