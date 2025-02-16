@echo off
set "repertoire=C:\S5\GitHub\ticketing_2455\code"
set "src=%repertoire%\src"
set "lib=%repertoire%\lib"
set "web=%repertoire%\web"
set "xml=%repertoire%\web.xml"
set "nomAppli=ticketing"
set "webapps=C:\Program Files\Apache Software Foundation\Tomcat 10.1\webapps"
set "temp=C:\S5\GitHub\ticketing_2455\code_temp"
set "bin=%repertoire%\bin"

rmdir /s /q "%temp%"

mkdir "%temp%"

mkdir "%temp%\WEB-INF"

mkdir "%temp%\WEB-INF\lib"

mkdir "%temp%\WEB-INF\web"

mkdir "%temp%\WEB-INF\classes"

xcopy /s /y "%lib%" "%temp%\WEB-INF\lib"
@REM xcopy /s /y "%web%" "%temp%\WEB-INF\web"
xcopy /s /y "%web%" "%temp%"
copy  /y "%xml%" "%temp%\WEB-INF"

@REM compiler les model et mettre .classes dans bin
@REM javac -cp "%lib%\*" -d "%bin%" "%src%\model\*.java"

@REM compiler les controller avec lib et bin
javac -parameters -cp "%lib%\*;%bin%" -d "%bin%" "%src%\myconnection\*.java"
javac -parameters -cp "%lib%\*;%bin%" -d "%bin%" "%src%\controller\*.java"

@REM copier ce qu'il y a dans bin dans classes
xcopy /s /y "%bin%" "%temp%\WEB-INF\classes"

cd /d "%temp%"
jar -cvf "%webapps%\%nomAppli%.war" *


pause
