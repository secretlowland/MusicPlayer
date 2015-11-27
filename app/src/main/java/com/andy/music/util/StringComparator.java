package com.andy.music.util;

import android.util.Log;

import java.util.Comparator;

/**
 * 通过首字母比较字符串
 * 此类中非字母类字符排在后面
 * Created by andy on 2015/11/21.
 */
public class StringComparator implements Comparator {
    @Override
    public int compare(Object lhs, Object rhs) {
        if (lhs==null && rhs==null) {
            return 0;
        }
        if (lhs==null && rhs!=null) {
            return 1;
        }
        if (lhs!=null && rhs==null) {
            return -1;
        }

        String ls = CharacterParser.getInstance().getSelling(lhs.toString());
        String rs = CharacterParser.getInstance().getSelling(rhs.toString());

        if (ls==null && rs==null) {
            return 0;
        }
        if (ls==null && rs!=null) {
            return 1;
        }
        if (ls!=null && rs==null) {
            return -1;
        }

        int llen = ls.length();
        int rlen = rs.length();

        if( llen<=0 || rlen<=0) {
            return llen - rlen;
        }

        char lc = Character.toUpperCase(ls.charAt(0));
        char rc = Character.toUpperCase(rs.charAt(0));

        if (lc >='A' && lc <='Z' && (rc<'A' || rc > 'Z')) {
            return -1;
        }
        if (rc >='A' && rc <='Z' && (lc<'A' || lc > 'Z')) {
            return 1;
        }
        return lc - rc;
    }
}
