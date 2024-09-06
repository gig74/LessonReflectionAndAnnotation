package org.example.annotation;

import org.example.dz.DZ;

import java.lang.reflect.Method;

public class CommandListener
{
    @Command(name = "привет", args = "", desc = "Будь культурным, поздоровайся",
            showInHelp = false, aliases = {"здаров","добрый день"})
    public void hello(String[] args)
    {
        try {
            Method helloVoid = this.getClass().getDeclaredMethod("hello", new Class[]{String[].class});
            Command command = helloVoid.getAnnotation(Command.class);
            String s = command.name(); // привет
            System.out.println(s);
            String[] s2 = command.aliases(); // привет
            for (String s3:s2) {
                System.out.println(s3);
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        //Какой-то функционал, на Ваше усмотрение.
    }

    @Command(name = "пока", args = "", desc = "Прощается", aliases = {"удачи"})
    public void bie(String[] args)
    {
        // Функционал
//        Method[] methods = this.getClass().getDeclaredMethods();  // Все методы, включая приватные (насчёт родительских - неизвестно, надо уточнять)
        try {
            Method helloVoid = this.getClass().getDeclaredMethod("bie", new Class[]{String[].class});
            Command command = helloVoid.getAnnotation(Command.class);
            String s = command.name(); // привет
            System.out.println(s);
            String[] s2 = command.aliases(); // привет
            for (String s3:s2) {
                System.out.println(s3);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Command(name = "помощь", args = "", desc = "Выводит список команд", aliases = {"help", "команды"})
    public void help(String[] args)
    {
        StringBuilder sb = new StringBuilder("Список команд: \n");
        Method[] methods = this.getClass().getDeclaredMethods();
        for (Method m : this.getClass().getDeclaredMethods())
        {
            if (m.isAnnotationPresent(Command.class))
            {
                Command com = m.getAnnotation(Command.class);
                if (com.showInHelp()) //Если нужно показывать команду в списке.
                {
                    sb.append("Бот, ").
                            append(com.name()).
                            append(" ").
                            append(com.args()).
                            append(" - ").
                            append(com.desc()).
                            append("\n");
                }
            }
        }
        //Отправка sb.toString();
        System.out.println("sb " + sb.toString());
    }
}
