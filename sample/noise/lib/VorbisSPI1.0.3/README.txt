-----------------------------------------------------
 Vorbis SPI.

 Project Homepage :
   http://www.javazoom.net/vorbisspi/vorbisspi.html 

 Online MP3, OGG Vorbis Forum :
   http://www.javazoom.net/services/forums/index.jsp
-----------------------------------------------------

Vorbis SPI adds OGG Vorbis capabilities to JavaSound API.
It is based on JOrbis library (Java Ogg Vorbis decoder).

How to install it :
-----------------
Add vorbisspi1.0.3.jar, tritonus_share.jar, jorbis-0.0.15.jar, jogg-0.0.7.jar into
your runtime CLASSPATH. Your application should rely on JavaSound API only. The
Java Virtual Machine will load the VorbisSPI at runtime.


Known problems :
--------------
- Low sampling rates such as 14Khz are not supported.
- AudioInputStream is closed at the end of song for some icecast streams 
  with title streaming enabled.


Changes :
-------

 05/27/2008 : VorbisSPI 1.0.3
 -----------------------------------
 - SPI compatibility bug fix.


 11/23/2007 : VorbisSPI 1.0.3-Debian
 -----------------------------------
 - Tritonus share update support.


 10/01/2005 : VorbisSPI 1.0.2
 ----------------------------
 - UTF-8 support added for Ogg comments.
 + JOrbis 0.0.15 included.
   

 11/02/2004 : VorbisSPI 1.0.1
 ----------------------------
 + JOrbis 0.0.14 included.
   It fixes a file lock bug.


 04/05/2004 : VorbisSPI 1.0
 --------------------------
 - Custom information (bitrate, ...), available through AudioFileFormat.getType(), 
   workaround has been removed. Use TAudioFormat.properties() and 
   TAudioFileFormat.properties() now. Here are all new parameters :
     AudioFormat parameters  :
     ~~~~~~~~~~~~~~~~~~~~~~
     - bitrate : [Integer], bitrate in bits per seconds, average bitrate for VBR enabled stream.
     - vbr : [Boolean], VBR flag

     AudioFileFormat parameters :
     ~~~~~~~~~~~~~~~~~~~~~~~~~~
     + Standard parameters :
       - duration : [Long], duration in microseconds.
       - title : [String], Title of the stream.
       - author : [String], Name of the artist of the stream.
       - album : [String], Name of the album of the stream.
       - date : [String], The date (year) of the recording or release of the stream.
       - copyright : [String], Copyright message of the stream.
       - comment : [String], Comment of the stream.
    + Extended Ogg Vorbis parameters :
       - ogg.length.bytes : [Integer], length in bytes.
       - ogg.bitrate.min.bps : [Integer], minimum bitrate.
       - ogg.bitrate.nominal.bps : [Integer], nominal bitrate.
       - ogg.bitrate.max.bps :  [Integer], maximum bitrate.
       - ogg.channels : [Integer], number of channels 1 : mono, 2 : stereo.
       - ogg.frequency.hz :  [Integer], sampling rate in hz.
       - ogg.version : [Integer], version.
       - ogg.serial : [Integer], serial number.
       - ogg.comment.track : [String], track number.
       - ogg.comment.genre : [String], genre field.
       - ogg.comment.encodedby : [String], encoded by field.
       - ogg.comment.ext : [String], extended comments (indexed): 
         For instance : 
          ogg.comment.ext.1=Something 
          ogg.comment.ext.2=Another comment 

    DecodedVorbisAudioInputStream parameters :
    ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    - ogg.position.byte : [Long], current position in bytes in the stream.


 03/15/2004 : VorbisSPI 0.9
 --------------------------
 - Hang bug fixed for some Ogg Vorbis songs.


 11/11/2003 : VorbisSPI 0.8
 ---------------------------
 - WAV/AU SPI conflict bug fixed.
 - AudioInputStream.available() bug fixed.
 - Custom information available through AudioFileFormat.getType() workaround :
    VORBISxNominalBitRateInBpsxLengthInMilliSeconds (e.g. VORBISx128000x282267)
    Note that this workaround will be removed in VorbisSPI 1.0. Another workaround
    to pass extra parameters (Ogg comments, Bitrates, ... ) will be available and 
    compliant with JDK 1.5.

 - Design improved :
    tritonus_share.jar included (old Tritonus classes removed).
    TDebug class used for debugging traces.
    (Use -Dtritonus.TraceAudioFileReader=true to enable traces)
    jUnit classes included.


 04/15/2003 : VorbisSPI 0.7a
 ---------------------------
 META-INF/services folder fixed.


 03/24/2003 : VorbisSPI 0.7
 --------------------------
 - OGG Vorbis streaming support improved.
 + JOrbis 0.0.12 included.


 03/04/2002 : VorbisSPI 0.6
 --------------------------
 - OGG Vorbis streaming support improved.
 - Nominal BitRate added to encoding type.
 - File length returned.
 + JOrbis 0.0.11 included.


 10/01/2001 : VorbisSPI 0.5
 --------------------------
 + JOrbis 0.0.8 included.
 Project started. It is licensed under LGPL.


Note :
-----
Ogg Vorbis is a fully Open, non-proprietary, patent-and-royalty-free,
general-purpose compressed audio format for high quality (44.1-48.0kHz,
16+ bit, polyphonic) audio and music at fixed and variable bitrates
from 16 to 128 kbps/channel. This places Vorbis in the same class as audio
representations including MPEG-1 audio layer 3, MPEG-4 audio (AAC
and TwinVQ), and PAC.
Vorbis is the first of a planned family of Ogg multimedia coding formats
being developed as part of Xiphophorus's Ogg multimedia  project.
