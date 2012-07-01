package edu.sharif.ce.mir.console;

import edu.sharif.ce.mir.console.command.definition.impl.DefaultCommandDefinitionParser;
import edu.sharif.ce.mir.console.command.definition.parse.CommandParseTree;
import edu.sharif.ce.mir.console.command.definition.parse.RootCommandParseTree;
import edu.sharif.ce.mir.console.command.matcher.CommandMatcher;
import edu.sharif.ce.mir.console.command.matcher.ConsoleCommand;
import edu.sharif.ce.mir.console.command.matcher.impl.DefaultCommandMatcher;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (29/2/12, 12:08)
 */
public class CommandMatcherTest {

    @Test
    public void matchCommandWithoutOptionalPartsTest() throws Exception {
        final DefaultCommandDefinitionParser parser = new DefaultCommandDefinitionParser();
        final String definition = "set #string(variable) to #integer(value)";
        System.out.println("definition = " + definition);
        final CommandParseTree tree = parser.parse(definition);
        CommandMatcher matcher = new DefaultCommandMatcher();
        final String str = "set m to 1";
        System.out.println(((RootCommandParseTree) tree).getFlatString());
        ConsoleCommand command = matcher.match(tree, str);
        System.out.println("command = " + command);
        Assert.assertNotNull(command);
        Assert.assertEquals(1, command.getGroups().size());
        Assert.assertEquals(2, command.getParameters().size());
        Assert.assertTrue(command.getGroups().containsKey("#root"));
        Assert.assertEquals(str, command.getGroups().get("#root"));
        Assert.assertTrue(command.getParameters().containsKey("variable"));
        Assert.assertEquals("m", command.getParameters().get("variable"));
        Assert.assertTrue(command.getParameters().containsKey("value"));
        Assert.assertEquals(1, command.getParameters().get("value"));
    }

    @Test
    public void matchCommandWithOneOptionalPartTest() throws Exception {
        final DefaultCommandDefinitionParser parser = new DefaultCommandDefinitionParser();
        final String definition = "set #string(variable) to #integer(value) [condition}if #boolean]";
        System.out.println("definition = " + definition);
        final CommandParseTree tree = parser.parse(definition);
        CommandMatcher matcher = new DefaultCommandMatcher();
        //without the optional part
        String str = "set m to 1";
        System.out.println(((RootCommandParseTree) tree).getFlatString());
        ConsoleCommand command = matcher.match(tree, str);
        System.out.println("command = " + command);
        Assert.assertNotNull(command);
        Assert.assertEquals(1, command.getGroups().size());
        Assert.assertEquals(2, command.getParameters().size());
        Assert.assertTrue(command.getGroups().containsKey("#root"));
        Assert.assertEquals(str, command.getGroups().get("#root"));
        Assert.assertTrue(command.getParameters().containsKey("variable"));
        Assert.assertEquals("m", command.getParameters().get("variable"));
        Assert.assertTrue(command.getParameters().containsKey("value"));
        Assert.assertEquals(1, command.getParameters().get("value"));
        //with the optional part
        str = "set m to 1 if false";
        command = matcher.match(tree, str);
        System.out.println("command = " + command);
        Assert.assertNotNull(command);
        Assert.assertEquals(2, command.getGroups().size());
        Assert.assertEquals(3, command.getParameters().size());
        Assert.assertTrue(command.getGroups().containsKey("#root"));
        Assert.assertEquals(str, command.getGroups().get("#root"));
        Assert.assertTrue(command.getParameters().containsKey("variable"));
        Assert.assertEquals("m", command.getParameters().get("variable"));
        Assert.assertTrue(command.getParameters().containsKey("value"));
        Assert.assertEquals(1, command.getParameters().get("value"));
        Assert.assertTrue(command.getParameters().containsKey("3"));
        Assert.assertEquals(false, command.getParameters().get("3"));
    }

    @Test
    public void matchCommandWithMultipleOptionalPartsTest() throws Exception {
        final DefaultCommandDefinitionParser parser = new DefaultCommandDefinitionParser();
        final String definition = "set #string(variable) to #integer(value) [condition}if #boolean] [duration}for #integer seconds]";
        System.out.println("definition = " + definition);
        final CommandParseTree tree = parser.parse(definition);
        CommandMatcher matcher = new DefaultCommandMatcher();
        //without the optional parts
        String str = "set m to 1";
        System.out.println(((RootCommandParseTree) tree).getFlatString());
        ConsoleCommand command = matcher.match(tree, str);
        System.out.println("command = " + command);
        Assert.assertNotNull(command);
        Assert.assertEquals(1, command.getGroups().size());
        Assert.assertEquals(2, command.getParameters().size());
        Assert.assertTrue(command.getGroups().containsKey("#root"));
        Assert.assertEquals(str, command.getGroups().get("#root"));
        Assert.assertTrue(command.getParameters().containsKey("variable"));
        Assert.assertEquals("m", command.getParameters().get("variable"));
        Assert.assertTrue(command.getParameters().containsKey("value"));
        Assert.assertEquals(1, command.getParameters().get("value"));
        //with the first optional part
        str = "set m to 1 if false";
        command = matcher.match(tree, str);
        System.out.println("command = " + command);
        Assert.assertNotNull(command);
        Assert.assertEquals(2, command.getGroups().size());
        Assert.assertEquals(3, command.getParameters().size());
        Assert.assertTrue(command.getGroups().containsKey("#root"));
        Assert.assertEquals(str, command.getGroups().get("#root"));
        Assert.assertTrue(command.getParameters().containsKey("variable"));
        Assert.assertEquals("m", command.getParameters().get("variable"));
        Assert.assertTrue(command.getParameters().containsKey("value"));
        Assert.assertEquals(1, command.getParameters().get("value"));
        Assert.assertTrue(command.getParameters().containsKey("3"));
        Assert.assertEquals(false, command.getParameters().get("3"));
        //with the second optional part
        str = "set m to 1 for 10 seconds";
        command = matcher.match(tree, str);
        System.out.println("command = " + command);
        Assert.assertNotNull(command);
        Assert.assertEquals(2, command.getGroups().size());
        Assert.assertEquals(3, command.getParameters().size());
        Assert.assertTrue(command.getGroups().containsKey("#root"));
        Assert.assertEquals(str, command.getGroups().get("#root"));
        Assert.assertTrue(command.getParameters().containsKey("variable"));
        Assert.assertEquals("m", command.getParameters().get("variable"));
        Assert.assertTrue(command.getParameters().containsKey("value"));
        Assert.assertEquals(1, command.getParameters().get("value"));
        Assert.assertTrue(command.getParameters().containsKey("4"));
        Assert.assertEquals(10, command.getParameters().get("4"));
        //with both optional parts
        str = "set m to 1 if true for 10 seconds";
        command = matcher.match(tree, str);
        System.out.println("command = " + command);
        Assert.assertNotNull(command);
        Assert.assertEquals(3, command.getGroups().size());
        Assert.assertEquals(4, command.getParameters().size());
        Assert.assertTrue(command.getGroups().containsKey("#root"));
        Assert.assertEquals(str, command.getGroups().get("#root"));
        Assert.assertTrue(command.getParameters().containsKey("variable"));
        Assert.assertEquals("m", command.getParameters().get("variable"));
        Assert.assertTrue(command.getParameters().containsKey("value"));
        Assert.assertEquals(1, command.getParameters().get("value"));
        Assert.assertTrue(command.getParameters().containsKey("3"));
        Assert.assertEquals(true, command.getParameters().get("3"));
        Assert.assertTrue(command.getParameters().containsKey("4"));
        Assert.assertEquals(10, command.getParameters().get("4"));
    }

    @Test
    public void listCommandInputTest() throws Exception {
        final DefaultCommandDefinitionParser parser = new DefaultCommandDefinitionParser();
        final CommandParseTree tree = parser.parse("echo #list:string(input)");
        final DefaultCommandMatcher matcher = new DefaultCommandMatcher();
        ConsoleCommand match = matcher.match(tree, "echo {a,b,c}");
        Assert.assertNotNull(match);
        System.out.println(match);
        match = matcher.match(tree, "echo 'hello world'");
        Assert.assertNotNull(match);
        System.out.println(match);
    }

    @Test
    public void emptyStringArgumentTest() throws Exception {
        final DefaultCommandDefinitionParser parser = new DefaultCommandDefinitionParser();
        final String definition = "help #string(command)";
        System.out.println("definition = " + definition);
        final CommandParseTree tree = parser.parse(definition);
        System.out.println("tree = " + tree);
        CommandMatcher matcher = new DefaultCommandMatcher();
        ConsoleCommand match = matcher.match(tree, "help  ");
        Assert.assertNull(match);
        match = matcher.match(tree, "help ''");
        Assert.assertNotNull(match);
        Assert.assertEquals(1, match.getParameters().size());
        Assert.assertTrue(match.getParameters().containsKey("command"));
        Assert.assertEquals("", match.getParameters().get("command"));
    }
}
