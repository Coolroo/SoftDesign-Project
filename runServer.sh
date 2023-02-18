./frontend/runServer.sh &
echo "Compiling & running frontend"
wait
./backend/runServer.sh &
echo "Compiling & running backend"
wait
echo "Servers started"
