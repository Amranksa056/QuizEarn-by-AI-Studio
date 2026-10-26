@echo off
title QuizEarn
cd /d "%~dp0"

rem Open the app a moment after the server boots.
start "" /b cmd /c "timeout /t 2 >nul & start http://localhost:5173/"

npm start
pause
