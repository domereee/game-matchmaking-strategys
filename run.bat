@echo off
rem Build (if needed) and start the live demo. Example: run.bat --reset
cd /d "%~dp0"
if not exist target\strategy-demo.jar call mvn -q package
java -jar target\strategy-demo.jar %*
