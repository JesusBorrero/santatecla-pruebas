cd ../front

# Remove old generated resources
Remove-Item dist\front\* -Force -Recurse
Remove-Item ..\back\src\main\resources\static\* -Force -Recurse

# Build angular app
docker run --rm --name angular-cli -v ${PWD}:/angular -w /angular node /bin/bash -c "npm install; npm run build"

cd ../back

# Copy new generated resources on back static
xcopy /Y /s ..\front\dist\front\* src\main\resources\static\

# Create jar
docker run -it --rm -v ${PWD}:/usr/src/project -w /usr/src/project maven:alpine mvn -DskipTests package
xcopy /Y target\back-0.0.1-SNAPSHOT.jar ..\docker\build\

cd ../docker

# Build new santatecla image
docker build -t sigma98/santatecla .

docker-compose up