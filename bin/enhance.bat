echo off
rem ##############################################################
rem this batch starts the gna-jorm enhancer.
rem Andreas Marochow     andreas@marochow.de
rem ##############################################################

color 75
title=enhancer
call setenv.bat
set CP=%CP%;%LIB%\javassist.jar

%JAVA% -cp %CP% -Xmx64M de.ama.db.tools.Enhancer inDir %HOME%\classes verbose 1
pause
echo all classes enhanced OK
