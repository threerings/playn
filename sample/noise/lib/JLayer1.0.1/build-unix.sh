#!/bin/sh
#######################################################
#         JLayer 1.0.1 Un*x Build Script
#
# Project Homepage :
#   http://www.javazoom.net/javalayer/javalayer.html
#
# Java and MP3 online Forums :
#   http://www.javazoom.net/services/forums/index.jsp
#
#######################################################

# JAVA_HOME and JL must be set below
JAVA_HOME=/usr/local/java/jdk1.3.1
JL=/home/javazoom/JLayer1.0.1

#---------------------------
# Do not modify lines below
#---------------------------
CLASSPATH=$JAVA_HOME/lib/tools.jar
PATH=$PATH:$JAVA_HOME/bin
JLDECODERSRC=$JL/src/javazoom/jl/decoder
JLCONVERTERSRC=$JL/src/javazoom/jl/converter
JLSIMPLEPLAYER=$JL/src/javazoom/jl/player
JLADVPLAYER=$JL/src/javazoom/jl/player/advanced
javac -classpath $CLASSPATH:$JL/classes -d $JL/classes $JLDECODERSRC/*.java
javac -classpath $CLASSPATH:$JL/classes -d $JL/classes $JLCONVERTERSRC/*.java
cd $JLDECODERSRC
cp *.ser $JL/classes/javazoom/jl/decoder

# MP3 Simple + Advanced Player support :
#
# Comment both lines below for JDK1.1.x or JDK 1.2.x
cd $JLSIMPLEPLAYER
javac -classpath $JL/classes -d $JL/classes *.java
cd $JLADVPLAYER
javac -classpath $JL/classes -d $JL/classes *.java

# Jar Generation
cd $JL/classes
jar cvf ../jl1.0.1.jar *
cd $JL
