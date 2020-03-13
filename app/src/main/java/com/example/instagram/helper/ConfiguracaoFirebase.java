package com.example.instagram.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfiguracaoFirebase {

    private static DatabaseReference referenciaFirebase; //É estático porque não vamos instanciar essa classe, só vamos utilizar esse método
    private static FirebaseAuth referenciaAutenticacao;
    private static StorageReference storage;

    //Retorna a instância do FirebaseDatabase
    public static DatabaseReference getFirebase(){
        if ( referenciaFirebase == null ) {
            referenciaFirebase = FirebaseDatabase.getInstance().getReference(); //Permite gerenciar o banco de dados
        }
        return referenciaFirebase;
    }

    //Retorna a instância do FirebaseAuth
    public static FirebaseAuth getFirebaseAutenticacao(){
        if ( referenciaAutenticacao == null ) {
            referenciaAutenticacao = FirebaseAuth.getInstance();
        }
        return referenciaAutenticacao;

    }

    public static StorageReference getFirebaseStorage(){
        if ( storage == null ) {
            storage = FirebaseStorage.getInstance().getReference(); //com o getReference, terá que definir de FirebaseStorage para StorageReference
        }
        return storage;
    }
}
