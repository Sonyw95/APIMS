package com.customer.apims.dao;

public interface DatabaseDao {

    public Object excuteQuery(String namespace, String id, Object param); // 단일 ROW 쿼리 조회 INTERFACE
    public Object excuteQueryList(String namespace, String id , Object param ); // 복수 ROW 쿼리 조회 INTERFACE
    public int excuteInsert(String namespace, String id , Object param); // Insert 쿼리 INTERFACE
    public int excuteUpdate(String namespace, String id , Object param); // Update 쿼리 INTERFACE
    public int excuteDelete(String namespace, String id , Object param); // Delete 쿼리 INTERFACE
}
