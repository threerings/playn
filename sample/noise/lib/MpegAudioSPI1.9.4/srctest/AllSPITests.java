import javazoom.spi.mpeg.sampled.file.PropertiesTest;
import javazoom.spi.mpeg.sampled.file.MpegAudioFileReaderTest;
import junit.framework.Test;
import junit.framework.TestSuite;


/**
 * @author Administrateur
 *
 */
public class AllSPITests
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test for default package");
		//$JUnit-BEGIN$
		suite.addTest(new TestSuite(MpegAudioFileReaderTest.class));
		suite.addTest(new TestSuite(PropertiesTest.class));
		suite.addTest(new TestSuite(PlayerTest.class));
		//$JUnit-END$
		return suite;
	}
}
