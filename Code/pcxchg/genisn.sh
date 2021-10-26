#creating gensis block and channel blocks

configtxgen -profile PCXCHGOrdererGenesis -outputBlock ./orderer/genesis_block.pb -channelID asuschannel


configtxgen -profile asuschannel -outputCreateChannelTx ./channels/asuschannel.pb -channelID asus




configtxgen -profile asuschannel -outputAnchorPeersUpdate ./channels/asus_asusanchor.pb -channelID asus -asOrg AsusMSP
configtxgen -profile asuschannel -outputAnchorPeersUpdate ./channels/asus_dellanchor.pb -channelID asus -asOrg DellMSP
configtxgen -profile asuschannel -outputAnchorPeersUpdate ./channels/asus_hpanchor.pb -channelID asus -asOrg HPMSP
configtxgen -profile asuschannel -outputAnchorPeersUpdate ./channels/asus_amazonanchorasus.pb -channelID asus -asOrg AmazonMSP


