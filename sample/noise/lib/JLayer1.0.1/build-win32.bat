rem #######################################################
rem #         JLayer 1.0.1 WIN32 Build Script
rem #
rem # Project Homepage :
rem #   http://www.javazoom.net/javalayer/javalayer.html 
rem #
rem # Java and MP3 online Forums :
rem #   http://www.javazoom.net/services/forums/index.jsp
rem #
rem #######################################################

rem # JAVA_HOME and JL must be set below
set JAVA_HOME=c:\jdk1.3.1
set JL=c:\JLayer1.0.1

rem #---------------------------
rem # Do not modify lines below
rem #---------------------------
set CLASSPATH=%JAVA_HOME%\lib\tools.jar
set PATH=%PATH%;%JAVA_HOME%\bin
set JLDECODERSRC=%JL%\src\javazoom\jl\decoder
set JLCONVERTERSRC=%JL%\src\javazoom\jl\converter
set JLSIMPLEPLAYER=%JL%\src\javazoom\jl\player
set JLADVPLAYER=%JL%\src\javazoom\jl\player\advanced
javac -classpath %CLASSPATH%;%JL%\classes -d %JL%\classes %JLDECODERSRC%\*.java
javac -classpath %CLASSPATH%;%JL%\classes -d %JL%\classes %JLCONVERTERSRC%\*.java
cd %JLDECODERSRC%
copy *.ser %JL%\classes\javazoom\jl\decoder

rem # MP3 Simple + Advanced Player support :
rem #
rem # Comment both lines below for JDK1.1.x or JDK 1.2.x
cd %JLSIMPLEPLAYER%
javac -classpath %JL%\classes -d %JL%\classes *.java

cd %JLADVPLAYER%
javac -classpath %JL%\classes -d %JL%\classes *.java

rem # JAR Generation
cd %JL%\classes
jar cvf ..\jl1.0.1.jar *
cd %JL%
