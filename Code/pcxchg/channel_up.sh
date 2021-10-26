docker exec -it cli.Amazon sh -c 'peer channel create -c asus -f ./channels/asuschannel.pb -o orderer.pcxchg.com:7050'

docker exec -it cli.Amazon sh -c 'peer channel join -b asus.block'

docker exec cli.Asus sh -c 'peer channel join -b asus.block'

docker exec cli.HP sh -c 'peer channel join -b asus.block'


docker exec cli.Dell sh -c 'peer channel join -b asus.block'



docker exec cli.Asus sh -c 'peer channel update -o orderer.pcxchg.com:7050 -c asus -f ./channels/asus_asusanchor.pb'

docker exec cli.Dell sh -c 'peer channel update -o orderer.pcxchg.com:7050 -c asus -f ./channels/asus_dellanchor.pb'


docker exec cli.HP sh -c 'peer channel update -o orderer.pcxchg.com:7050 -c asus -f ./channels/asus_hpanchor.pb'


docker exec cli.Amazon sh -c 'peer channel update -o orderer.pcxchg.com:7050 -c asus -f ./channels/asus_amazonanchorasus.pb'


