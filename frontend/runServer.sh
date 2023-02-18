cd /home/coolroo/repos/SoftDesign-Project/frontend
npm run build &
wait
pm2 start npm --name "next" -- start
