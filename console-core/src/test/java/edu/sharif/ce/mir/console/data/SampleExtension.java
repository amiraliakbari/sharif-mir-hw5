package edu.sharif.ce.mir.console.data;

import edu.sharif.ce.mir.console.Console;
import edu.sharif.ce.mir.console.api.Command;
import edu.sharif.ce.mir.console.api.Extension;
import edu.sharif.ce.mir.console.api.OnLoad;
import edu.sharif.ce.mir.console.api.OnUnload;
import edu.sharif.ce.mir.utils.entities.list.*;

import java.util.Map;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2/3/12, 21:51)
 */
@Extension
public class SampleExtension {

    public enum Status {
        UNKNOWN, INITIALIZED, UNLOADED
    }
    
    private Status status = Status.UNKNOWN;
    private int echoes = 0;
    private String last = null;

    public Status getStatus() {
        return status;
    }

    public int getEchoes() {
        return echoes;
    }

    public String getLast() {
        return last;
    }

    @OnLoad
    public void load() {
        this.status = Status.INITIALIZED;
    }
    
    @OnUnload
    public void unload() {
        this.status = Status.UNLOADED;
    }
    
    @Command(
            definition = "echo #list:string(input)"
    )
    public void echo(Console console, Map<String, Object> input) {
        this.echoes ++;
        this.last = ((StringList) input.get("input")).get(0);
    }

}
