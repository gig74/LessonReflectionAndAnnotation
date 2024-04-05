package org.example.reflection;

import java.lang.reflect.Field;

public class ModifingPrivateFields {
    public static void main(String[] args) throws Exception {
        WithPrivateFinalField pf = new WithPrivateFinalField();

        System.out.println(pf);

        Field f = pf.getClass().getDeclaredField("i");
        f.setAccessible(true);
        f.setInt(pf,47);
        System.out.println(pf);

        f = pf.getClass().getDeclaredField("s");
        f.setAccessible(true);
        f.set(pf,"MODIFY S");
        System.out.println(pf);

        f = pf.getClass().getDeclaredField("s2");
        f.setAccessible(true);
        f.set(pf,"MODIFY S2");
        System.out.println(pf);
    }

    static class WithPrivateFinalField{
        private int i = 1;
        private final String s = "String final";
        private String s2 = "String private";

        public String toString(){
            return i + " - " + s + " - " + s2;
        }
    }
}
