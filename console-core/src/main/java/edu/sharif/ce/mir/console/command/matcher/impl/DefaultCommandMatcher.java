package edu.sharif.ce.mir.console.command.matcher.impl;

import edu.sharif.ce.mir.console.command.definition.parse.CommandParseTree;
import edu.sharif.ce.mir.console.command.definition.parse.ParameterCommandParseTree;
import edu.sharif.ce.mir.console.command.definition.parse.RootCommandParseTree;
import edu.sharif.ce.mir.console.command.definition.parse.TerminalCommandParseTree;
import edu.sharif.ce.mir.console.command.matcher.CommandMatcher;
import edu.sharif.ce.mir.console.command.matcher.ConsoleCommand;
import edu.sharif.ce.mir.utils.entities.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (29/2/12, 16:01)
 */
public class DefaultCommandMatcher implements CommandMatcher {
    
    @Override
    public ConsoleCommand match(CommandParseTree parseTree, String command) {
        final HashMap<String, Object> parameters = new HashMap<String, Object>();
        final HashMap<String, String> groups = new HashMap<String, String>();
        final Pair<String, String> pair = matchTo(command, parseTree, parameters, groups);
        if (pair == null || !pair.getSecond().isEmpty()) {
            return null;
        }
        return new DefaultConsoleCommand(parameters, groups);
    }
    
    public Pair<String, String> next(String str) {
        if (str.isEmpty()) {
            return new Pair<String, String>(null, "");
        }
        String token = "";
        Character quote = null;
        int pos = 0;
        if ("\"'`".contains(Character.toString(str.charAt(0)))) {
            quote = str.charAt(0);
            pos ++;
        } else if ("{".equals(Character.toString(str.charAt(0)))) {
            //We have a list at our hands and will be treating it as such
            int open = 0;
            while (pos < str.length()) {
                if (quote != null) {
                    if (str.charAt(pos) == quote) {
                        quote = null;
                    }
                    token += str.charAt(pos);
                    pos ++;
                    continue;
                } else {
                    if (str.substring(pos).startsWith("\\\\") ||
                            str.substring(pos).startsWith("\\'") ||
                            str.substring(pos).startsWith("\\\"") ||
                            str.substring(pos).startsWith("\\`")
                            ) {
                        token += str.charAt(pos + 1);
                        pos += 2;
                        continue;
                    }
                    if (str.substring(pos).startsWith("\\n")) {
                        token += "\n";
                        pos += 2;
                        continue;
                    }
                    if (str.substring(pos).startsWith("\\r")) {
                        token += "\r";
                        pos += 2;
                        continue;
                    }
                    if (str.substring(pos).startsWith("\\t")) {
                        token += "\t";
                        pos += 2;
                        continue;
                    }
                    if ("\"'`".contains(Character.toString(str.charAt(pos)))) {
                        quote = str.charAt(pos);
                    }
                    if ("}".equals(Character.toString(str.charAt(pos)))) {
                        open --;
                        if (open == 0) {
                            token += "}";
                            pos ++;
                            break;
                        }
                    }
                    if ("{".equals(Character.toString(str.charAt(pos)))) {
                        open ++;
                    }
                }
                token += str.charAt(pos);
                pos ++;
            }
            return new Pair<String, String>(token, str.substring(pos).trim());
        }
        while (pos < str.length()) {
            if (str.substring(pos).startsWith("\\\\") ||
                    str.substring(pos).startsWith("\\'") ||
                    str.substring(pos).startsWith("\\\"") ||
                    str.substring(pos).startsWith("\\`")
                    ) {
                token += str.charAt(pos + 1);
                pos += 2;
                continue;
            }
            if (str.substring(pos).startsWith("\\n")) {
                token += "\n";
                pos += 2;
                continue;
            }
            if (str.substring(pos).startsWith("\\r")) {
                token += "\r";
                pos += 2;
                continue;
            }
            if (str.substring(pos).startsWith("\\t")) {
                token += "\t";
                pos += 2;
                continue;
            }
            if (quote == null && Character.isWhitespace(str.charAt(pos))) {
                break;
            }
            if (quote != null && "\"'`".contains(Character.toString(str.charAt(pos)))) {
                pos ++;
                break;
            }
            token += str.charAt(pos);
            pos ++;
        }
        return new Pair<String, String>(token, str.substring(pos).trim());
    }
    
    private Pair<String, String> matchTo(String command, CommandParseTree tree, Map<String, Object> parameters, Map<String, String> groups) {
        if (tree instanceof TerminalCommandParseTree) {
            TerminalCommandParseTree parseTree = (TerminalCommandParseTree) tree;
            final Pair<String, String> next = next(command);
            if (next.getFirst() == null) {
                return null;
            }
            if (next.getFirst().equals(parseTree.getValue())) {
                return next;
            }
        } else if (tree instanceof ParameterCommandParseTree) {
            final ParameterCommandParseTree parseTree = (ParameterCommandParseTree) tree;
            final Pair<String, String> next = next(command);
            if (next.getFirst() == null) {
                return null;
            }
            if (parseTree.accepts(next.getFirst())) {
                return next;
            }
        } else if (tree instanceof RootCommandParseTree) {
            Map<String, Object> currentParameters = new HashMap<String, Object>();
            Map<String, String> currentGroups = new HashMap<String, String>();
            String original = command;
            for (int i = 0; i < tree.size(); i ++) {
                if (tree.get(i) instanceof TerminalCommandParseTree) {
                    TerminalCommandParseTree parseTree = (TerminalCommandParseTree) tree.get(i);
                    final Pair<String, String> pair = matchTo(command, parseTree, parameters, groups);
                    if (pair == null) {
                        return null;
                    }
                    command = pair.getSecond();
                } else if (tree.get(i) instanceof ParameterCommandParseTree) {
                    final ParameterCommandParseTree parseTree = (ParameterCommandParseTree) tree.get(i);
                    final Pair<String, String> pair = matchTo(command, parseTree, parameters, groups);
                    if (pair == null) {
                        return null;
                    }
                    command = pair.getSecond();
                    parseTree.setValue(pair.getFirst());
                    currentParameters.put(parseTree.getName(), parseTree.getValue());
                } else if (tree.get(i) instanceof RootCommandParseTree) {
                    final RootCommandParseTree parseTree = (RootCommandParseTree) tree.get(i);
                    final Pair<String, String> pair = matchTo(command, parseTree, currentParameters, currentGroups);
                    if (pair != null) {
                        command = pair.getSecond();
                    }
                }
            }
            if (!((RootCommandParseTree) tree).getName().isEmpty()) {
                currentGroups.put(((RootCommandParseTree) tree).getName(), original.substring(0, original.length() - command.length()));
            }
            parameters.putAll(currentParameters);
            groups.putAll(currentGroups);
            return new Pair<String, String>("", command);
        }
        return null;
    }
    
}
