
 #!/bin/bash

start=`date +%s`


for((i=1;i<=100;i++)); 
do
  
  docker exec cli.Dell sh -c "peer chaincode invoke -C asus -n pcxchg -c '{\"Args\":[\"get_ballot\"]}'"
  docker exec cli.Dell sh -c "peer chaincode invoke -C asus -n pcxchg -c '{\"Args\":[\"update_ballot\"]}'"
  docker exec cli.Dell sh -c "peer chaincode invoke -C asus -n pcxchg -c '{\"Args\":[\"get_ballot\"]}'"
  docker exec cli.Dell sh -c "peer chaincode invoke -C asus -n pcxchg -c '{\"Args\":[\"update_ballot\"]}'"
  docker exec cli.Dell sh -c "peer chaincode invoke -C asus -n pcxchg -c '{\"Args\":[\"update_ballot\"]}'"
  docker exec cli.Dell sh -c "peer chaincode invoke -C asus -n pcxchg -c '{\"Args\":[\"get_ballot\"]}'"

done
end=`date +%s`
runtime=$((end-start))
echo "Exucution time $runtime"
