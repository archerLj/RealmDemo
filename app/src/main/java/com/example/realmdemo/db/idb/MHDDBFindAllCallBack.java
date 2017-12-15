package com.example.realmdemo.db.idb;

import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by archerlj on 2017/12/13.
 */

public interface MHDDBFindAllCallBack {
    <E extends RealmObject> void result(RealmResults<E> result);
}
