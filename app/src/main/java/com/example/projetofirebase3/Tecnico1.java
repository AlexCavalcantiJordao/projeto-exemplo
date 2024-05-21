package com.example.projetofirebase3;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Tecnico1 {
    private String nome;
    private String data;
    private String hora;
    private String dia;
    private String minutos;
    private String mes;

    public Tecnico1() {

    }

    public Tecnico1(String data, String hora, String dia, String minutos, String mes, String nome) {

        this.nome = nome;
        this.data = data;
        this.hora = hora;
        this.dia = dia;
        this.minutos = minutos;
        this.mes = mes;

    }

    public String getValue() {

        return nome + data + hora + dia + minutos + mes;
    }

    public String getNome() {
        return nome;
    }

    String getData() {
        return data;
    }

    String getHora() {
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

    // Criar uma referência ao banco de dados firebase....
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    // Função para gravar um objeto Dados no banco de dados firabase....
    public void SalvarDadosUsuario(Tecnico1 tecnico1) {
        String key = database.getKey();
        database.child("tecnico1").push().getKey();
        // Gravar o objeto Dados no banco de dados firebase sob a chave gerada....

        database.child("pessoa").child(key).setValue(tecnico1);
        // Função para ler todos os objetos Dados do banco de dados firebase....
    }

    // Função para ler todos os objetos Dados do banco de dados firebase....
    public void SalvarDadosUsuario() {
        // Obter uma referência à lista de dados armazenados no banco de dados firebase....

        DatabaseReference dadosRef = database.child("tecnico1");

        dadosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Iterar sobre os snapshots e converter cada um em um objeto Dados...
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Tecnico1 tecnico1 = snapshot.getValue(Tecnico1.class);

                    // Fazer algo com o objetos Dados, como imprimir na tela....

                    System.out.println(tecnico1.getData());
                    System.out.println(tecnico1.getDia());
                    System.out.println(tecnico1.getHora());
                    System.out.println(tecnico1.getMinutos());
                    System.out.println(tecnico1.getMes());
                    System.out.println(tecnico1.getNome());
                    System.out.println("-------------------------------");
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