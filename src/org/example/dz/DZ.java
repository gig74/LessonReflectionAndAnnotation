package org.example.dz;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Напишите класс который бы на основе рефлексии создавал бы новые экземпляры DZ, обращался бы к их методам
 * getSumInteger и  getSumFromList
 * и выводил результаты в консоль
 */

public class DZ {
    private int i;
    private String s;
    private List<Double> list = new ArrayList<>();

    public DZ() {
    }

    private DZ(int i, String s) {
        this.i = i;
        this.s = s;
    }

    public DZ(int i, String s, List<Double> list) {
        this.i = i;
        this.s = s;
        this.list = list;
    }

    public int getI() {
        return i;
    }

    private void setI(int i) {
        this.i = i;
    }

    public String getS() {
        return s;
    }

    private void setS(String s) {
        this.s = s;
    }

    public List<Double> getList() {
        return list;
    }

    public void setList(List<Double> list) {
        this.list = list;
    }

    private int getSumInteger(DZ dz1, DZ dz2) {
        return dz1.getI() + dz2.getI();
    }

    private List<Double> getSumFromList(DZ dz1, DZ dz2) {
        return Stream.concat(dz1.getList().stream(), dz2.getList().stream()).collect(Collectors.toList());
    }

}

class ReflectionDZ {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        System.out.println("Начинаю рефлексию");
        Class aclass = DZ.class;
//        DZ dz = new DZ();
//        Class aclass = dz.getClass();

//        Class dz = Class.forName("org.example.dz.DZ");

        System.out.println(aclass.getName());
//
//        Class cls = dz.getClass();
        int mods = DZ.class.getModifiers();
        if (Modifier.isPublic(mods)) {
            System.out.println("public");
        }
        if (Modifier.isAbstract(mods)) {
            System.out.println("abstract");
        }
        if (Modifier.isFinal(mods)) {
            System.out.println("final");
        }

//        Class<?>    clsArray = dz.getClass();
//        Constructor[] constructors = clsArray.getConstructors();
//        int numConstructor = 0;
//        for (Constructor constructor : constructors) {
//            numConstructor++;
//            System.out.println("Constructor " + numConstructor);
//            Class<?>[] params = constructor.getParameterTypes();
//            for (Class<?> param : params) {
//                System.out.println(param.getName());
//            }
//        }


        // Получение информации
        Constructor[] constructors = DZ.class.getDeclaredConstructors(); // Все конструкторы
        Field[] fields = DZ.class.getDeclaredFields(); // Все поля непосредсвенно класса (поля родительских классов надо запрашивать олтдельно)
        Method[] methods = DZ.class.getDeclaredMethods();  // Все методы, включая приватные (насчёт родительских - неизвестно, надо уточнять)

        Field fld = DZ.class.getDeclaredField("s");

        // Получаем класс подходящего конструктора
        Constructor constructor = DZ.class.getDeclaredConstructor(int.class, String.class); // DZ(int i, String s)
        constructor.setAccessible(true); // Делаем приватный конструктор доступным
        System.out.println("Конструктор " + constructor.toGenericString());
        // С помощью подходящего конструктора и newInstance создаём два объекта DZ
        DZ builder = (DZ) constructor.newInstance(1, "string");
        DZ builder2 = (DZ) constructor.newInstance(2, "string");

        System.out.println("Object builder class " + builder.getClass().getName());

        // Берём ссылку на метод
        Method getSumInteger = DZ.class.getDeclaredMethod("getSumInteger", new Class[]{DZ.class, DZ.class});
        getSumInteger.setAccessible(true); // Делаем метод доступным
        int sumInteger = (Integer) getSumInteger.invoke(builder, builder, builder2); // Отрабатываем метод
        System.out.println("getSumInteger " + sumInteger);

        // Изменение в одном из объектов приватного поля
        //Field iRefl = builder2.getClass().getDeclaredField("i");
        Field iRefl = DZ.class.getDeclaredField("i");
        iRefl.setAccessible(true); // Делам поле доступным
        System.out.println("старое значенние i " + iRefl.getInt(builder) ) ;
        iRefl.setInt(builder, 47);
        sumInteger = (Integer) getSumInteger.invoke(builder, builder, builder2); // Отрабатываем метод
        System.out.println("Повторно getSumInteger " + sumInteger);

        // Исполняем метод setI у builder2 потом ещё раз отрабатываем метод getSumInteger
        // Берём ссылку на метод
        Method setI = DZ.class.getDeclaredMethod("setI", new Class[]{int.class});
        setI.setAccessible(true); // Делаем метод доступным
        System.out.println("bulder2 старое значенние i " + iRefl.getInt(builder2)) ;
        setI.invoke(builder2, 15); // Отрабатываем метод
        System.out.println("bulder2 новое значенние i " + iRefl.getInt(builder2)) ;
        sumInteger = (Integer) getSumInteger.invoke(builder, builder, builder2); // Отрабатываем метод
        System.out.println("Повторно-повторно getSumInteger " + sumInteger);

        // Получаем класс подходящего конструктора
        Constructor constructor2 = DZ.class.getConstructor(int.class, String.class, List.class); // DZ_Solution(Integer i, String s, List<Double> list)

        // Через конструктор пару объектов
        DZ builder3 = (DZ)constructor2.newInstance(1, "string", Arrays.asList(1.2, 45.6, 33, 9));
        DZ builder4 = (DZ)constructor2.newInstance(1, "string", Arrays.asList(1.2, 45.6, 33, 9));

        Method getSumFromList = DZ.class.getDeclaredMethod("getSumFromList", new Class[]{DZ.class, DZ.class});
        getSumFromList.setAccessible(true); // Делаем метод доступным
        var sumList = (List<Double>) getSumFromList.invoke(builder, builder3, builder4); // Отрабатываем метод в переменную оригинального типа
        System.out.println("getSumInteger " + sumList);
    }
}