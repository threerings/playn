/*
 *   VorbisAudioFileReaderTest - JavaZOOM : http://www.javazoom.net
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
package javazoom.spi.vorbis.sampled.file;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import junit.framework.TestCase;

/**
 * This test case doesn't include assert, so it's not a jUnit test
 * as you used to have. However, it helps in testing all methods
 * of SPI. 
 */
public class VorbisAudioFileReaderTest extends TestCase
{
	private String basefile="g:\\data\\";
	private String baseurl="file:///g:/data/";
	private String filename=null;
	private String fileurl=null;
	private String[] names={"hangbug.ogg","test.ogg","test.au","test.wav"};
	private PrintStream out = null;
	
	/**
	 * Constructor for VorbisAudioFileReaderTest.
	 * @param arg0
	 */
	public VorbisAudioFileReaderTest(String arg0)
	{
		super(arg0);
		out = System.out;
	}
	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();
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
		for (int i =0;i<names.length;i++)
		{
			filename = basefile + names[i];
			fileurl = baseurl + names[i];
			_testGetAudioFileFormatFile();
			_testGetAudioFileFormatURL();
			_testGetAudioFileFormatInputStream();
		}
	}
	
	public void testGetAudioInputStream()
	{
		for (int i =0;i<names.length;i++)
		{
			filename = basefile + names[i];
			fileurl = baseurl + names[i];		
			_testGetAudioInputStreamFile();
			_testGetAudioInputStreamURL();
			_testGetAudioInputStreamInputStream();
		}		
		//_testGetInfo();				
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
			// TODO : Add assert();	
		}
		catch (UnsupportedAudioFileException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
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
			// TODO : Add assert();
		}
		catch (UnsupportedAudioFileException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
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
			// TODO : Add assert();
		}
		catch (UnsupportedAudioFileException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
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
			fin.close();
			in.close();
			// TODO : Add assert();
		}
		catch (UnsupportedAudioFileException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
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
			in.close();		
			// TODO : Add assert();
		}
		catch (UnsupportedAudioFileException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
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
			in.close();
			// TODO : Add assert();
		}
		catch (UnsupportedAudioFileException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void _testGetInfo() 
	{
			if (out!=null) out.println("*** testGetInfo ***");
			try
			{
				byte[] audioData = getByteArrayFromFile(new File(filename));
				AudioInputStream ais = AudioSystem.getAudioInputStream(new ByteArrayInputStream(audioData));	
			}
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			catch (UnsupportedAudioFileException e)
			{
				e.printStackTrace();
			}
	}
	
	private static byte[] getByteArrayFromFile(final File file) throws FileNotFoundException, IOException
	{
		final FileInputStream fis = new FileInputStream(file);
		try
		{
			final ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
			final byte[] buffer = new byte[1024];
			int cnt;
			while ((cnt = fis.read(buffer)) != -1)
			{
				bos.write(buffer, 0, cnt);
			}
			return bos.toByteArray();
		}
		finally
		{
			fis.close();
		}
	}

	private void dumpAudioFileFormat(AudioFileFormat baseFileFormat, PrintStream out, String info) throws UnsupportedAudioFileException
	{
		if (out != null)
		{
			// AudioFileFormat
			out.println("  -----  "+info+"  -----");		
			out.println("    ByteLength="+ baseFileFormat.getByteLength());		
			out.println("    getFrameLength="+ baseFileFormat.getFrameLength());		
			out.println("    getType="+ baseFileFormat.getType());		
			AudioFormat baseFormat = baseFileFormat.getFormat(); 
			// AudioFormat					
			out.println("    Source Format="+baseFormat.toString());
			out.println("    Channels="+ baseFormat.getChannels());		
			out.println("    FrameRate="+ baseFormat.getFrameRate());		
			out.println("    FrameSize="+ baseFormat.getFrameSize());		
			out.println("    SampleRate="+ baseFormat.getSampleRate());		
			out.println("    SampleSizeInBits="+ baseFormat.getSampleSizeInBits());		
			out.println("    Encoding="+ baseFormat.getEncoding());						
		}		
	}
	
	private void dumpAudioInputStream(AudioInputStream in, PrintStream out, String info) throws IOException
	{
		if (out != null)
		{	
			out.println("  -----  "+info+"  -----");		
			out.println("    Available="+in.available());
			out.println("    FrameLength="+in.getFrameLength());
			AudioFormat baseFormat = in.getFormat();
			// AudioFormat					
			out.println("    Source Format="+baseFormat.toString());
			out.println("    Channels="+ baseFormat.getChannels());		
			out.println("    FrameRate="+ baseFormat.getFrameRate());		
			out.println("    FrameSize="+ baseFormat.getFrameSize());		
			out.println("    SampleRate="+ baseFormat.getSampleRate());		
			out.println("    SampleSizeInBits="+ baseFormat.getSampleSizeInBits());		
			out.println("    Encoding="+ baseFormat.getEncoding());							
		}		
	}
}
