package edu.sharif.ce.mir.console.io;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (3/3/12, 2:56)
 */
public enum OutputLevel {

    INFO(1), ERROR(2), WARNING(3), DEBUG(4);

    private final int level;

    private OutputLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
