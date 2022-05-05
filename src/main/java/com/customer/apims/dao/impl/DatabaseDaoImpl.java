package com.customer.apims.dao.impl;

import com.customer.apims.dao.DatabaseDao;
import org.apache.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class DatabaseDaoImpl implements DatabaseDao {

    @Resource
    SqlSessionTemplate sqlSessionTemplate; // myBatis

    Logger logger = Logger.getLogger(this.getClass());

    public DatabaseDaoImpl(){}

    //  class 의존존
    public DatabaseDaoImpl(SqlSessionTemplate sqlSessionTemplate){
        this.sqlSessionTemplate =sqlSessionTemplate;
    }

    @Override
    public Object excuteQuery(String namespace, String id, Object param) {
        return sqlSessionTemplate.selectOne(namespace + "." + id, param);
    }

    @Override
    public Object excuteQueryList(String namespace, String id, Object param) {
        return sqlSessionTemplate.selectList(namespace + "." + id, param);
    }

    @Override
    public int excuteInsert(String namespace, String id, Object param) {

        try{
            if(id.contains("insert")){
                return sqlSessionTemplate.update(namespace + "." + id, param);
            }else{
                String ERROR = "excuteUpdate  [Mapper 'id']  Not insert ID Contains !!";
                logger.error(ERROR);
                return Integer.parseInt(ERROR);
            }

        }catch (Exception e){

            logger.error(e.getMessage());
            return Integer.parseInt(e.getMessage());		//[2016.10.05 lej]
            //return 0;
        }

    }


    @Override
    public int excuteUpdate(String namespace, String id, Object param) {

        try{
            if(id.contains("update")){
                return sqlSessionTemplate.update(namespace + "." + id, param);
            }else{
                String ERROR = "excuteUpdate  [Mapper 'id']  Not Update ID Contains !!";
                logger.error(ERROR);
                return Integer.parseInt(ERROR);
            }

        }catch (Exception e){

            logger.error(e.getMessage());
            return Integer.parseInt(e.getMessage());		//[2016.10.05 lej]
            //return 0;
        }
    }

    @Override
    public int excuteDelete(String namespace, String id, Object param) {

        try{
            if(id.contains("delete")){
                return sqlSessionTemplate.update(namespace + "." + id, param);
            }else{
                String ERROR = "excuteUpdate  [Mapper 'id']  Not delete ID Contains !!";
                logger.error(ERROR);
                return Integer.parseInt(ERROR);
            }

        }catch (Exception e){

            logger.error(e.getMessage());
            return Integer.parseInt(e.getMessage());		//[2016.10.05 lej]
            //return 0;
        }

    }
}
