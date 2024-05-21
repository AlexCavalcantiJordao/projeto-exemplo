
package com.example.projetofirebase3;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projetofirebase3.databinding.ActivityAgendamentoBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class Agendamento extends AppCompatActivity {

    private Button btAgendar;
    private ActivityAgendamentoBinding binding;
    private String Agenda;
    private String minutos, mes, nome, tecnico1, tecnico2, tecnico3;
    private EditText edit_data, edit_hora, edit_profissional, edit_mes, edit_nome;
    private TextView text_tecnico1, text_tecnico2, text_tecnico3, tecnico;
    private String data = "";
    private String hora = "";
    private final Calendar calendar = Calendar.getInstance();
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("agenda");
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String[] mensagens = {"Preencha todos os campos.", "O chamado foi finalizado com sucesso."};
    String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agendamento);

        binding = ActivityAgendamentoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String nome = getIntent().getExtras() != null ? getIntent().getExtras().getString("nome") : "";
        DatePicker datePicker = binding.datePicket;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            datePicker.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String dia = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                String mes = monthOfYear < 9 ? "0" + (monthOfYear + 1) : String.valueOf(monthOfYear + 1);
                data = dia + " / " + mes + " / " + year;
            });
        }
        TimePicker timePicker = binding.timePicker;
        timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            String minuto = minute < 10 ? "0" + minute : String.valueOf(minute);
            hora = hourOfDay + ":" + minuto;
        });
        timePicker.setIs24HourView(true);
        binding.btAgendar.setOnClickListener(view -> {

            FirebaseAuth.getInstance().signOut();
            boolean tecnico1 = binding.tecnico1.isChecked();
            boolean tecnico2 = binding.tecnico2.isChecked();
            boolean tecnico3 = binding.tecnico3.isChecked();

            if (data.isEmpty() || hora.isEmpty() || nome.isEmpty()) {
                Snackbar snackbar = Snackbar.make(view, mensagens[0], Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();

                while (hora.isEmpty()) {
                    FirebaseAuth.getInstance().signOut();
                    mensagens(view, "Preencha o horário", "#FF0000");
                } if (hora.compareTo("8:00") < 0 || hora.compareTo("17:00") > 0) {
                    mensagens(view, "Suporte Técnico não está em funcionamento - horário é das 08 Horas : 00 minutos ás 17 Horas : 00 minutos.", "#FF0000");
                    mensagens(view, "Coloque uma data", "#FF0000");

                } if (tecnico1 && !data.isEmpty() && !hora.isEmpty()) {
                    SalvarDadosUsuario(view, nome, "Alex Fonseca", data, hora);

                } if (tecnico2 && !data.isEmpty() && !hora.isEmpty()) {
                    SalvarDadosUsuario(view, nome, "Pedro Paulo", data, hora);

                } if (tecnico3 && !data.isEmpty() && !hora.isEmpty()) {
                    SalvarDadosUsuario(view, nome, "Lilian Cavalcanti", data, hora);

                } else {
                    mensagens(view, "Escolha um técnico", "#FF0000");
                    SalvarDadosUsuario(view, nome, data, mes, hora);
                    Snackbar snackbar1 = Snackbar.make(view, mensagens[1], Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();

                }
            }
        });
    }

    private void CadastrarUsuario(View view) {

        String nome = edit_nome.getText().toString();
        String mes = edit_mes.getText().toString();
        String data = edit_data.getText().toString();
        String hora = edit_hora.getText().toString();
        String tecnico1 = text_tecnico1.getText().toString();
        String tecnico2 = text_tecnico2.getText().toString();
        String tecnico3 = text_tecnico3.getText().toString();

        System.out.println("Numeração " + nome + mes + data + hora + tecnico1 + tecnico2 + tecnico3);
        // Criar um ID único para a pessoa no Firebase
        String usuarioID = databaseReference.push().getKey();
        Agenda agenda = new Agenda(nome, mes, data, hora, tecnico1, tecnico2, tecnico3);

        databaseReference.child(usuarioID).setValue(agenda).addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("chamada", "Deus me Ajuda" + agenda.getNome()); // 1

                if (task.isSuccessful()) {
                    Log.d("entrou", "entrou " + task.isSuccessful());
                    SalvarDadosUsuario(view, nome, "Alex Fonseca", data, hora);
                } else {
                    String erro;
                    try {
                        throw task.getException();
                    } catch (Exception e) {
                        erro = "Erro ao não preencher as todas as tabelas.";
                    }
                    Snackbar snackbar = Snackbar.make(view, erro, Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });

    }

    private void mensagens(View view, String mensagem, String cor) {
        Snackbar snackbar = Snackbar.make(view, mensagem, Snackbar.LENGTH_SHORT);
        snackbar.setBackgroundTint(Color.parseColor(cor));
        snackbar.setTextColor(Color.parseColor("#FFFFFF"));
        snackbar.show();
    }

    private void SalvarDadosUsuario(View view, String nome, String tecnico1, String tecnico2, String tecnico3) {

        String agenda = btAgendar.getText().toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> usuarios = new HashMap<>();
        usuarios.put("tecnico1, tecnico2, tecnico3, data, hora, agenda", agenda);

        DocumentReference documentReference = db.collection("Usuários").document(usuarioID);
        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void oVoid) {
                        Log.d("db", "Sucesso ao salvar os dados");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.w("db_error", "Erro ao salvar os dados" + e.toString());

                    }
                });
    }
}
