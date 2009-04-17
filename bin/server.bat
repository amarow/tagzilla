color 17
title=tagzilla Server
call setenv.bat
cls
set DEBUG=-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005

%JAVA% -cp %CP% %DEBUG% de.ama.server.Starter
pause
