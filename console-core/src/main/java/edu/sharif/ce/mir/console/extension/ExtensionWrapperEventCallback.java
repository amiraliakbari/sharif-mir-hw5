package edu.sharif.ce.mir.console.extension;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (3/3/12, 0:12)
 */
public interface ExtensionWrapperEventCallback {

    <E> void execute(ExtensionWrapper<E> wrapper);

    public static class NONE implements ExtensionWrapperEventCallback {

        @Override
        public <E> void execute(ExtensionWrapper<E> wrapper) {
        }

    }

}
