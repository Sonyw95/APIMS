package com.customer.apims.util;

import com.customer.apims.exception.HttpConnectionException;
import org.apache.log4j.Logger;
import org.ezdevgroup.ezframework.support.wrapper.http.EzHttpClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;


public class CallConnection {

    Logger logger = Logger.getLogger(this.getClass());

    /**
     *  API CONNECTION SERVICE UTIL
     * @param paramurl
     * @param dataParam
     * @thorws IOException, Exception
     */

    public int callConnectionService(String paramurl, Map<String, Object> dataParam) throws UnsupportedEncodingException, Exception {
        logger.debug(" Start Connection Service ");

        EzHttpClient httpClient = new EzHttpClient();

        StringBuffer parameter = new StringBuffer();
        // Reflection
        if(dataParam != null){
            Iterator<Map.Entry<String, Object>> it = dataParam.entrySet().iterator();

            while(it.hasNext()){
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>)it.next();
                parameter.append("&").append(URLEncoder.encode(entry.getKey(), "UTF-8"))
                        .append("=").append(URLEncoder.encode((String)(entry.getValue()==null?"":entry.getValue()), "UTF-8"));
            }
        }

        String url = paramurl + parameter.toString();
        logger.debug(url);

        httpClient.setUrl(url);
        httpClient.setMothod(EzHttpClient.POST);
        httpClient.setDataType(EzHttpClient.CONTENT_JSON);

        int resCode = httpClient.send();

        if(resCode != 200){
            throw new HttpConnectionException("Not able 200");
        }

        return resCode;


    }
}
