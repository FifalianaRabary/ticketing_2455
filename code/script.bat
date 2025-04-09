@echo off
setlocal enabledelayedexpansion

REM === CONFIGURATION DES CHEMINS ===
set "repertoire=C:\S5\GitHub\ticketing_2455\code"
set "src=%repertoire%\src"
set "lib=%repertoire%\lib"
set "web=%repertoire%\web"
set "xml=%repertoire%\web.xml"
set "nomAppli=ticketing"
set "webapps=C:\Program Files\Apache Software Foundation\Tomcat 10.1\webapps"
set "temp=%repertoire%_temp"
set "bin=%repertoire%\bin"

REM === NETTOYAGE ET PREPARATION ===
echo Nettoyage des dossiers...
rmdir /s /q "%temp%" 2>nul
rmdir /s /q "%bin%" 2>nul
mkdir "%temp%\WEB-INF\classes"
mkdir "%temp%\WEB-INF\lib"
mkdir "%bin%"

REM === COPIE DES FICHIERS ===
echo Copie des fichiers...
xcopy /s /y "%web%\*" "%temp%" >nul
copy /y "%xml%" "%temp%\WEB-INF" >nul
xcopy /s /y "%lib%\*" "%temp%\WEB-INF\lib" >nul

REM === COMPILATION SPECIALISEE ===
echo Compilation des classes...

REM 1. Compiler d'abord les classes sans dépendances
for %%f in ("%src%\myconnection\*.java") do (
    echo Compilation de %%f
    javac -parameters -cp "%lib%\*" -d "%bin%" "%%f"
    if errorlevel 1 (
        echo ERREUR de compilation dans %%f
        pause
        exit /b 1
    )
)

REM 2. Compiler les classes model indépendantes
set "modelDir=%src%\model"
set "modelFiles=Utilisateur.java Modele.java TypeSiege.java Ville.java Avion.java PromotionSiege.java RegleAnnulationReservation.java RegleReservation.java Vol.java  PrixVolEnfant.java"

for %%f in (%modelFiles%) do (
    echo Compilation de %modelDir%\%%f
    javac -parameters -cp "%lib%\*;%bin%" -d "%bin%" "%modelDir%\%%f"
    if errorlevel 1 (
        echo ERREUR de compilation dans %modelDir%\%%f
        pause
        exit /b 1
    )
)

REM 3. Compiler les classes interdépendantes ensemble
echo Compilation des classes interdépendantes...
javac -parameters -cp "%lib%\*;%bin%" -d "%bin%" "%modelDir%\Reservation.java" "%modelDir%\DetailReservation.java"
if errorlevel 1 (
    echo ERREUR de compilation des classes interdépendantes
    pause
    exit /b 1
)

REM 4. Compiler les controllers
for %%f in ("%src%\controller\*.java") do (
    echo Compilation de %%f
    javac -parameters -cp "%lib%\*;%bin%" -d "%bin%" "%%f"
    if errorlevel 1 (
        echo ERREUR de compilation dans %%f
        pause
        exit /b 1
    )
)

REM === VERIFICATION COMPILATION ===
echo Vérification des fichiers compilés...
dir /s "%bin%"
pause

REM === PREPARATION DU WAR ===
echo Copie des classes compilées...
xcopy /s /y "%bin%\*" "%temp%\WEB-INF\classes" >nul

echo Structure du dossier temporaire:
tree "%temp%"
pause

REM === GENERATION DU WAR ===
echo Création du fichier WAR...
cd /d "%temp%"
jar -cvf "%webapps%\%nomAppli%.war" * > "%temp%\war_creation.log"

if not exist "%webapps%\%nomAppli%.war" (
    echo ERREUR: création du WAR a échoué
    type "%temp%\war_creation.log"
    pause
    exit /b 1
)

echo Contenu du WAR:
"C:\Program Files\Java\jdk-17\bin\jar.exe" -tf "%webapps%\%nomAppli%.war" > "%temp%\war_content.log"
type "%temp%\war_content.log"

REM === DEPLOIEMENT ===
echo.
echo === Déploiement terminé ===
echo Fichier WAR: %webapps%\%nomAppli%.war
echo.
echo Vérifiez dans le manager Tomcat: http://localhost:8080/manager/html
echo Ou accédez directement à: http://localhost:8080/%nomAppli%/
pause