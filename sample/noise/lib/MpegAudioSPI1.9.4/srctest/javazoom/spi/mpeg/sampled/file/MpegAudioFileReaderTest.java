/*
 *   MpegAudioFileReaderTest - JavaZOOM : http://www.javazoom.net
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU Library General Public License as published
 *   by the Free Software Foundation; either version 2 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Library General Public License for more details.
 *
 *   You should have received a copy of the GNU Library General Public
 *   License along with this program; if not, write to the Free Software
 *   Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */
package javazoom.spi.mpeg.sampled.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.Properties;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import junit.framework.TestCase;

/**
 * MpegAudioFileReader unit test.
 * It matches test.mp3 properties to test.mp3.properties expected results.
 * As we don't ship test.mp3, you have to generate your own test.mp3.properties
 * Uncomment out = System.out; in setUp() method to generated it on stdout from 
 * your own MP3 file.
 */
public class MpegAudioFileReaderTest extends TestCase
{
	private String basefile=null;
	private String baseurl=null;
	private String filename=null;
	private String fileurl=null;
	private String name=null;
	private Properties props = null;
	private PrintStream out = null;
	
	/**
	 * Constructor for MpegAudioFileReaderTest.
	 * @param arg0
	 */
	public MpegAudioFileReaderTest(String arg0)
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
		InputStream pin = getClass().getClassLoader().getResourceAsStream("test.mp3.properties");
		props.load(pin);
		basefile = (String) props.getProperty("basefile");
		baseurl = (String) props.getProperty("baseurl");
		name = (String) props.getProperty("filename");		
		filename = basefile + name;	
		fileurl = baseurl + name;	
		//out = System.out;
	}
	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	public void testGetAudioFileFormat()
	{
			_testGetAudioFileFormatFile();
			_testGetAudioFileFormatURL();
			_testGetAudioFileFormatInputStream();
	}
	
	public void testGetAudioInputStream()
	{	
			_testGetAudioInputStreamFile();
			_testGetAudioInputStreamURL();
			_testGetAudioInputStreamInputStream();			
	}
	
	/*
	 * Test for AudioFileFormat getAudioFileFormat(File)
	 */
	public void _testGetAudioFileFormatFile()
	{
		if (out!=null) out.println("*** testGetAudioFileFormatFile ***");
		try
		{
			File file = new File(filename);
			AudioFileFormat baseFileFormat= AudioSystem.getAudioFileFormat(file);			
			dumpAudioFileFormat(baseFileFormat,out,file.toString());
			assertEquals("FrameLength",Integer.parseInt((String)props.getProperty("FrameLength")),baseFileFormat.getFrameLength());			
			assertEquals("ByteLength",Integer.parseInt((String)props.getProperty("ByteLength")),baseFileFormat.getByteLength());			
		}
		catch (UnsupportedAudioFileException e)
		{
			assertTrue("testGetAudioFileFormatFile:"+e.getMessage(),false);
		}
		catch (IOException e)
		{
			assertTrue("testGetAudioFileFormatFile:"+e.getMessage(),false);
		}
	}
	
	/*
	 * Test for AudioFileFormat getAudioFileFormat(URL)
	 */
	public void _testGetAudioFileFormatURL()
	{
		if (out!=null) out.println("*** testGetAudioFileFormatURL ***");
		try
		{
			URL url = new URL(fileurl);
			AudioFileFormat baseFileFormat= AudioSystem.getAudioFileFormat(url);			
			dumpAudioFileFormat(baseFileFormat,out,url.toString());
			assertEquals("FrameLength",-1,baseFileFormat.getFrameLength());			
			assertEquals("ByteLength",-1,baseFileFormat.getByteLength());			
		}
		catch (UnsupportedAudioFileException e)
		{
			assertTrue("testGetAudioFileFormatURL:"+e.getMessage(),false);
		}
		catch (IOException e)
		{
			assertTrue("testGetAudioFileFormatURL:"+e.getMessage(),false);
		}
	}

	/*
	 * Test for AudioFileFormat getAudioFileFormat(InputStream)
	 */
	public void _testGetAudioFileFormatInputStream()
	{
		if (out!=null) out.println("*** testGetAudioFileFormatInputStream ***");
		try
		{
			InputStream in = new BufferedInputStream(new FileInputStream(filename));
			AudioFileFormat baseFileFormat= AudioSystem.getAudioFileFormat(in);			
			dumpAudioFileFormat(baseFileFormat,out,in.toString());
			in.close();
			assertEquals("FrameLength",-1,baseFileFormat.getFrameLength());			
			assertEquals("ByteLength",-1,baseFileFormat.getByteLength());			
		}
		catch (UnsupportedAudioFileException e)
		{
			assertTrue("testGetAudioFileFormatInputStream:"+e.getMessage(),false);
		}
		catch (IOException e)
		{
			assertTrue("testGetAudioFileFormatInputStream:"+e.getMessage(),false);
		}
	}

	/*
	 * Test for AudioInputStream getAudioInputStream(InputStream)
	 */
	public void _testGetAudioInputStreamInputStream()
	{
		if (out!=null) out.println("*** testGetAudioInputStreamInputStream ***");
		try
		{
			InputStream fin = new BufferedInputStream(new FileInputStream(filename));
			AudioInputStream in= AudioSystem.getAudioInputStream(fin);		
			dumpAudioInputStream(in,out,fin.toString());
			assertEquals("FrameLength",-1,in.getFrameLength());			
			assertEquals("Available",Integer.parseInt((String)props.getProperty("Available")),in.available());			
			fin.close();
			in.close();
		}
		catch (UnsupportedAudioFileException e)
		{
			assertTrue("testGetAudioInputStreamInputStream:"+e.getMessage(),false);
		}
		catch (IOException e)
		{
			assertTrue("testGetAudioInputStreamInputStream:"+e.getMessage(),false);
		}
	}
	
	/*
	 * Test for AudioInputStream getAudioInputStream(File)
	 */
	public void _testGetAudioInputStreamFile()
	{
		if (out!=null) out.println("*** testGetAudioInputStreamFile ***");
		try
		{
			File file = new File(filename);
			AudioInputStream in= AudioSystem.getAudioInputStream(file);
			dumpAudioInputStream(in,out,file.toString());
			assertEquals("FrameLength",-1,in.getFrameLength());			
			assertEquals("Available",Integer.parseInt((String)props.getProperty("Available")),in.available());
			in.close();		
		}
		catch (UnsupportedAudioFileException e)
		{
			assertTrue("testGetAudioInputStreamFile:"+e.getMessage(),false);
		}
		catch (IOException e)
		{
			assertTrue("testGetAudioInputStreamFile:"+e.getMessage(),false);
		}
	}

	/*
	 * Test for AudioInputStream getAudioInputStream(URL)
	 */
	public void _testGetAudioInputStreamURL()
	{
		if (out!=null) out.println("*** testGetAudioInputStreamURL ***");
		try
		{
			URL url = new URL(fileurl);
			AudioInputStream in= AudioSystem.getAudioInputStream(url);		
			dumpAudioInputStream(in,out,url.toString());
			assertEquals("FrameLength",-1,in.getFrameLength());			
			assertEquals("Available",Integer.parseInt((String)props.getProperty("Available")),in.available());
			in.close();
		}
		catch (UnsupportedAudioFileException e)
		{
			assertTrue("testGetAudioInputStreamURL:"+e.getMessage(),false);
		}
		catch (IOException e)
		{
			assertTrue("testGetAudioInputStreamURL:"+e.getMessage(),false);
		}
	}
	
	private void dumpAudioFileFormat(AudioFileFormat baseFileFormat, PrintStream out, String info) throws UnsupportedAudioFileException
	{
		AudioFormat baseFormat = baseFileFormat.getFormat(); 
		if (out != null)
		{
			// AudioFileFormat
			out.println("  -----  "+info+"  -----");		
			out.println("    ByteLength="+ baseFileFormat.getByteLength());		
			out.println("    FrameLength="+ baseFileFormat.getFrameLength());		
			out.println("    Type="+ baseFileFormat.getType());		
			// AudioFormat					
			out.println("    SourceFormat="+baseFormat.toString());
			out.println("    Channels="+ baseFormat.getChannels());		
			out.println("    FrameRate="+ baseFormat.getFrameRate());		
			out.println("    FrameSize="+ baseFormat.getFrameSize());		
			out.println("    SampleRate="+ baseFormat.getSampleRate());		
			out.println("    SampleSizeInBits="+ baseFormat.getSampleSizeInBits());		
			out.println("    Encoding="+ baseFormat.getEncoding());						
		}
		assertEquals("Type",(String)props.getProperty("Type"),baseFileFormat.getType().toString());
		assertEquals("SourceFormat",(String)props.getProperty("SourceFormat"),baseFormat.toString());
		assertEquals("Channels",Integer.parseInt((String)props.getProperty("Channels")),baseFormat.getChannels());			
		assertTrue("FrameRate",Float.parseFloat((String)props.getProperty("FrameRate"))==baseFormat.getFrameRate());
		assertEquals("FrameSize",Integer.parseInt((String)props.getProperty("FrameSize")),baseFormat.getFrameSize());			
		assertTrue("SampleRate",Float.parseFloat((String)props.getProperty("SampleRate"))==baseFormat.getSampleRate());
		assertEquals("SampleSizeInBits",Integer.parseInt((String)props.getProperty("SampleSizeInBits")),baseFormat.getSampleSizeInBits());			
		assertEquals("Encoding",(String)props.getProperty("Encoding"),baseFormat.getEncoding().toString());	
	}
	
	private void dumpAudioInputStream(AudioInputStream in, PrintStream out, String info) throws IOException
	{
		AudioFormat baseFormat = in.getFormat();
		if (out != null)
		{	
			out.println("  -----  "+info+"  -----");		
			out.println("    Available="+in.available());
			out.println("    FrameLength="+in.getFrameLength());
			// AudioFormat					
			out.println("    SourceFormat="+baseFormat.toString());
			out.println("    Channels="+ baseFormat.getChannels());		
			out.println("    FrameRate="+ baseFormat.getFrameRate());		
			out.println("    FrameSize="+ baseFormat.getFrameSize());		
			out.println("    SampleRate="+ baseFormat.getSampleRate());		
			out.println("    SampleSizeInBits="+ baseFormat.getSampleSizeInBits());		
			out.println("    Encoding="+ baseFormat.getEncoding());							
		}		
		assertEquals("SourceFormat",(String)props.getProperty("SourceFormat"),baseFormat.toString());
		assertEquals("Channels",Integer.parseInt((String)props.getProperty("Channels")),baseFormat.getChannels());			
		assertTrue("FrameRate",Float.parseFloat((String)props.getProperty("FrameRate"))==baseFormat.getFrameRate());
		assertEquals("FrameSize",Integer.parseInt((String)props.getProperty("FrameSize")),baseFormat.getFrameSize());			
		assertTrue("SampleRate",Float.parseFloat((String)props.getProperty("SampleRate"))==baseFormat.getSampleRate());
		assertEquals("SampleSizeInBits",Integer.parseInt((String)props.getProperty("SampleSizeInBits")),baseFormat.getSampleSizeInBits());			
		assertEquals("Encoding",(String)props.getProperty("Encoding"),baseFormat.getEncoding().toString());	

	}
}
