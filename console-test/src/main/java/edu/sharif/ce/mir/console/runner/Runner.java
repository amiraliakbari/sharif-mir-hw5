package edu.sharif.ce.mir.console.runner;

import edu.sharif.ce.mir.console.Console;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (3/3/12, 1:57)
 */
public class Runner {

    public static void main(String[] args) {
        final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring/context.xml");
        final Console console = context.getBean("console", Console.class);
        console.run();
    }

}
