package com.example.projetofirebase3;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Tecnico3 {

    private String tecnico3;
    private String nome;
    private String data;
    private String hora;
    private String dia;
    private String minutos;
    private String mes;

    public Tecnico3() {

    }

    public Tecnico3(String tecnico3, String data, String hora, String dia, String minutos, String mes, String nome) {

        this.tecnico3 = tecnico3;
        this.nome = nome;
        this.data = data;
        this.hora = hora;
        this.dia = dia;
        this.minutos = minutos;
        this.mes = mes;
    }

    public String getValue() {
        return tecnico3 + data + hora + dia + minutos + mes + nome;
    }

    public String getTecnico3() {
        return tecnico3;
    }

    public String getData() {
        return data;
    }

    public String getHora() {
        return hora;
    }

    String getDia() {
        return dia;
    }

    String getMinutos() {
        return minutos;
    }

    String getMes() {
        return mes;
    }

    String getNome() {
        return nome;
    }

    // Salvar os dados dos Técnicos2...
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public void SalvarDadosUsuario(Tecnico3 tecnico3) {

        String key = database.getKey();

        // Gravar o objeto Dados no banco de dados firebase sob a chave gerada....
        database.child("tecnico3").push().getKey();

        // Função para ler todos os objetos Dados do banco de dados firebase....
        database.child("tecnico3").child(key).setValue(tecnico3);
    }

    // Função para ler todos os objetos Dados do banco de dados firebase....
    public void SalvarDadosUsuario() {

        DatabaseReference dadosRef = database.child("tecnicos3");

        dadosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Tecnico2 tecnico2 = snapshot.getValue(Tecnico2.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Tratar o erro de leitura dos dados....
                System.out.println("Falha ao ler os dados.");
                databaseError.getMessage();

            }
        });

    }
}