package org.example.reflection;

import org.example.dz.DZ;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;

class ReflectionDZ {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        System.out.println("Начинаю рефлексию");
//        Class aclass = DZ.class;

//        DZ dz = new DZ();
//        Class aclass = dz.getClass();

        Class aclass = Class.forName("org.example.dz.DZ");

        System.out.println(aclass.getName());

        Object[] argsForReflection; // Переменная для аргументов обращения к функции

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
        Class<?>[] ifs = DZ.class.getInterfaces();

        Field fld = DZ.class.getDeclaredField("s");

        // Получаем класс подходящего конструктора
        Constructor constructor = DZ.class.getDeclaredConstructor(int.class, String.class); // DZ(int i, String s)
        constructor.setAccessible(true); // Делаем приватный конструктор доступным
        System.out.println("Конструктор " + constructor.toGenericString());
        // С помощью подходящего конструктора и newInstance создаём два объекта DZ
        DZ builder = (DZ) constructor.newInstance(1, "string");
        DZ builder2 = (DZ) constructor.newInstance(2, "string");

//        System.out.println("Object builder class " + builder.getClass().getName());

        // Берём ссылку на метод
        Method getSumInteger = DZ.class.getDeclaredMethod("getSumInteger", new Class[]{DZ.class, DZ.class});
        getSumInteger.setAccessible(true); // Делаем метод доступным
        int sumInteger = (Integer) getSumInteger.invoke(builder, builder, builder2); // Отрабатываем метод
        System.out.println("getSumInteger " + sumInteger);

        // Изменение в одном из объектов приватного поля
        //Field iRefl = builder2.getClass().getDeclaredField("i");
        Field iRefl = DZ.class.getDeclaredField("i");
        iRefl.setAccessible(true); // Делам поле доступным
        System.out.println("builder старое значенние i " + iRefl.getInt(builder) ) ;
        iRefl.setInt(builder, 47);
        System.out.println("bulder новое значенние i " + iRefl.getInt(builder)) ;
        sumInteger = (Integer) getSumInteger.invoke(builder, builder, builder2); // Отрабатываем метод
        System.out.println("Повторно getSumInteger " + sumInteger);

        // Исполняем метод setI у builder2 потом ещё раз отрабатываем метод getSumInteger
        // Берём ссылку на метод
        Method setI = DZ.class.getDeclaredMethod("setI", new Class[]{int.class});
        setI.setAccessible(true); // Делаем метод доступным
        System.out.println("bulder2 старое значенние i " + iRefl.getInt(builder2)) ;
        setI.invoke(builder2, 15); // Отрабатываем метод
        System.out.println("bulder2 новое значенние i " + iRefl.getInt(builder2)) ;
        argsForReflection = new Object[] {builder, builder2};
        //sumInteger = (Integer) getSumInteger.invoke(builder, builder, builder2); // Отрабатываем метод
        sumInteger = (Integer) getSumInteger.invoke(builder, argsForReflection); // Отрабатываем метод
        System.out.println("Повторно-повторно getSumInteger " + sumInteger);

        // Получаем класс подходящего конструктора
        Constructor constructor2 = DZ.class.getConstructor(int.class, String.class, List.class); // DZ_Solution(Integer i, String s, List<Double> list)

        // Через конструктор пару объектов
        DZ builder3 = (DZ)constructor2.newInstance(1, "string", Arrays.asList(1.2, 45.6, 33, 9));
        DZ builder4 = (DZ)constructor2.newInstance(1, "string", Arrays.asList(1.2, 45.6, 33, 9));

        argsForReflection = new Object[] {builder3, builder4};

        Method getSumFromList = DZ.class.getDeclaredMethod("getSumFromList", new Class[]{DZ.class, DZ.class});
        getSumFromList.setAccessible(true); // Делаем метод доступным
//        var sumList = (List<Double>) getSumFromList.invoke(builder, builder3, builder4); // Отрабатываем метод в переменную оригинального типа
        var sumList = (List<Double>) getSumFromList.invoke(builder, argsForReflection); // Отрабатываем метод в переменную оригинального типа
        System.out.println("getSumFromList " + sumList);
    }
}
