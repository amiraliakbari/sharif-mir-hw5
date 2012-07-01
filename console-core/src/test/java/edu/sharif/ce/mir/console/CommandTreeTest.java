package edu.sharif.ce.mir.console;

import edu.sharif.ce.mir.console.command.definition.CommandDefinitionParser;
import edu.sharif.ce.mir.console.command.definition.impl.DefaultCommandDefinitionParser;
import edu.sharif.ce.mir.console.command.definition.parse.CommandParseTree;
import edu.sharif.ce.mir.console.command.definition.parse.RootCommandParseTree;
import edu.sharif.ce.mir.console.command.definition.parse.TerminalCommandParseTree;
import edu.sharif.ce.mir.console.errors.BracketWithoutOpeningInCommandDefinitionError;
import edu.sharif.ce.mir.console.errors.MisplacedBracketInCommandDefinitionError;
import edu.sharif.ce.mir.console.errors.UnclosedBracketInCommandDefinition;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (28/2/12, 15:36)
 */
public class CommandTreeTest {

    @Test
    public void invalidCommandDefinitionTest() throws Exception {
        boolean condition = false;
        final CommandDefinitionParser parser = new DefaultCommandDefinitionParser();
        try {
            parser.parse("add #int(first to #int(second)");
        } catch (MisplacedBracketInCommandDefinitionError e) {
            condition = true;
        }
        Assert.assertTrue(condition);
        condition = false;
        try {
            parser.parse("add #int(first) to #int([second)");
        } catch (MisplacedBracketInCommandDefinitionError e) {
            condition = true;
        }
        Assert.assertTrue(condition);
        condition = false;
        try {
            parser.parse("add #int(first) to #int(second) [(())]");
        } catch (MisplacedBracketInCommandDefinitionError e) {
            condition = true;
        }
        Assert.assertTrue(condition);
        condition = false;
        try {
            parser.parse("add #int(first");
        } catch (UnclosedBracketInCommandDefinition e) {
            condition = true;
        }
        Assert.assertTrue(condition);
        condition = false;
        try {
            parser.parse("add [#int(first");
        } catch (UnclosedBracketInCommandDefinition e) {
            System.out.println(e.getMessage());
            condition = true;
        }
        Assert.assertTrue(condition);
        condition = false;
        try {
            parser.parse("add [#int(first)");
        } catch (UnclosedBracketInCommandDefinition e) {
            System.out.println(e.getMessage());
            condition = true;
        }
        Assert.assertTrue(condition);
        condition = false;
        try {
            parser.parse("add [#int)");
        } catch (BracketWithoutOpeningInCommandDefinitionError e) {
            System.out.println(e.getMessage());
            condition = true;
        }
        Assert.assertTrue(condition);
    }

    @Test
    public void tokenOnlyCommandWithoutOptionalPartsTest() throws Exception {
        final String definition = "this is a test.";
        CommandDefinitionParser parser = new DefaultCommandDefinitionParser();
        final CommandParseTree tree = parser.parse(definition);
        Assert.assertNotNull(tree);
        Assert.assertTrue(tree instanceof RootCommandParseTree);
        final String[] terminals = {"this", "is", "a", "test."};
        Assert.assertEquals(terminals.length, tree.size());
        for (int i = 0; i < tree.size(); i ++) {
            final CommandParseTree item = tree.get(i);
            Assert.assertTrue(item instanceof TerminalCommandParseTree);
            final TerminalCommandParseTree terminal = (TerminalCommandParseTree) item;
            Assert.assertEquals(terminals[i], terminal.getValue());
        }
    }

    @Test
    public void commandWithParametersTest() throws Exception {
        final String definition = "check if #string(a) is #integer(b) [then store it in #string(target) [v}verbosely]]";
        CommandDefinitionParser parser = new DefaultCommandDefinitionParser();
        final CommandParseTree tree = parser.parse(definition);
        System.out.println(tree);
    }

}
