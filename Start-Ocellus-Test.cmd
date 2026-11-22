@echo off
setlocal
set "JAVA_EXE=%LOCALAPPDATA%\RuneLite\jre\bin\java.exe"
if not exist "%JAVA_EXE%" set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"
if not exist "%JAVA_EXE%" (
  echo RuneLite Java runtime not found. Install RuneLite or set JAVA_HOME.
  pause
  exit /b 1
)
"%JAVA_EXE%" -ea -cp "%~dp0lib\*" com.docsultan.ocellusstart.OcellusStartLauncher --developer-mode
pause
