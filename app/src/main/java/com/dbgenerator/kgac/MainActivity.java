package com.dbgenerator.kgac;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dbgenerator.kgac.database.DatabaseClient;
import com.dbgenerator.kgac.database.entity.EANMaster;
import com.dbgenerator.kgac.database.entity.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int READ_EXTERNAL_STORAGE_PERMISSION_CODE = 101;

    private Button btnAction;
    private TextView txtEAN;
    private TextView txtUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAction = findViewById(R.id.btn_action);
        btnAction.setOnClickListener(this);
        txtEAN = (TextView) findViewById(R.id.EAN_count);
        txtUser = (TextView) findViewById(R.id.user_count);
    }

    @Override
    protected void onResume() {
        super.onResume();
        createDocumentFolderIfRequired();
    }

    private void checkPermissions() {
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
//            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_CODE);
//        } else {
        createDocumentFolderIfRequired();
//        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        checkPermissions();
//    }

    private void createDocumentFolderIfRequired() {
        File docFolder = new File(getExternalFilesDir(null), "Document");
        File dbFolder = new File(getExternalFilesDir(null), "Generated_DB");
        if (docFolder.mkdir()) {
            Log.d("TAG", "Insert files in Document folder");
        }
        if (dbFolder.mkdir()) {
            Log.d("TAG", "Generated database will be in Generated_DB folder");
        }
    }

    private void insertEANMaster() {

        class EANMasterTask extends AsyncTask<Void, Void, Integer> {

            @Override
            protected Integer doInBackground(Void... voids) {
                List<EANMaster> eanMasterFromFile = createEANMasterListFromFile();
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().eanMasterDao().deleteAll();
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .eanMasterDao()
                        .insertAll(eanMasterFromFile);
//                List<EANMaster> all = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
//                        .eanMasterDao().getAll();
//                for (int i = 0; i < all.size(); i++) {
//                    Log.d("TAG", "User name= " + all.get(i).toString());
//                }
                return eanMasterFromFile.size();
            }

            @Override
            protected void onPostExecute(Integer count) {
                super.onPostExecute(count);
                txtEAN.setVisibility(View.VISIBLE);
                txtEAN.setText("EAN entry count: " + count);
                exportDatabase("KgacData");
            }
        }
        EANMasterTask st = new EANMasterTask();
        st.execute();
    }

    private List<EANMaster> createEANMasterListFromFile() {
        File userMaster = new File(getExternalFilesDir(null), "Document/EAN_MASTER.txt");
        ArrayList<EANMaster> eanMasterList = new ArrayList<>();
        if (!userMaster.exists()) {
            Log.d("TAG", "EAN MASTER FILE DOES NOT EXISTS");
            return eanMasterList;
        }
        FileInputStream inputStream = null;
        Scanner sc = null;
        try {
            try {
                inputStream = new FileInputStream(userMaster.getAbsolutePath());
            } catch (FileNotFoundException e) {
                Log.d("TAG", "Error while parsing EAN MASTER FILE: " + e.getMessage());
                return eanMasterList;
            }
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                Log.d("TAG", "Reading line: " + line);
                eanMasterList.add(new EANMaster(line.trim()));
            }
            if (sc.ioException() != null) {
                Log.d("TAG", "EXCEPTION WHILE READING EAN MASTER");
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sc != null) {
                sc.close();
            }
        }
        return eanMasterList;
    }

//    ------------------

    private void insertUser() {

        class UserTask extends AsyncTask<Void, Void, Integer> {

            @Override
            protected Integer doInBackground(Void... voids) {
                List<User> userListFromFile = createUserListFromFile();
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase().userDao().deleteAll();
                DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                        .userDao()
                        .insertAll(userListFromFile);
//                List<User> all = DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
//                        .userDao().getAll();
//                for (int i = 0; i < all.size(); i++) {
//                    Log.d("TAG", "User name= " + all.get(i).toString());
//                }
                return userListFromFile.size();
            }

            @Override
            protected void onPostExecute(Integer count) {
                super.onPostExecute(count);
                txtUser.setVisibility(View.VISIBLE);
                txtUser.setText("User entry count: " + count);
                insertEANMaster();
            }

        }
        UserTask st = new UserTask();
        st.execute();
    }

    private List<User> createUserListFromFile() {
        File userMaster = new File(getExternalFilesDir(null), "Document/USER_MASTER.txt");
        ArrayList<User> userList = new ArrayList<>();
        if (!userMaster.exists()) {
            Log.d("TAG", "USER MASTER FILE DOES NOT EXISTS");
            return userList;
        }
        FileInputStream inputStream = null;
        Scanner sc = null;
        try {
            try {
                inputStream = new FileInputStream(userMaster.getAbsolutePath());
            } catch (FileNotFoundException e) {
                Log.d("TAG", "Error while parsing USER MASTER FILE: " + e.getMessage());
                return userList;
            }
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                Log.d("TAG", "Reading line: " + line);
                String[] itemArray = line.split(",");
                userList.add(new User(itemArray[0], itemArray[1], itemArray[2]));
            }
            if (sc.ioException() != null) {
                Log.d("TAG", "EXCEPTION WHILE READING USER_MASTER");
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sc != null) {
                sc.close();
            }
        }
        return userList;
    }

    public void exportDatabase(String databaseName) {
        try {
            File sd = new File(getExternalFilesDir(null), "Generated_DB");

            if (sd.canWrite()) {
                String backupDBPath = "KgacData";
                File currentDB = getDatabasePath(databaseName);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_action:
                insertUser();
                break;
        }
    }
}