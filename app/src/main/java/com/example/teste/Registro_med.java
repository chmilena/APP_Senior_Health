package com.example.teste;


import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.SystemClock;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Registro_med extends AppCompatActivity{

    private EditText editTextNomeProduto;
    private EditText editTextDosagem; // Novo EditText para a dosagem
    private RadioGroup radioGroupDosagem;
    private EditText editTextIntervaloHoras;
    private Button buttonSelecionarData;
    private Button buttonSelecionarHora;
    private int horaSelecionada, minutoSelecionado;
    private EditText editTextQuantidadeDias;
    private Button buttonSalvar;
    private Button buttonMostrarProdutos; // Novo botão

    // Declare uma lista para armazenar os produtos
    private List<String> listaProdutos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_med);

        editTextNomeProduto = findViewById(R.id.editTextNomeMedicamento);
        editTextDosagem = findViewById(R.id.editTextDosagem);
        radioGroupDosagem = findViewById(R.id.radioGroupDosagem);
        buttonSelecionarData = findViewById(R.id.buttonSelecionarData);
        buttonSelecionarHora = findViewById(R.id.buttonSelecionarHora);
        editTextIntervaloHoras = findViewById(R.id.editTextIntervaloHoras);
        editTextQuantidadeDias = findViewById(R.id.editTextQuantidadeDias);
        buttonSalvar = findViewById(R.id.buttonSalvar);
        buttonMostrarProdutos = findViewById(R.id.buttonMostrarHorarios); // Novo botão

        // Configurar listeners para os botões de seleção de data e hora
        buttonSelecionarData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abrir o DatePickerDialog
                showDatePickerDialog();
            }
        });

        buttonSelecionarHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Abrir o TimePickerDialog
                showTimePickerDialog();

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Meu Canal de Notificação";
            String description = "Descrição do Canal";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel_id", name, importance);
            channel.setDescription(description);
            Log.i("MeuApp", "canal");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


        radioGroupDosagem.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Lógica para lidar com a seleção de dosagem aqui
            }
        });

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Coletar os dados do produto dos campos de entrada
                String nomeProduto = editTextNomeProduto.getText().toString();
                String dosagem = editTextDosagem.getText().toString(); // Obtenha a dosagem
                String dataInicial = buttonSelecionarData.getText().toString();
                String horaInicial = buttonSelecionarHora.getText().toString();
                String intervaloHoras = editTextIntervaloHoras.getText().toString();
                String quantidadeDias = editTextQuantidadeDias.getText().toString();

                // Construir uma representação do produto
                String produto = "Medicamento:" + nomeProduto + " - Dosagem: " + dosagem + " - Data: " + dataInicial;

                // Adicionar o produto à lista de produtos
                listaProdutos.add(produto);

                // Agendar a notificação com os dados coletados
                agendarNotificacao(nomeProduto, horaInicial, Integer.parseInt(intervaloHoras), Integer.parseInt(quantidadeDias));

                // Limpar os campos de entrada após a adição do produto
                editTextNomeProduto.setText("");
                editTextDosagem.setText(""); // Limpe o campo de dosagem
                radioGroupDosagem.clearCheck(); // Limpe a seleção do RadioButton de dosagem (se necessário)
                buttonSelecionarData.setText("DATA DE INÍCIO");
                buttonSelecionarHora.setText("HORA INICIAL");
                editTextIntervaloHoras.setText("");
                editTextQuantidadeDias.setText("");

                // Mostrar o diálogo de sucesso
                mostrarDialogoSucesso();

            }
        });

        buttonMostrarProdutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Iniciar a atividade ListaProdutosActivity e passar a lista de produtos
                Intent intent = new Intent(Registro_med.this, ListaMedicamentosActivity.class);
                intent.putStringArrayListExtra("produtos", new ArrayList<>(listaProdutos));
                startActivity(intent);
            }
        });
    }

    private void showDatePickerDialog() {
        // Obtém a data atual para definir como padrão no DatePickerDialog
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Crie um DatePickerDialog para a seleção de data
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Aqui você pode tratar a data selecionada
                String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                buttonSelecionarData.setText(selectedDate); // Atualize o campo de data com o valor selecionado
            }
        }, year, month, dayOfMonth);

        // Exiba o DatePickerDialog
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {

               // Obtém a hora atual para definir como padrão no TimePickerDialog
        Calendar calendar = Calendar.getInstance();
        int horaAtual = calendar.get(Calendar.HOUR_OF_DAY);
        int minutoAtual = calendar.get(Calendar.MINUTE);

        // Crie um TimePickerDialog para a seleção de hora
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                horaSelecionada = hourOfDay;
                minutoSelecionado = minute;// Aqui você pode chamar o método setAlarm para configurar o alarme
                setAlarm(horaSelecionada, minutoSelecionado);

                // Exemplo: exiba um Toast para confirmar o horário selecionado
                Toast.makeText(Registro_med.this, "Lembrete definido para " + horaSelecionada + ":" + minutoSelecionado, Toast.LENGTH_SHORT).show();
            }
        },
                horaAtual,
                minutoAtual,
                true
        );
        // Exiba o TimePickerDialog
        timePickerDialog.show();
    }

    // Método para configurar o alarme com base na hora e minuto selecionados
    private void setAlarm(int hourOfDay, int minute) {
        // Obtenha uma instância do AlarmManager
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Crie uma Intent para a ação que deseja executar quando o alarme disparar
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.setAction("ALARME_DISPARADO"); // Define uma ação personalizada

        // Você pode passar dados extras para o AlarmReceiver usando o Bundle
        Bundle extras = new Bundle();
        extras.putString("mensagem", "Hora de tomar o medicamento!"); // Exemplo de mensagem
        intent.putExtras(extras);

        // Crie um PendingIntent que será usado para disparar a Intent quando o alarme for acionado
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE
        );

        // Calcule o horário em milissegundos para o alarme
        long alarmTimeMillis = calculateAlarmTime(hourOfDay, minute);

        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
    }

    // Método para calcular o horário em milissegundos com base na hora e minuto selecionados
    private long calculateAlarmTime(int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Verifique se o horário selecionado é no futuro, caso contrário, adicione um dia
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        return calendar.getTimeInMillis();
    }
    private void mostrarDialogoSucesso() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_success, null);
        TextView textViewDialogMessage = dialogView.findViewById(R.id.textViewDialogMessage);
        textViewDialogMessage.setText("Medicação cadastrada com sucesso!");

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        }, 2000);
    }
    private long converterHoraParaMilissegundos(String hora) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date date = sdf.parse(hora);
            if (date != null) {
                return date.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0; // Retorno padrão se a conversão falhar
    }

    private void agendarNotificacao(String nomeProduto, String horaInicial, int intervaloHoras, int quantidadeDias) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Verifique se a horaInicial não está vazia antes de convertê-la em milissegundos
        if (!horaInicial.isEmpty()) {
            // Crie um Intent que irá disparar a notificação
            Intent intent = new Intent(this, AlarmReceiver.class);
            intent.putExtra("nome_produto", nomeProduto);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            // Calcule o tempo para a primeira notificação
            long horaEmMilissegundos = converterHoraParaMilissegundos(horaInicial);
            long intervaloEmMilissegundos = intervaloHoras * 60 * 60 * 1000; // Intervalo em horas convertido para milissegundos
            long primeiroDisparo = horaEmMilissegundos - SystemClock.elapsedRealtime();

            // Agende as notificações repetidas
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, primeiroDisparo, intervaloEmMilissegundos, pendingIntent);
        } else {
            // Lide com o caso em que horaInicial está vazia (pode exibir um erro ou mensagem apropriada)
            Toast.makeText(this, "Hora inicial vazia", Toast.LENGTH_SHORT).show();
        }
    }
}
