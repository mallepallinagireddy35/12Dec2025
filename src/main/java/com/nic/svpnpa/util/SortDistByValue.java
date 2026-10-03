package com.nic.svpnpa.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SortDistByValue {

	public static Map<String, String> getSortedMap(Map<String, String> dlist) {
		List<Map.Entry<String, String>> list
        = new LinkedList<Map.Entry<String, String>>(dlist.entrySet());

            // Sort the list 
            Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
                public int compare(Map.Entry<String, String> o1,
                        Map.Entry<String, String> o2) {
                    return (o1.getValue()).compareTo(o2.getValue());
                }
            });
             HashMap<String, String> distlist = new LinkedHashMap<String, String>(); 
        for (Map.Entry<String, String> aa : list) { 
            distlist.put(aa.getKey(), aa.getValue()); 
        } 
        return distlist;
    }
	
}
