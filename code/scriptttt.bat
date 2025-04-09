@echo off
setlocal enabledelayedexpansion

:: === Configuration des chemins ===
set "repertoire=C:\S5\GitHub\ticketing_2455\code"
set "src=%repertoire%\src"
set "lib=%repertoire%\lib"
set "web=%repertoire%\web"
set "xml=%repertoire%\web.xml"
set "nomAppli=ticketing"
set "webapps=C:\Program Files\Apache Software Foundation\Tomcat 10.1\webapps"
set "temp=C:\S5\GitHub\ticketing_2455\code_temp"
set "bin=%repertoire%\bin"

:: === Nettoyage complet ===
echo Nettoyage des repertoires...
rmdir /s /q "%temp%" 2>nul
rmdir /s /q "%bin%" 2>nul
mkdir "%bin%"
mkdir "%temp%"
mkdir "%temp%\WEB-INF"
mkdir "%temp%\WEB-INF\lib"
mkdir "%temp%\WEB-INF\classes"
mkdir "%temp%\WEB-INF\web"

:: === Copie des ressources ===
echo Copie des ressources...
xcopy /s /y "%lib%\*" "%temp%\WEB-INF\lib\" >nul
xcopy /s /y "%web%\*" "%temp%\" >nul
copy /y "%xml%" "%temp%\WEB-INF\" >nul

:: === Compilation ordonnée avec vérification ===
echo Compilation des sources Java...
set "classpath=%lib%\*;%bin%"

:: Liste des packages dans l'ordre de dépendance
set "packages=myconnection model controller test"

for %%p in (%packages%) do (
    echo [COMPILATION] Package: %%p
    if exist "%src%\%%p\" (
        for /r "%src%\%%p" %%f in (*.java) do (
            echo Traitement de %%~nxf...
            javac -parameters -cp "!classpath!" -d "%bin%" "%%f"
            if errorlevel 1 (
                echo [ERREUR] Echec compilation: %%~nxf
                pause
                exit /b 1
            )
            :: Vérification immédiate que le .class a été généré
            set "classfile=%bin%\%%~pf%%~nf.class"
            set "classfile=!classfile:%src%=!"
            if not exist "!classfile!" (
                echo [ERREUR] Fichier .class non généré: !classfile!
                pause
                exit /b 1
            )
        )
    ) else (
        echo [ATTENTION] Package %%p introuvable
    )
)

:: === Vérification ciblée de Vol.class ===
if not exist "%bin%\model\Vol.class" (
    echo [ERREUR CRITIQUE] Vol.class manquant dans %bin%\model\
    echo Verifiez:
    echo - Le fichier %src%\model\Vol.java existe
    echo - Aucune erreur de compilation ci-dessus
    pause
    exit /b 1
)

:: === Packaging final ===
echo Creation du WAR...
xcopy /s /y "%bin%\*" "%temp%\WEB-INF\classes\" >nul
cd /d "%temp%"
jar -cvf "%webapps%\%nomAppli%.war" * >nul

echo [SUCCES] Construction terminee - %nomAppli%.war deploye
pause