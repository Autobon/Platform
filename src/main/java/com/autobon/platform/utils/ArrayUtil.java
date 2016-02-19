package com.autobon.platform.utils;

import org.springframework.stereotype.Component;

/**
 * Created by yuh on 2016/2/17.
 */
@Component
public class ArrayUtil {

    public String arrayToString(String[] array){
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<array.length;i++){
            if(i==array.length-1){
                sb.append(array[i]);
            }else{
                sb.append(array[i]).append(",");
            }
        }
        return sb.toString();
    }

}
