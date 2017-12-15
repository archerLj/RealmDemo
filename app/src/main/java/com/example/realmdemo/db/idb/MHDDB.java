package com.example.realmdemo.db.idb;

import java.util.List;

import javax.annotation.Nullable;

import io.realm.RealmObject;
import io.realm.RealmQuery;

/**
 * Created by archerlj on 2017/12/12.
 */

public interface MHDDB {

    /**
     * 插入 - 一条数据
     *
     * object中未配置主键的时候调用该方法，如配有主键，请调用insertOrUpdate方法，
     *
     * 注意：未防止在回调中更新UI导致app崩溃，请在Activity或Fragment销毁的时候调用RealmAsyncTask的cancel()方法取消后续处理
     *
     * @param object
     * @param callBack
     * @param <E>
     * @return
     */
     <E extends RealmObject> void insert(E object,
                                         MHDDBCallBack callBack);


    /**
     * 插入或者更新 - 一条数据
     *
     * 如果object已经在Realm中存在，则更新该object, 否则在Realm中插入
     * object配置了主键的时候调用该方法，如没有配置主键，请调用insert方法，否则会报IllegalArgumentException.
     *
     * 注意： 未防止在回调中更新UI导致app崩溃，请在Activity或Fragment销毁的时候调用RealmAsyncTask的cancel()方法取消后续处理
     *
     * @param object
     * @param <E>
     */
    <E extends RealmObject>  void insertOrUpdate(E object,
                                                 MHDDBCallBack callBack);



    /**
     * 插入 - 多条数据
     *
     * object中未配置主键的时候调用该方法，如配有主键，请调用insertOrUpdate方法，
     *
     * 注意：未防止在回调中更新UI导致app崩溃，请在Activity或Fragment销毁的时候调用RealmAsyncTask的cancel()方法取消后续处理
     *
     * @param objects
     * @param callBack
     * @param <E>
     * @return
     */
    <E extends RealmObject>  void insert(List<E> objects,
                                         MHDDBCallBack callBack);


    /**
     * 插入或者更新 - 多条数据
     *
     * 如果object已经在Realm中存在，则更新该object, 否则在Realm中插入
     * object配置了主键的时候调用该方法，如没有配置主键，请调用insert方法，否则会报IllegalArgumentException.
     *
     * 注意： 未防止在回调中更新UI导致app崩溃，请在Activity或Fragment销毁的时候调用RealmAsyncTask的cancel()方法取消后续处理
     *
     * @param objects
     * @param <E>
     */
    <E extends RealmObject>  void insertOrUpdate(List<E> objects,
                                                 MHDDBCallBack callBack);


    /**
     * 获取query对象
     *
     * 获取query对象后，可自由组装查询条件
     *
     * @param clazz
     * @param <E>
     * @return
     */
    <E extends RealmObject> RealmQuery query(Class<E> clazz);


    /**
     * 查询-第一条数据
     *
     * @param query
     * @param callBack
     */
    <E extends RealmObject>  void findFirst(RealmQuery query,
                                            MHDDBFindCallBack callBack);


    /**
     * 查询-所有数据
     *
     * @param query
     * @param callBack
     * @param <E>
     */
    <E extends RealmObject>  void findAll(RealmQuery query,
                                          MHDDBFindAllCallBack callBack);


    /**
     * 更新-一条数据
     *
     * 同步方法
     *
     * @param clazz
     * @param query
     * @param callBack
     * @param <E>
     */
    <E extends RealmObject>  void updateFirst(Class<E> clazz,
                                              RealmQuery query,
                                              MHDDBFindCallBack callBack);


    /**
     * 更新-一条数据
     *
     * 异步方法
     *
     * @param clazz
     * @param query
     * @param callBack
     * @param <E>
     */
    <E extends RealmObject> void updateFirstAsync(Class<E> clazz,
                                                  RealmQuery query,
                                                  MHDDBFindCallBack callBack,
                                                  @Nullable MHDDBCallBack callBackb);


    /**
     * 更新-多条数据
     *
     * 同步方法
     *
     * @param clazz
     * @param query
     * @param callBack
     * @param <E>
     */
    <E extends RealmObject>  void updateAll(Class<E> clazz,
                                            RealmQuery query,
                                            MHDDBFindAllCallBack callBack);


    /**
     * 更新-多条数据
     *
     * 异步方法
     *
     * @param clazz
     * @param query
     * @param callBack
     * @param <E>
     */
    <E extends RealmObject> void updateAllAsync(Class<E> clazz,
                                                RealmQuery query,
                                                MHDDBFindAllCallBack callBack,
                                                @Nullable MHDDBCallBack callBackb);


    /**
     * 删除 - 删除第一条数据
     *
     * 同步方法
     *
     * @param clazz
     * @param query
     * @param <E>
     */
    <E extends RealmObject>  void deleteFirst(Class<E> clazz,
                                              RealmQuery query);


    /**
     * 删除 - 删除一条数据
     *
     * 异步方法
     *
     * @param clazz
     * @param query
     * @param <E>
     */
    <E extends RealmObject> void deleteFirstAsync(Class<E> clazz,
                                                  RealmQuery query,
                                                  MHDDBCallBack callBack);


    /**
     *
     * 删除 - 删除所有数据
     *
     * 同步方法
     *
     * @param clazz
     * @param query
     * @param <E>
     */
    <E extends RealmObject>  void deleteAll(Class<E> clazz,
                                            RealmQuery query);


    /**
     *
     * 删除 - 删除所有数据
     *
     * 异步方法
     *
     * @param clazz
     * @param query
     * @param <E>
     */
    <E extends RealmObject>  void deleteAllAsync(Class<E> clazz,
                                                 RealmQuery query,
                                                 MHDDBCallBack callBack);


    /**
     * 清理
     *
     * 在Activity或Fragment销毁的时候调用clear()方法释放数据库资源
     */
    void clear();
}