$ErrorActionPreference = "Stop"

Set-Location $PSScriptRoot

$ServerIp = "15.165.113.0"
$SshUser = "ubuntu"
$SshKey = Join-Path $HOME ".ssh\duntalk-github-actions"

Write-Host "[1/4] Build backend"

& .\gradlew.bat clean bootJar

if ($LASTEXITCODE -ne 0) {
    throw "Gradle bootJar failed."
}

$JarFile = Get-ChildItem ".\build\libs\*.jar" |
    Where-Object { $_.Name -notlike "*-plain.jar" } |
    Sort-Object LastWriteTime -Descending |
    Select-Object -First 1

if (-not $JarFile) {
    throw "JAR file not found."
}

Write-Host "[2/4] Upload JAR"

& scp -i $SshKey `
    $JarFile.FullName `
    "${SshUser}@${ServerIp}:/tmp/duntalk-app.jar"

if ($LASTEXITCODE -ne 0) {
    throw "JAR upload failed."
}

Write-Host "[3/4] Replace JAR and restart service"

$RemoteCommand = "sudo cp /opt/duntalk/app.jar /opt/duntalk/app.jar.bak && sudo mv /tmp/duntalk-app.jar /opt/duntalk/app.jar && sudo chown ubuntu:ubuntu /opt/duntalk/app.jar && sudo systemctl restart duntalk && sleep 20 && sudo systemctl is-active --quiet duntalk && sudo ss -ltn | grep -q ':8080 '"

& ssh -i $SshKey "${SshUser}@${ServerIp}" $RemoteCommand

if ($LASTEXITCODE -ne 0) {
    throw "Server deployment failed. Check: journalctl -u duntalk"
}

Write-Host "[4/4] Deployment completed"