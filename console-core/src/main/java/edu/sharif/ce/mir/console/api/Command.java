package edu.sharif.ce.mir.console.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to specify which public methods of a class marked
 * with the {@link Extension} annotation are actually console commands.
 * The method name will replace the command's name. You can also add a
 * description and a help text for the command which can be later used by
 * the {@link edu.sharif.ce.mir.console.impl.extensions.Help} extension to
 * provide help and usage information for any given command.
 *
 * Methods annotated with Command can have parameters of these types:
 * <ul>
 *     <li>{@link edu.sharif.ce.mir.console.command.matcher.GroupMap}</li>
 *     <li>{@link edu.sharif.ce.mir.console.Console}</li>
 *     <li>{@link edu.sharif.ce.mir.console.em.ExtensionManager}</li>
 *     <li>{@link edu.sharif.ce.mir.console.command.definition.CommandDefinitionParser}</li>
 *     <li>{@link edu.sharif.ce.mir.console.command.matcher.CommandMatcher}</li>
 *     <li>{@link java.util.Map}</li>
 * </ul>
 * In each case, they would receive the appropriate value. For the last case, the
 * argument value would be filled with parameters for which a value could have been
 * scanned from the input
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (29/2/12, 18:20)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {

    /**
     * The command definition. The command definition follows this pattern:
     *      token token token #type(name) token [optional part]...
     * The matcher will then try to match any input commands to the definition
     * @return the definition
     */
    public String definition();
    public String description() default "";
    public String help() default "";

}
