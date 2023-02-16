@echo off

title ${project.artifactId}

setlocal & pushd

set JAVA_OPTS=-Dspring.profiles.include=core,dev
if "${project.artifactId}" == "usb-camera" (
java -Xms128m -Xmx512m -jar ./../usb-camera-1.0.0.jar
)  else  (
java -Xms128m -Xmx512m -jar ./../%JAVA_OPTS% usb-camera-1.0.0.jar
)

endlocal & popd
pause



