package com.example.instagram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.instagram.R;
import com.example.instagram.helper.Base64Custom;
import com.example.instagram.helper.ConfiguracaoFirebase;
import com.example.instagram.helper.UsuarioFirebase;
import com.example.instagram.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha;
    private Button botaoCadastrar;
    private ProgressBar progressBar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        inicializarComponentes();

        //cadastrar usuario
        progressBar.setVisibility(View.GONE);
        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Recuperar textos dos campos
                String textoNome    = campoNome.getText().toString();
                String textoEmail   = campoEmail.getText().toString();
                String textoSenha   = campoSenha.getText().toString();

                if ( !textoNome.isEmpty() ) { // Verifica nome: Se o textoNome não estiver vazio
                    if ( !textoEmail.isEmpty() ) { // Verifica e-mail: Se o textoEmail não estiver vazio
                        if ( !textoSenha.isEmpty() ) { //Verifica senha; Se o textoSenha não estiver vazio

                            usuario = new Usuario();
                            usuario.setNome( textoNome );
                            usuario.setEmail( textoEmail );
                            usuario.setSenha( textoSenha );

                            cadastrar( usuario );

                        }else {
                            Toast.makeText(CadastroActivity.this, "Preencha a senha!" , Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(CadastroActivity.this, "Preencha o email!" , Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(CadastroActivity.this, "Preencha o nome!" , Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public void inicializarComponentes(){

        campoNome = findViewById(R.id.editCadastroNome);
        campoEmail = findViewById(R.id.editCadastroEmail);
        campoSenha = findViewById(R.id.editCadastroSenha);
        botaoCadastrar = findViewById(R.id.buttonCadastrar);
        progressBar = findViewById(R.id.progressCadastro);

        campoNome.requestFocus();

    }

    /*  */

    public void cadastrar (final Usuario usuario){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()

        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if ( task.isSuccessful() ) { // Salvar os cadastros no Database

                    try {
                        progressBar.setVisibility(View.GONE);

                        //Salvar dados no firebase
                        String idUsuario = task.getResult().getUser().getUid();
                        usuario.setId( idUsuario );
                        usuario.salvar();

                        //Salvar dados no profile do firebase
                        UsuarioFirebase.atualizarNomeUsuario(usuario.getNome());

                        Toast.makeText(CadastroActivity.this, "Sucesso ao cadastrar usuário!", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else {

                    progressBar.setVisibility(View.GONE);

                    String excecao = "";
                    try {
                        throw task.getException();
                    } catch ( FirebaseAuthWeakPasswordException e) {
                        excecao = "Digite uma senha mais forte!";
                    } catch ( FirebaseAuthInvalidCredentialsException e) {
                        excecao = "Por favor, digite um email válido";
                    } catch ( FirebaseAuthUserCollisionException e) {
                        excecao = "Esta conta já foi cadastrada";
                    } catch ( Exception e ) {
                        excecao = "Erro ao cadastrar usuário" + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroActivity.this, excecao, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}








