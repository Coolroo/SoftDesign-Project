cd "${BASH_SOURCE}"
./gradlew bootJar &
wait
nohup java -jar ./build/libs/plagueinc-0.0.1-SNAPSHOT.jar > my.log 2>&1 & 
echo "$!" > save_pid.txt
