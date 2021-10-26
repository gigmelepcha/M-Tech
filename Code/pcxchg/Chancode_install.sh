#docker exec cli.Asus sh -c 'peer chaincode install -p pcxchg -n pcxchg -v 0 -l java'

docker exec cli.Asus sh -c 'peer chaincode install -p /opt/gopath/src/github.com/chaincode/ -n pcxchg -v 0 -l java'
docker exec cli.HP sh -c 'peer chaincode install -p /opt/gopath/src/github.com/chaincode/ -n pcxchg -v 0 -l java'
docker exec cli.Dell sh -c 'peer chaincode install -p /opt/gopath/src/github.com/chaincode/ -n pcxchg -v 0 -l java'
docker exec cli.Amazon sh -c 'peer chaincode install -p /opt/gopath/src/github.com/chaincode/ -n pcxchg -v 0 -l java'

#peer chaincode instantiate -C asus -n pcxchg -v 0 -c '{"Args":["init"]}' -P "OR('AsusMSP.member','DellMSP.member','HPMSP.member')" --collections-config /opt/gopath/src/github.com/chaincode/collections_config.json

#docker exec cli.Asus sh -c "peer chaincode instantiate -C asus -n pcxchg -v 0-c '{"Args":[]}'"
#docker exec cli.Dell sh -c "peer chaincode instantiate -C dell -n pcxchg -v 0.1 -c '{"Args":[]}'"

#docker exec cli.Asus sh -c "peer chaincode invoke -C asus -n pcxchg -c '{"Args":["add_members", "Doung"]}'"
#docker exec cli.Asus sh -c "peer chaincode invoke -C asus -n pcxchg -c '{"Args":["start","9"]}'"
#docker exec cli.Asus sh -c "peer chaincode invoke -C asus -n pcxchg -c '{"Args":["signup", ""]}'"
#Registration baeupdate_ballot
#docker exec cli.Asus sh -c "peer chaincode invoke -C asus -n pcxchg -c '{"Args":["update_candidates","lepcha","sairam","gurung","sherpa","tamang","botay"]}'"




#docker exec cli.Asus sh -c "peer chaincode invoke -C asus -n pcxchg -c '{"Args":["get_ballot"]}'"

#peer chaincode instantiate -o orderer.example.com:7050 --tls --cafile $ORDERER_CA -C mychannel -n medium -v 1.0 -c '{"Args":["init"]}' -P "OR('Org1MSP.member','Org2MSP.member')" --collections-config  $GOPATH/src/github.com/chaincode/medium/collections_config.json



#docker exec cli.Asus sh -c "peer chaincode invoke -C asus -n pcxchg -c '{"Args":["update_ballot"]}'"
#docker exec cli.Asus sh -c "peer chaincode invoke -C asus -n pcxchg -c '{"Args":["count"]}'"
#docker exec cli.Asus sh -c "peer chaincode invoke -C asus -n pcxchg -c '{"Args":["private"]}'"

#docker exec cli.Asus sh -c "peer chaincode invoke -C asus -n pcxchg -c '{"Args":["vote", "z"]}'"
#docker exec cli.Asus sh -c "peer chaincode invoke -C asus -n pcxchg -c '{"Args":["Registration", "Doung"]}'"


#docker exec cli.Asus sh -c "peer chaincode invoke -C asus -n pcxchg -c '{"Args":["vote","2","10","3"]}'"
 


