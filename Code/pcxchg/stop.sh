
docker-compose -f docker-compose-pcxchg.yaml down

docker rm $(docker ps -a)

docker ps -a 
