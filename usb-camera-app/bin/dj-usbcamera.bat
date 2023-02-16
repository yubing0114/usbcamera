@echo off

if "%1"=="hide" goto CmdBegin
start mshta vbscript:createobject("wscript.shell").run("""%~0"" hide",0)(window.close)&&exit
:CmdBegin

title usb-camera

setlocal & pushd
taskkill -f -t -im java.exe

set PATH=%cd%
echo %PATH%
cd ..
set PATH=%cd%
echo %PATH%

taskkill -f -t -im java.exe
%PATH%\jre\bin\java -Xms128m -Xmx512m -jar %PATH%\dj-usb-camera-1.0.0.jar

endlocal & popd
pause



