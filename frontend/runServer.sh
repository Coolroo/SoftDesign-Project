cd "${BASH_SOURCE}"
npm run build &
wait
pm2 start npm --name "next" -- start
