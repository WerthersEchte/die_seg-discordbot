# DIE SEG (auction house)
_Die SEG_ functions as auction house discord bot.
_Die SEG_ sells letters to other _traders_ via auctions.

## running the bot

### gradle
Minimal required java version is ``jdk 9.+``.  
To run the bot, simply use  
``.\gradlew run``  
or  
``.\gradlew run --args="ENTER YOUR SECRET TOKEN"``  

### docker (Thanks to Rasmus Thomsen@Cogitri)

Running the bot is a matter of:

``docker build . -t seg``  
``docker run -e SECRET_TOKEN="MY_TOKEN" seg``


## Auctions
_Die SEG_ uses several commands for managing the letter auctions:

| response pattern | description | example
| ------------- |:-------------:| :-----|
|``!SEG auction start [id] [letter] [startbid]``  | starts an auction with _id_ for _letter_ with _startbid_ | ``!SEG auction start 1 A 0`` |
| ``!SEG auction closed [id]`` | ends an auction with _id_ if no one has bid        | ``!SEG auction closed 1`` |
| ``!SEG auction won [id] [traderId] [bid]`` | trader with _traderId_ won auction with _id_ for _bid_ price      | ``!SEG auction won 1 12516 2``|

## Interaction

Interaction with _die SEG_ is done via different commands:

| command pattern | description | example
| ------------- |:-------------:| :-----|
|``!SEG help``  | returns help about available commands | ``!SEG help`` |
| ``!SEG auction bid [id] [amount]`` | places a bid with _amount_ on auction with _id_       | ``!SEG auction bid 1 2`` |
| ``!SEG auction bid [id] [traderId] [bid]`` | _die SEG_ confirms new higher bids with       | ``!SEG auction bid 1 12516 2``|
| ``!SEG register`` | registers client as a trader and receives startingmoney      | ``!SEG register``|
| ``!SEG info traders`` | returns all registered traders      | ``!SEG info traders`` |
    
## Configuration

The _Die SEG_ bot can be configured via a ``config.json`` file in root. 
Available Attributes are:

| Attribute | description | default
| ------------- |:-------------:| :-----|
|``token``  | secret token for the discord bot |  |
|``admins``  | client tags who get admin role |  |
|``botName``  | name of the discord bot | "DIE SEG" |
|``startingPoints``  | starting money for new registered traders | 1000 |
|``auctionTime``  | time after last bid in millisec for closing auctions | 5000 |
|``auctionChannelName``  | name of the text channel for auction messages | "auction" |
|``infoChannelName``  | name of the text channel for info messages | "info" |
|``logChannelName``  | name of the text channel for log messages | "log" |
|``cmdPrefix``  | prefix for all bot commands | "!SEG" |
|``cmdAuction``  | command for auction commands | "auction" |
|``cmdInfo``  | command for info commands | "info" |
|``cmdRegister``  | command for register commands | "register" |
|``cmdKill``  | command for gracefully stopping the bot | "kill" |
|``cmdClear``  | command for clearing chats | "clear" |
|``cmdHelp``  | command for help | "help" |
|``cmdStatistics``  | command for getting stats | "stats" |
|``resStartAuction``  | bot response for starting auctions | "auction start %d %s" |
|``resEndAuction``  | bot response for ending auctions | "auction closed" |
|``resWonAuction``  | bot response for successful auctions | "auction won" |
|``resHigherBid``  | bot response after a higher bid was placed | "auction bid %d %d <@%s>" |
|``resRegistered``  | bot response after successful register | "registered" |
|``resMerchantInfo``  | bot response after requesting all traders | "info traders" |
