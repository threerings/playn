package javazoom.spi.vorbis.sampled.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import junit.framework.TestCase;

import org.tritonus.share.sampled.TAudioFormat;
import org.tritonus.share.sampled.file.TAudioFileFormat;

/**
 * PropertiesContainer unit test.
 * It matches test.ogg properties to test.ogg.properties expected results.
 * As we don't ship test.ogg, you have to generate your own test.ogg.properties
 * Uncomment out = System.out; in setUp() method to generated it on stdout from 
 * your own Ogg Vorbis file.
 */
public class PropertiesTest extends TestCase
{
	private String basefile=null;
	private String baseurl=null;
	private String filename=null;
	private String fileurl=null;
	private String name=null;
	private Properties props = null;
	private PrintStream out = null;

	/**
	 * Constructor for PropertiesTest.
	 * @param arg0
	 */
	public PropertiesTest(String arg0)
	{
		super(arg0);
	}
	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();
		props = new Properties();
		InputStream pin = getClass().getClassLoader().getResourceAsStream("test.ogg.properties");
		props.load(pin);
		basefile = (String) props.getProperty("basefile");
		baseurl = (String) props.getProperty("baseurl");
		name = (String) props.getProperty("filename");
		filename = basefile + name;	
        String stream = (String) props.getProperty("stream");
        if (stream != null) fileurl = stream;
        else fileurl = baseurl + name; 			
		out = System.out;
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}
	
	public void testPropertiesFile()
	{
		String[] testPropsAFF = {"duration","title","author","album","date","comment",
							  "copyright","ogg.bitrate.min","ogg.bitrate.nominal","ogg.bitrate.max"};
		String[] testPropsAF = {"vbr", "bitrate"};
		
		File file = new File(filename);
		AudioFileFormat baseFileFormat = null;
		AudioFormat baseFormat = null;
		try
		{		
			baseFileFormat = AudioSystem.getAudioFileFormat(file);
			baseFormat = baseFileFormat.getFormat();
			if (out != null) out.println("-> Filename : "+filename+" <-");
			if (out != null)  out.println(baseFileFormat);
			if (baseFileFormat instanceof TAudioFileFormat)
			{
				Map properties = ((TAudioFileFormat)baseFileFormat).properties();
				if (out != null)  out.println(properties);
				for (int i=0;i<testPropsAFF.length;i++)
				{
					String key = testPropsAFF[i];
					if (properties.get(key) != null)
					{
						String val = (properties.get(key)).toString();
						//if (out != null)  out.println(key+"="+val);
						String valexpected = props.getProperty(key);
						//assertEquals(key,valexpected,val);
					}
				}
			}
			else
			{
				assertTrue("testPropertiesFile : TAudioFileFormat expected",false);
			}
			
			if (baseFormat instanceof TAudioFormat)
			{
				Map properties = ((TAudioFormat)baseFormat).properties();
				for (int i=0;i<testPropsAF.length;i++)
				{
					String key = testPropsAF[i];
					if (properties.get(key) != null)
					{
						String val = (properties.get(key)).toString();
						if (out != null)  out.println(key+"="+val);
						String valexpected = props.getProperty(key);
						//assertEquals(key,valexpected,val);						
					}
				}
			}
			else
			{
				assertTrue("testPropertiesFile : TAudioFormat expected",false);
			}			
		}
		catch (UnsupportedAudioFileException e)
		{
			assertTrue("testPropertiesFile : "+e.getMessage(),false);
		}
		catch (IOException e)
		{
			assertTrue("testPropertiesFile : "+e.getMessage(),false);
		}
	}

	public void testPropertiesURL()
	{
		String[] testPropsAFF = {"duration","title","author","album","date","comment",
							  "copyright","ogg.bitrate.min","ogg.bitrate.nominal","ogg.bitrate.max"};
		String[] testPropsAF = {"vbr", "bitrate"};
		AudioFileFormat baseFileFormat = null;
		AudioFormat baseFormat = null;
		try
		{		
			URL url = new URL(fileurl);
			baseFileFormat = AudioSystem.getAudioFileFormat(url);
			baseFormat = baseFileFormat.getFormat();
			if (out != null) out.println("-> URL : "+fileurl+" <-");
			if (out != null) out.println(baseFileFormat);
			if (baseFileFormat instanceof TAudioFileFormat)
			{
				Map properties = ((TAudioFileFormat)baseFileFormat).properties();
				if (out != null)  out.println(properties);
				for (int i=0;i<testPropsAFF.length;i++)
				{
					String key = testPropsAFF[i];
					if (properties.get(key) != null)
					{
						String val = (properties.get(key)).toString();
						//if (out != null)  out.println(key+"="+val);
						String valexpected = props.getProperty(key);
						//assertEquals(key,valexpected,val);						
					}
				}
			}
			else
			{
				assertTrue("testPropertiesURL : TAudioFileFormat expected",false);
			}
			if (baseFormat instanceof TAudioFormat)
			{
				Map properties = ((TAudioFormat)baseFormat).properties();
				for (int i=0;i<testPropsAF.length;i++)
				{
					String key = testPropsAF[i];
					if (properties.get(key) != null)
					{
						String val = (properties.get(key)).toString();
						if (out != null)  out.println(key+"="+val);
						String valexpected = props.getProperty(key);
						//assertEquals(key,valexpected,val);						
					}
				}
			}
			else
			{
				assertTrue("testPropertiesURL : TAudioFormat expected",false);
			}			
		}
		catch (UnsupportedAudioFileException e)
		{
			assertTrue("testPropertiesURL : "+e.getMessage(),false);
		}
		catch (IOException e)
		{
			assertTrue("testPropertiesURL : "+e.getMessage(),false);
		}
	}
}
