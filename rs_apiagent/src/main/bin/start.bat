@ECHO OFF
set CLASSPATH=F:\dev\api-agent\damoaClient\;F:\dev\api-agent\damoaClient\lib\ojdbc6.jar;F:\dev\api-agent\damoaClient\lib\damoa-api-agent-1.0-SNAPSHOT.jar

"C:\Program Files\Java\jdk1.6.0_45\bin\java" -classpath %CLASSPATH% com.damoa.DamoaAgent
