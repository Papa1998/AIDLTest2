package com.example.aidlclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.aidl.bookaidl.Book;
import com.example.aidl.bookaidl.IBookManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button btn_getBookList;
    private Button btn_addBook;
    private Button btn_bindService;

    IBookManager bookManager;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bookManager = IBookManager.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_getBookList = findViewById(R.id.btn_getBookList);
        btn_addBook = findViewById(R.id.btn_addBook);
        btn_bindService = findViewById(R.id.btn_bindService);

        btn_bindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.example.aidlservice", "com.example.aidlservice.BookManagerService"));
                bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            }
        });

        btn_getBookList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    List<Book> list = bookManager.getBookList();
                    Log.i(TAG, "query book list, list type: " + list.getClass().getCanonicalName());
                    Log.i(TAG, "query book list, list content: " + list.toString());
                }catch (RemoteException e){
                    e.printStackTrace();
                }
            }
        });

        btn_addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Book book = new Book(3, "Android开发艺术探索");
                    bookManager.addBook(book);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }
}
