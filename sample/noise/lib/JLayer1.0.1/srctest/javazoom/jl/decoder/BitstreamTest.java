/*
 * 11/19/2004 : 1.0 moved to LGPL.
 * 01/01/2004 : Initial version by E.B javalayer@javazoom.net
 *-----------------------------------------------------------------------
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
 *----------------------------------------------------------------------
 */

package javazoom.jl.decoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Properties;

import junit.framework.TestCase;

/**
 * Bitstream unit test.
 * It matches test.mp3 properties to test.mp3.properties expected results.
 * As we don't ship test.mp3, you have to generate your own test.mp3.properties
 * Uncomment out = System.out; in setUp() method to generated it on stdout from 
 * your own MP3 file.
 * @since 0.4
 */
public class BitstreamTest extends TestCase
{
	private String basefile = null;
	private String name = null;
	private String filename = null;
	private PrintStream out = null;
	private Properties props = null;
	private FileInputStream mp3in = null;
	private Bitstream in = null;
	
	/**
	 * Constructor for BitstreamTest.
	 * @param arg0
	 */
	public BitstreamTest(String arg0)
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
		name = (String) props.getProperty("filename");		
		filename = basefile + name;	
		mp3in = new FileInputStream(filename);
		in = new Bitstream(mp3in);
		//out = System.out;
	}
	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception
	{
		super.tearDown();
		in.close();
		mp3in.close();	
	}

	public void testStream()
	{
		try
		{
			InputStream id3in = in.getRawID3v2();
			int size = id3in.available();
			Header header = in.readFrame();
			if (out != null)
			{
				out.println("--- "+filename+" ---");
				out.println("ID3v2Size="+size);
				out.println("version="+header.version());
				out.println("version_string="+header.version_string());
				out.println("layer="+header.layer());
				out.println("frequency="+header.frequency());
				out.println("frequency_string="+header.sample_frequency_string());
				out.println("bitrate="+header.bitrate());
				out.println("bitrate_string="+header.bitrate_string());
				out.println("mode="+header.mode());
				out.println("mode_string="+header.mode_string());
				out.println("slots="+header.slots());
				out.println("vbr="+header.vbr());
				out.println("vbr_scale="+header.vbr_scale());
				out.println("max_number_of_frames="+header.max_number_of_frames(mp3in.available()));
				out.println("min_number_of_frames="+header.min_number_of_frames(mp3in.available()));
				out.println("ms_per_frame="+header.ms_per_frame());
				out.println("frames_per_second="+(float) ((1.0 / (header.ms_per_frame())) * 1000.0));
				out.println("total_ms="+header.total_ms(mp3in.available()));
				out.println("SyncHeader="+header.getSyncHeader());
				out.println("checksums="+header.checksums());
				out.println("copyright="+header.copyright());
				out.println("original="+header.original());
				out.println("padding="+header.padding());
				out.println("framesize="+header.calculate_framesize());
				out.println("number_of_subbands="+header.number_of_subbands());				
			}
			assertEquals("ID3v2Size",Integer.parseInt((String)props.getProperty("ID3v2Size")),size);			
			assertEquals("version",Integer.parseInt((String)props.getProperty("version")),header.version());
			assertEquals("version_string",(String)props.getProperty("version_string"),header.version_string());
			assertEquals("layer",Integer.parseInt((String)props.getProperty("layer")),header.layer());
			assertEquals("frequency",Integer.parseInt((String)props.getProperty("frequency")),header.frequency());
			assertEquals("frequency_string",(String)props.getProperty("frequency_string"),header.sample_frequency_string());
			assertEquals("bitrate",Integer.parseInt((String)props.getProperty("bitrate")),header.bitrate());
			assertEquals("bitrate_string",(String)props.getProperty("bitrate_string"),header.bitrate_string());
			assertEquals("mode",Integer.parseInt((String)props.getProperty("mode")),header.mode());
			assertEquals("mode_string",(String)props.getProperty("mode_string"),header.mode_string());
			assertEquals("slots",Integer.parseInt((String)props.getProperty("slots")),header.slots());
			assertEquals("vbr",Boolean.valueOf((String)props.getProperty("vbr")),new Boolean(header.vbr()));
			assertEquals("vbr_scale",Integer.parseInt((String)props.getProperty("vbr_scale")),header.vbr_scale());
			assertEquals("max_number_of_frames",Integer.parseInt((String)props.getProperty("max_number_of_frames")),header.max_number_of_frames(mp3in.available()));
			assertEquals("min_number_of_frames",Integer.parseInt((String)props.getProperty("min_number_of_frames")),header.min_number_of_frames(mp3in.available()));
			assertTrue("ms_per_frame",Float.parseFloat((String)props.getProperty("ms_per_frame"))==header.ms_per_frame());
			assertTrue("frames_per_second",Float.parseFloat((String)props.getProperty("frames_per_second"))==(float) ((1.0 / (header.ms_per_frame())) * 1000.0));
			assertTrue("total_ms",Float.parseFloat((String)props.getProperty("total_ms"))==header.total_ms(mp3in.available()));
			assertEquals("SyncHeader",Integer.parseInt((String)props.getProperty("SyncHeader")),header.getSyncHeader());
			assertEquals("checksums",Boolean.valueOf((String)props.getProperty("checksums")),new Boolean(header.checksums()));
			assertEquals("copyright",Boolean.valueOf((String)props.getProperty("copyright")),new Boolean(header.copyright()));
			assertEquals("original",Boolean.valueOf((String)props.getProperty("original")),new Boolean(header.original()));
			assertEquals("padding",Boolean.valueOf((String)props.getProperty("padding")),new Boolean(header.padding()));
			assertEquals("framesize",Integer.parseInt((String)props.getProperty("framesize")),header.calculate_framesize());
			assertEquals("number_of_subbands",Integer.parseInt((String)props.getProperty("number_of_subbands")),header.number_of_subbands());
			in.closeFrame();
		}
		catch (BitstreamException e)
		{
			assertTrue("BitstreamException : "+e.getMessage(),false);
		}		
		catch (IOException e)
		{
			assertTrue("IOException : "+e.getMessage(),false);
		}		
	}
}
