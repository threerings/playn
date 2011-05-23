/*
 * 11/19/04		1.0 moved to LGPL.
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

import javazoom.jl.decoder.BitstreamTest;
import javazoom.jl.player.jlpTest;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * JavaLayer test suite.
 */
public class AllTests
{
	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test for javazoom.jl.decoder");
		//$JUnit-BEGIN$
		suite.addTest(new TestSuite(BitstreamTest.class));
		suite.addTest(new TestSuite(jlpTest.class));
		//$JUnit-END$
		return suite;
	}
}
