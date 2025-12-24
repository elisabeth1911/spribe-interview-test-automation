@ECHO OFF
SETLOCAL

SET BASE_DIR=%~dp0
SET WRAPPER_DIR=%BASE_DIR%\.mvn\wrapper
SET PROPERTIES_FILE=%WRAPPER_DIR%\maven-wrapper.properties
SET JAR_FILE=%WRAPPER_DIR%\maven-wrapper.jar

IF NOT EXIST "%PROPERTIES_FILE%" (
  ECHO Missing %PROPERTIES_FILE%
  EXIT /B 1
)

IF NOT EXIST "%JAR_FILE%" (
  FOR /F "usebackq tokens=1,* delims==" %%A IN ("%PROPERTIES_FILE%") DO (
    IF "%%A"=="wrapperUrl" SET WRAPPER_URL=%%B
  )
  IF "%WRAPPER_URL%"=="" (
    ECHO Missing wrapperUrl in %PROPERTIES_FILE%
    EXIT /B 1
  )
  ECHO Downloading Maven Wrapper jar...
  IF EXIST "%SystemRoot%\\System32\\WindowsPowerShell\\v1.0\\powershell.exe" (
    powershell -NoProfile -Command "(New-Object Net.WebClient).DownloadFile('%WRAPPER_URL%','%JAR_FILE%')"
  ) ELSE (
    ECHO PowerShell not found to download %WRAPPER_URL%
    EXIT /B 1
  )
)

IF NOT "%JAVA_HOME%"=="" (
  SET JAVA_EXE=%JAVA_HOME%\\bin\\java.exe
) ELSE (
  SET JAVA_EXE=java
)

"%JAVA_EXE%" -classpath "%JAR_FILE%" -Dmaven.multiModuleProjectDirectory="%BASE_DIR%" org.apache.maven.wrapper.MavenWrapperMain %*

ENDLOCAL
