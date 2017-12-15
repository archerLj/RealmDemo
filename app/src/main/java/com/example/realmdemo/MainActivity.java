package com.example.realmdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.realmdemo.db.idb.MHDDBCallBack;
import com.example.realmdemo.db.idb.MHDDBFindAllCallBack;
import com.example.realmdemo.db.idb.MHDDBFindCallBack;
import com.example.realmdemo.db.impl.MHDDBHelper;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "SecondActivity";
    private MHDDBHelper mMHDDBHelper;

    private Button insertOne;
    private Button insertMulti;
    private Button deleteFirst;
    private Button deleteAll;
    private Button updateFirst;
    private Button updateAll;
    private Button findFirst;
    private Button findAll;

    
    ///////////////////////////////////////////////////////////////////////
    //                     LifeCycle methods
    ///////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMHDDBHelper = new MHDDBHelper();
        viewInit();
        btnAction();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mMHDDBHelper.clear();
    }


    ///////////////////////////////////////////////////////////////////////
    //                 MHDDBHelper usage method
    ///////////////////////////////////////////////////////////////////////
    private void insertOne() {

        User user = new User();
        user.setName("User 1");
        user.setAge(10);

        mMHDDBHelper.insert(user, new MHDDBCallBack() {
            @Override
            public void success() {
                Log.d(TAG, "insert one success: ");
            }

            @Override
            public void failed(Throwable error) {
                Log.d(TAG, "insert one failed: ");
            }
        });
    }

    private void insertMulti() {

        List<User> users = new ArrayList<>();

        User user = new User();
        user.setName("User 2");
        user.setAge(20);

        User user1 = new User();
        user1.setName("User 3");
        user1.setAge(30);

        users.add(user);
        users.add(user1);

        mMHDDBHelper.insert(users, new MHDDBCallBack() {
            @Override
            public void success() {
                Log.d(TAG, "insert multi success: ");
            }

            @Override
            public void failed(Throwable error) {
                Log.d(TAG, "insert multi failed: " + error);
            }
        });
    }

    private void findAll() {

        RealmQuery query = mMHDDBHelper.query(User.class);
        query.contains("name", "User");

        mMHDDBHelper.findAll(query, new MHDDBFindAllCallBack() {
            @Override
            public <E extends RealmObject> void result(final RealmResults<E> result) {
                Log.d(TAG, "find all result: \n" + result);
            }
        });
    }

    private void findFirst() {

        RealmQuery query = mMHDDBHelper.query(User.class);
        query.equalTo("name", "User 1");

        mMHDDBHelper.findFirst(query, new MHDDBFindCallBack() {
            @Override
            public void result(RealmModel object) {
                Log.d(TAG, "find first result: \n" + object);
            }
        });
    }

    private void updateAll() {

        RealmQuery query = mMHDDBHelper.query(User.class);
        query.contains("name", "User");

        mMHDDBHelper.updateAllAsync(User.class, query, new MHDDBFindAllCallBack() {
            @Override
            public <E extends RealmObject> void result(RealmResults<E> result) {
                for (E e : result) {
                    User user = (User) e;
                    user.setAge(500);
                }
            }
        }, new MHDDBCallBack() {
            @Override
            public void success() {
                Log.d(TAG, "update all success: ");
            }

            @Override
            public void failed(Throwable error) {
                Log.d(TAG, "update all failed: \n" + error);
            }
        });
    }

    private void updateFirst() {

        RealmQuery query = mMHDDBHelper.query(User.class);
        query.equalTo("name", "User 1");

        mMHDDBHelper.updateFirstAsync(User.class, query, new MHDDBFindCallBack() {
            @Override
            public void result(RealmModel object) {
                User user = (User) object;
                user.setAge(99);
            }
        }, new MHDDBCallBack() {
            @Override
            public void success() {
                Log.d(TAG, "update first success: ");
            }

            @Override
            public void failed(Throwable error) {
                Log.d(TAG, "update first failed: \n" + error);
            }
        });
    }

    private void deleteAll() {

        RealmQuery query = mMHDDBHelper.query(User.class);
        query.contains("name", "User");

        mMHDDBHelper.deleteAllAsync(User.class, query, new MHDDBCallBack() {
            @Override
            public void success() {
                Log.d(TAG, "delete all success: ");
            }

            @Override
            public void failed(Throwable error) {
                Log.d(TAG, "delete all failed: \n" + error);
            }
        });
    }

    private void deleteFirst() {

        RealmQuery query = mMHDDBHelper.query(User.class);
        query.equalTo("name", "User 1");

        mMHDDBHelper.deleteFirstAsync(User.class, query, new MHDDBCallBack() {
            @Override
            public void success() {
                Log.d(TAG, "delete first success: ");
            }

            @Override
            public void failed(Throwable error) {
                Log.d(TAG, "delete first failed: \n" + error);
            }
        });
    }



    ///////////////////////////////////////////////////////////////////////
    //                  Init methods
    ///////////////////////////////////////////////////////////////////////
    private void viewInit() {
        insertOne = findViewById(R.id.insert_one_btn);
        insertMulti = findViewById(R.id.insert_multi_btn);
        deleteFirst = findViewById(R.id.delete_first_btn);
        deleteAll = findViewById(R.id.delete_all_btn);
        updateFirst = findViewById(R.id.update_first_btn);
        updateAll = findViewById(R.id.update_all_btn);
        findFirst = findViewById(R.id.find_first_btn);
        findAll = findViewById(R.id.find_all_btn);
    }

    private void btnAction() {
        insertOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertOne();
            }
        });

        insertMulti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertMulti();
            }
        });

        deleteFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFirst();
            }
        });

        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAll();
            }
        });

        updateFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFirst();
            }
        });

        updateAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAll();
            }
        });

        findFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findFirst();
            }
        });

        findAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findAll();
            }
        });
    }
}
