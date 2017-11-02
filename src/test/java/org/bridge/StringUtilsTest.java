package org.bridge;

import org.junit.Test;

public class StringUtilsTest {
    @Test
    public void arrayInsertArrayTest(){
        System.out.println("/**********1***********/");
        String str1[] = {"1","2","6","7"};
        String str2[] = {"3","4","5"};
        String str3[] = StringUtils.arrayInsertArray(str1, 2, str2);
        for(String s:str3) {
            System.out.print(s+"\t");
        }
        System.out.println();
        str3 = StringUtils.arrayInsertArray(str1, 10, str2);
        for(String s:str3) {
            System.out.print(s+"\t");
        }
    }

    @Test
    public void isNullOrEmptyTest(){
        System.out.println("/**********2***********/");
        String s1 = null;
        System.out.print(StringUtils.isNullOrEmpty(s1)+"\n");
    }

    @Test
    public void ignoreCaseReplaceTest(){
        System.out.println("/**********3***********/");
        String s1 = null;
        String s2 = null;
        String s3 = null;
        s1="a b c d ";
        s2=" ";
        s3=",";
        System.out.print(StringUtils.ignoreCaseReplace(s1, s2, s3)+"\n");
    }

}
