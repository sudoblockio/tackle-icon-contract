package io.contractdeployer.generics.irc3.utils;

import score.ArrayDB;

import java.util.ArrayList;
import java.util.List;

public class ArrayUtil {

    public static <T> boolean containsInArrayDb(T value, ArrayDB<T> arraydb) {
        if (arraydb == null || value == null) {
            return false;
        }
        boolean found = false;
        for (int i = 0; i < arraydb.size(); i++) {
            if (arraydb.get(i) != null
                    && arraydb.get(i).equals(value)) {
                found = true;
                break;
            }
        }
        return found;
    }

    public static <T> void removeFromArrayDB(T _item, ArrayDB<T> _array) {
        final int size = _array.size();
        if (size < 1) {
            return;
        }
        T top = _array.get(size - 1);
        for (int i = 0; i < size; i++) {
            if (_array.get(i).equals(_item)) {
                _array.set(i, top);
                _array.pop();
                return;
            }
        }

    }


    public static <T> void toArrayDB(T[] inArr, ArrayDB<T> arrayDB) {
        for (T t : inArr) {
            arrayDB.add(t);
        }
    }

    public static <T> List<T> toArray(ArrayDB<T> arrayDB) {
        int size = arrayDB.size();
        List<T> list = new ArrayList<T>();
        for(int i=0; i<size; i++){
            list.add(arrayDB.get(i));
        }
        return list;
    }

}
