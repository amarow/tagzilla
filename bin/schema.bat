echo off
rem ##############################################################
rem this batch starts the gna-jorm schema tool.
rem Andreas Marochow     andreas@marochow.de
rem ##############################################################

color 75
title=schema tool
call setenv.bat

%JAVA% -cp %CP% -Xmx64M de.ama.db.tools.SchemaManager tagzilla

