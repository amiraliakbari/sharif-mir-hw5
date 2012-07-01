package edu.sharif.ce.mir.console.impl.extensions;

import edu.sharif.ce.mir.console.Console;
import edu.sharif.ce.mir.console.api.Command;
import edu.sharif.ce.mir.console.api.Extension;
import edu.sharif.ce.mir.console.extension.tags.AutoLoadedExtension;
import edu.sharif.ce.mir.console.extension.tags.PerpetualExtension;
import edu.sharif.ce.mir.console.io.OutputLevel;
import edu.sharif.ce.mir.console.io.impl.PrimitiveOutput;
import edu.sharif.ce.mir.utils.entities.EnumWrapper;
import edu.sharif.ce.mir.utils.entities.list.StringList;

import java.util.Map;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (3/3/12, 1:59)
 */
@Extension(
        namespace = "console"
)
public class Basics implements PerpetualExtension, AutoLoadedExtension {

    @Command(
            definition = "exit",
            description = "Exits the console",
            help = "Gracefully shuts down the console"
    )
    public void exit(Console console) {
        console.terminate();
    }

    @Command(
            definition = "set output level to #enum[debug|warning|error|info](level)"
    )
    public void setOutputLevel(Console console, Map<String, Object> arguments) {
        console.setOutputLevel(((EnumWrapper) arguments.get("level")).getValue(OutputLevel.class));
    }

    @Command(
            definition = "echo #list:string(input)"
    )
    public void echo(Console console, Map<String, Object> arguments) {
        final StringList input = (StringList) arguments.get("input");
        for (String s : input) {
            console.write(new PrimitiveOutput(s));
        }
    }

}
