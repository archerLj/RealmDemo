package com.example.realmdemo.db.impl;

import android.content.Context;

import com.example.realmdemo.db.idb.MHDDB;
import com.example.realmdemo.db.idb.MHDDBCallBack;
import com.example.realmdemo.db.idb.MHDDBFindAllCallBack;
import com.example.realmdemo.db.idb.MHDDBFindCallBack;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import io.realm.Realm;
import io.realm.RealmAsyncTask;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by archerlj on 2017/12/12.
 */
public class MHDDBHelper implements MHDDB {

    private Realm mRealm;
    List<RealmAsyncTask> transactions = new ArrayList<>();

    public MHDDBHelper() {
        super();
        mRealm = Realm.getDefaultInstance();
    }


    ///////////////////////////////////////////////////////////////////////
    //                     class methods
    ///////////////////////////////////////////////////////////////////////
    public static void realmInit(Context context) {
        Realm.init(context);

        // 设置默认的configuration，这样后面所有的Realm.getdefaultInstance()获取的对象都对应这个configuration.
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name("myRealm.realm")
                .build();
        Realm.setDefaultConfiguration(configuration);
    }


    ///////////////////////////////////////////////////////////////////////
    //                     public methods
    ///////////////////////////////////////////////////////////////////////
    @Override
    public <E extends RealmObject> RealmQuery query(Class<E> clazz) {
        return mRealm.where(clazz);
    }


    // -----------------------------------------------------------------
    // MARK - 插入

    @Override
    public <E extends RealmObject> void insert(final E object,
                                               @Nullable final MHDDBCallBack callBack) {

        List<E> objects = new ArrayList<>();
        objects.add((E)object);
        insertCommon(objects, callBack);
    }


    @Override
    public <E extends RealmObject> void insertOrUpdate(final E object,
                                                       @Nullable final MHDDBCallBack callBack) {

        List<E> objects = new ArrayList<>();
        objects.add(object);
        insertOrUpdateCommn(objects, callBack);
    }

    @Override
    public <E extends RealmObject> void insert(final List<E> objects,
                                               @Nullable final MHDDBCallBack callBack) {

        insertCommon(objects, callBack);
    }

    @Override
    public <E extends RealmObject> void insertOrUpdate(final List<E> objects,
                                                       @Nullable final MHDDBCallBack callBack) {

        insertOrUpdateCommn(objects, callBack);
    }


    // -----------------------------------------------------------------
    // MARK - 查询
    @Override
    public <E extends RealmObject> void findFirst(RealmQuery query,
                                                  final MHDDBFindCallBack callBack) {

        E result = (E) query.findFirstAsync();
        result.addChangeListener(new RealmChangeListener<RealmModel>() {
            @Override
            public void onChange(RealmModel realmModel) {
                if (callBack != null) {
                    callBack.result(realmModel);
                }
            }
        });
    }

    @Override
    public <E extends RealmObject> void findAll(RealmQuery query,
                                                final MHDDBFindAllCallBack callBack) {

        RealmResults<E> results = query.findAllAsync();
        results.addChangeListener(new RealmChangeListener<RealmResults<E>>() {
            @Override
            public void onChange(RealmResults<E> es) {
                if (callBack != null) {
                    callBack.result(es);
                }
            }
        });
    }


    // -----------------------------------------------------------------
    // MARK - 更新
    @Override
    public <E extends RealmObject> void updateFirst(Class<E> clazz,
                                                    final RealmQuery query,
                                                    final MHDDBFindCallBack callBack) {

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (callBack != null) {
                    E object = (E) query.findFirst();
                    callBack.result(object);
                }
            }
        });
    }

    @Override
    public <E extends RealmObject> void updateFirstAsync(Class<E> clazz,
                                                         final RealmQuery query,
                                                         final MHDDBFindCallBack callBack,
                                                         @Nullable final MHDDBCallBack callBackb) {

        // 由于创建query的Realm与下面execute回调方法不在同一个线程，所以在execute回调中使用query会崩溃。
        // 这里先把query中的realm清空
        replacePropertyValue(query, "realm", null);


        RealmAsyncTask transaction = mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (callBack != null) {

                    // 把query中的realm替换为当前线程的传过来realm变量。
                    replacePropertyValue(query, "realm", realm);

                    E object = (E) query.findFirst();
                    callBack.result(object);
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                if (callBackb != null) {
                    callBackb.success();
                }
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                if (callBackb != null) {
                    callBackb.failed(error);
                }
            }
        });

        transactions.add(transaction);
    }


    @Override
    public <E extends RealmObject> void updateAll(Class<E> clazz,
                                                  final RealmQuery query,
                                                  final MHDDBFindAllCallBack callBack) {

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (callBack != null) {
                    RealmResults<E> objects = query.findAll();
                    callBack.result(objects);
                }
            }
        });
    }


    @Override
    public <E extends RealmObject> void updateAllAsync(Class<E> clazz,
                                                       final RealmQuery query,
                                                       final MHDDBFindAllCallBack callBack,
                                                       @Nullable final MHDDBCallBack callBackb) {

        replacePropertyValue(query, "realm", null);

        RealmAsyncTask transaction = mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                if (callBack != null) {
                    replacePropertyValue(query, "realm", realm);
                    RealmResults<E> objects = query.findAll();
                    callBack.result(objects);
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                if (callBackb != null) {
                    callBackb.success();
                }
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                if (callBackb != null) {
                    callBackb.failed(error);
                }
            }
        });

        transactions.add(transaction);
    }

    // -----------------------------------------------------------------
    // MARK - 删除
    @Override
    public <E extends RealmObject> void deleteFirst(Class<E> clazz,
                                               final RealmQuery query) {

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                E object = (E) query.findFirst();
                object.deleteFromRealm();
            }
        });
    }

    @Override
    public <E extends RealmObject> void deleteFirstAsync(Class<E> clazz, final  RealmQuery query, final MHDDBCallBack callBack) {

        replacePropertyValue(query, "realm", null);

        RealmAsyncTask transaction = mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                if (callBack != null) {
                    replacePropertyValue(query, "realm", realm);
                    E object = (E) query.findFirst();
                    object.deleteFromRealm();
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                if (callBack != null) {
                    callBack.success();
                }
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                if (callBack != null) {
                    callBack.failed(error);
                }
            }
        });

        transactions.add(transaction);
    }

    @Override
    public <E extends RealmObject> void deleteAll(Class<E> clazz,
                                                  final RealmQuery query) {

        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<E> objects = query.findAll();
                for (E object : objects) {
                    object.deleteFromRealm();
                }
            }
        });
    }

    @Override
    public <E extends RealmObject> void deleteAllAsync(Class<E> clazz, final RealmQuery query, final MHDDBCallBack callBack) {

        replacePropertyValue(query, "realm", null);

        RealmAsyncTask transaction = mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                replacePropertyValue(query, "realm", realm);
                RealmResults<E> objects = query.findAll();

                for (E object : objects) {
                    object.deleteFromRealm();
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                if (callBack != null) {
                    callBack.success();
                }
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                if (callBack != null) {
                    callBack.failed(error);
                }
            }
        });

        transactions.add(transaction);
    }

    // -----------------------------------------------------------------
    // MARK - 关闭Realm，清空transactions
    @Override
    public void clear() {
        for (RealmAsyncTask transaction : transactions) {
            transaction.cancel();
        }
        mRealm.close();
    }

    ///////////////////////////////////////////////////////////////////////
    //                     private methods
    ///////////////////////////////////////////////////////////////////////
    private <E extends RealmObject> void insertCommon(final List<E> objects,
                                                      @Nullable final MHDDBCallBack callBack) {

        RealmAsyncTask transaction = mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (E object : objects) {
                    realm.copyToRealm(object);
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                if (callBack != null) {
                    callBack.success();
                }
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                if (callBack != null) {
                    callBack.failed(error);
                }
            }
        });

        transactions.add(transaction);
    }

    private  <E extends RealmObject> void insertOrUpdateCommn(final List<E> objects,
                                                              @Nullable final MHDDBCallBack callBack) {

        RealmAsyncTask transaction = mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (E object : objects) {
                    realm.copyToRealmOrUpdate(object);
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                if (callBack != null) {
                    callBack.success();
                }
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                if (callBack != null) {
                    callBack.failed(error);
                }
            }
        });

        transactions.add(transaction);
    }


    
    ///////////////////////////////////////////////////////////////////////
    //                     Tool methods
    ///////////////////////////////////////////////////////////////////////
    // 对象属性值修改
    private void replacePropertyValue(Object object, String propertyName, Object newValue) {

        try {
            Field field = object.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            field.set(object, newValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
