package com.pdy.common;

import java.util.List;

public class StringUtil {
    public static String join(String join,List<String> strAry){
        StringBuffer sb=new StringBuffer();
        int count=0;
        for (String key : strAry) {
        	 if(count==0){
                 sb.append(key);
             }else{
                 sb.append(join).append(key);
             }
        	count++;
        }
        
        return new String(sb);
    }
 
  
}

