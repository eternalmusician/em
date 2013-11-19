package com.run4coloncancer.Eternal_Musician;

import java.util.*;

public class ArrayHandle {
    public static Object[] reverse(Object[] arr) {
        List < Object > list = Arrays.asList(arr);
        Collections.reverse(list);
        return list.toArray();
    }

}