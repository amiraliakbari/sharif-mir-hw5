package edu.sharif.ce.mir.console.command.definition.impl;

import edu.sharif.ce.mir.console.command.definition.CommandDefinitionParser;
import edu.sharif.ce.mir.console.command.definition.parse.*;
import edu.sharif.ce.mir.console.errors.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (28/2/12, 15:53)
 */
public class DefaultCommandDefinitionParser implements CommandDefinitionParser {

    private static int parametersCount;

    private static class PositionHolder {

        private int position;
        private char type;

        public PositionHolder(int position, char type) {
            this.position = position;
            this.type = type;
        }

        public int getPosition() {
            return position;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PositionHolder that = (PositionHolder) o;
            return type == that.type;
        }

        public char getType() {
            return type;
        }
    }

    private void validateParentheses(String definition) {
        final List<PositionHolder> holders = new ArrayList<PositionHolder>();
        for (int i = 0; i < definition.length(); i ++) {
            if (definition.charAt(i) == '[') {
                final PositionHolder holder = new PositionHolder(i, '[');
                if (!holders.isEmpty() && !holders.get(holders.size() - 1).equals(holder)) {
                    throw new MisplacedBracketInCommandDefinitionError(definition, i);
                }
                holders.add(holder);
            } else if (definition.charAt(i) == ']') {
                if (holders.isEmpty() || holders.get(holders.size() - 1).getType() != '[') {
                    throw new BracketWithoutOpeningInCommandDefinitionError(definition, i);
                }
                holders.remove(holders.size() - 1);
            } else if (definition.charAt(i) == '(') {
                final PositionHolder holder = new PositionHolder(i, '(');
                if (!holders.isEmpty() && holders.get(holders.size() - 1).equals(holder)) {
                    throw new MisplacedBracketInCommandDefinitionError(definition, i);
                }
                holders.add(holder);
            } else if (definition.charAt(i) == ')') {
                if (holders.isEmpty() || holders.get(holders.size() - 1).getType() != '(') {
                    throw new BracketWithoutOpeningInCommandDefinitionError(definition, i);
                }
                holders.remove(holders.size() - 1);
            }
        }
        if (!holders.isEmpty()) {
            throw new UnclosedBracketInCommandDefinition(definition, holders.get(holders.size() - 1).getPosition());
        }
    }

    private CommandParseTree parseSubTree(String definition, boolean isRoot) {
        final RootCommandParseTree tree = new RootCommandParseTree();
        int pos = 0;
        if (!isRoot && definition.matches("^[^\\s]+?\\}.*?")) {
            pos = definition.indexOf("}") + 1;
            tree.setName(definition.substring(0, pos - 1));
        }
        while (pos < definition.length()) {
            //skipping whitespace characters
            while (pos < definition.length() && Character.isWhitespace(definition.charAt(pos))) {
                pos ++;
            }
            //finding tokens
            if (pos >= definition.length()) {
                break;
            }
            //We are at the beginning of a new token
            String token = "";
            while (pos < definition.length() && !Character.isWhitespace(definition.charAt(pos))) {
                token += definition.charAt(pos);
                pos ++;
            }
            if (token.startsWith("#")) {
                parametersCount ++;
                //Parameter descriptor
                final Matcher matcher = Pattern.compile("^#([^\\(]+?)(?:\\(([^\\)]+?)\\))?$", Pattern.DOTALL).matcher(token);
                if (!matcher.find()) {
                    throw new InvalidParameterDefinitionInCommandDefinitionError(definition, token, pos);
                }
                final String type = matcher.group(1);
                String name = String.valueOf(parametersCount);
                if (matcher.group(2) != null) {
                    name = matcher.group(2);
                }
                tree.add(new ParameterCommandParseTree(name, type));
            } else if (token.startsWith("[")) {
                //Root token
                int open = 0;
                while (pos < definition.length()) {
                    token += definition.charAt(pos);
                    if (definition.charAt(pos) == '[') {
                        open ++;
                    } else if (definition.charAt(pos) == ']') {
                        open --;
                        if (open < 0) {
                            pos ++;
                            break;
                        }
                    }
                    pos ++;
                }
                token = token.substring(1, token.length() - 1);
                tree.add(parseSubTree(token, false));
            } else {
                //Terminal token
                tree.add(new TerminalCommandParseTree(token));
            }
        }
        return tree;
    }

    @Override
    public CommandParseTree parse(final String definition) {
        validateParentheses(definition);
        parametersCount = 0;
        final CommandParseTree tree = parseSubTree(definition, true);
        ((RootCommandParseTree) tree).setName("#root");
        tree.walk(new CommandParseTreeCallback() {

            private Set<String> parameters = new HashSet<String>();
            private Set<String> groups = new HashSet<String>();

            @Override
            public void execute(CommandParseTree tree) {
                if (tree instanceof ParameterCommandParseTree) {
                    final ParameterCommandParseTree parameter = (ParameterCommandParseTree) tree;
                    if (parameters.contains(parameter.getName())) {
                        throw new DuplicateParameterInCommandDefinitionError(parameter.getName(), definition);
                    }
                    parameters.add(parameter.getName());
                }
                if (tree instanceof RootCommandParseTree) {
                    final RootCommandParseTree group = (RootCommandParseTree) tree;
                    if (groups.contains(group.getName())) {
                        throw new DuplicateGroupNameInCommandDefinitionError(group.getName(), definition);
                    }
                    groups.add(group.getName());
                }
            }

        });
        return tree;
    }

}
