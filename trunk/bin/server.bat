color 17
title=tagzilla de.ama.server.services.impl.CrawlerServiceImpl
call setenv.bat
cls
set DEBUG=-Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005

%JAVA% -cp %CP% %DEBUG% de.ama.server.services.impl.CrawlerServiceImpl %1 %2 %3 %4 %5
pause
