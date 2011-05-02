/*
 *   SkipTest - JavaZOOM : http://www.javazoom.net
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Properties;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import junit.framework.TestCase;

/**
 * Skip bytes before playing.
 */
public class SkipTest extends TestCase
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
	public SkipTest(String arg0)
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
		out = System.out;
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	public void testSkipFile()
	{
		if (out != null) out.println("-> Filename : "+filename+" <-");
        try
        {
            File file = new File(filename);
            AudioFileFormat aff = AudioSystem.getAudioFileFormat(file);
            AudioInputStream in = AudioSystem.getAudioInputStream(file);
            AudioInputStream din = null;
            AudioFormat baseFormat = in.getFormat();
            if (out != null) out.println("Source Format : "+baseFormat.toString());
            AudioFormat  decodedFormat = new AudioFormat(
                  AudioFormat.Encoding.PCM_SIGNED,
                  baseFormat.getSampleRate(),
                  16,
                  baseFormat.getChannels(),
                  baseFormat.getChannels() * 2,
                  baseFormat.getSampleRate(),
                  false);
            if (out != null) out.println("Target Format : "+decodedFormat.toString());
            din = AudioSystem.getAudioInputStream(decodedFormat, in);            
            long toSkip = (long)(file.length()/2);
            long skipped = skip(din,toSkip);
            if (out != null) out.println("Skip : "+skipped+"/"+toSkip+" (Total="+file.length()+")");
            if (out != null) out.println("Start playing");
            rawplay(decodedFormat, din);
            in.close();       
            if (out != null) out.println("Played");
            assertTrue("testSkip : OK",true);            
        }
        catch (Exception e)
        {
            assertTrue(e.getMessage(),false);        
        }
	}
    
    private long skip(AudioInputStream in, long bytes) throws IOException
    {
        long SKIP_INACCURACY_SIZE = 1200;
        long totalSkipped = 0;
        long skipped = 0;
        while (totalSkipped < (bytes - SKIP_INACCURACY_SIZE))
        {
            skipped = in.skip(bytes - totalSkipped);
            if (skipped == 0) break;
            totalSkipped = totalSkipped + skipped;
        }
        return totalSkipped;        
    }

    private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException
    {
      SourceDataLine res = null;
      DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
      res = (SourceDataLine) AudioSystem.getLine(info);
      res.open(audioFormat);
      return res;
    }
    
    private void rawplay(AudioFormat targetFormat, AudioInputStream din) throws IOException, LineUnavailableException
    {
        byte[] data = new byte[4096];
        SourceDataLine line = getLine(targetFormat);        
        if (line != null)
        {
          // Start
          line.start();
          int nBytesRead = 0, nBytesWritten = 0;
          while (nBytesRead != -1)
          {
            nBytesRead = din.read(data, 0, data.length);
            if (nBytesRead != -1) nBytesWritten = line.write(data, 0, nBytesRead);
          }
          // Stop
          line.drain();
          line.stop();
          line.close();
          din.close();
        }       
    }

}
