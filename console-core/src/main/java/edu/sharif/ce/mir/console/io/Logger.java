package edu.sharif.ce.mir.console.io;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (3/3/12, 1:45)
 */
public interface Logger {

    void setOutputLevel(OutputLevel outputLevel);

    void error(String message);
    
    void error(Throwable throwable);
    
    void info(String message);

    void warn(String message);

    void debug(String message);

}
