package edu.sharif.ce.mir.console;

import edu.sharif.ce.mir.console.data.SampleExtension;
import edu.sharif.ce.mir.console.extension.ExtensionWrapper;
import edu.sharif.ce.mir.console.extension.impl.ClassExtensionWrapper;
import edu.sharif.ce.mir.console.impl.StandardConsole;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2/3/12, 21:50)
 */
public class ExtensionWrapperTest {

    @Test
    public void loadExtensionFromClassTest() throws Exception {
        final Console console = new StandardConsole();
        ExtensionWrapper<SampleExtension> wrapper = new ClassExtensionWrapper<SampleExtension>(SampleExtension.class, console, getClass().getClassLoader());
        Assert.assertEquals(1, wrapper.getCommands().size());
        final SampleExtension extension = wrapper.getExtension();
        Assert.assertNotNull(extension);
        Assert.assertEquals(extension.getClass().getSimpleName(), wrapper.getName());
        Assert.assertEquals(extension.getClass().getSimpleName(), wrapper.getNamespace());
        Assert.assertEquals(SampleExtension.Status.UNKNOWN, extension.getStatus());
        wrapper.load();
        Assert.assertEquals(SampleExtension.Status.INITIALIZED, extension.getStatus());
        final String command = "echo test";
        Assert.assertTrue(wrapper.knows(command));
        final int echoes = extension.getEchoes();
        wrapper.execute(command);
        Assert.assertEquals(echoes + 1, extension.getEchoes());
        Assert.assertEquals("test", extension.getLast());
        wrapper.unload();
        Assert.assertEquals(SampleExtension.Status.UNLOADED, extension.getStatus());
    }
}
