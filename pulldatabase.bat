@if "%DEBUG%" == "" @echo off
@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal
@rem ##########################################################################
@rem  Script configuration:
set PACKAGE_NAME=es.jpv.android.examples.loadersexample
set DB_NAME=dummyitemsdata.db
set ADB_EXE=C:\Users\0010120\AppData\Local\Android\sdk\platform-tools\adb.exe
set DB_BROWSER_EXE=C:\Users\0010120\Downloads\SQLiteDatabaseBrowserPortable\SQLiteDatabaseBrowserPortable.exe
set ABE_JAR=C:\Users\0010120\AndroidStudioProjects\android-backup-extractor\build\libs\abe-all.jar
set SEVENZIP_EXE=C:\utils\7-Zip\7z.exe
set DB_PATH=apps\%PACKAGE_NAME%\db
@rem ##########################################################################

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windowz variants

if not "%OS%" == "Windows_NT" goto win9xME_args
if "%@eval[2+2]" == "4" goto 4NT_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*
goto execute

:4NT_args
@rem Get arguments from the 4NT Shell from JP Software
set CMD_LINE_ARGS=%$

:execute
@rem Retrieve data from phone, unpack data and open database file
echo Extracting DB from phone...
"%ADB_EXE%" backup -f ~/backup.ab -noapk %PACKAGE_NAME%
echo Unpacking Android backup...
"%JAVA_EXE%" -jar "%ABE_JAR%" unpack ~/backup.ab ~/backup.tar
echo Untaring app data...
"%SEVENZIP_EXE%" e -y ~/backup.tar -o~/ %DB_PATH%\%DB_NAME%
echo Opening the DB in viewer...
start "" "%DB_BROWSER_EXE%" ~/%DB_NAME%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
echo Extraction finished!