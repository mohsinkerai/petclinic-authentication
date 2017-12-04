package com.system.demo.bulk.volunteer.job.elements;

import org.springframework.stereotype.Component;

import java.util.Map;


@Component
public class HeaderConvertor {


    String[] formNo = {"s.no","S.no","S.No","S.NO","s.no.","S.no.","S.No.","S.NO.,"};

    public String getKeyForForm(Map<String,String> dataMap){

        for(String key :formNo){
            if (dataMap.containsKey("key")) {
            return dataMap.get(key);
            }
        }
        return "";
    }
}
