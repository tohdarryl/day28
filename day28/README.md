# Day28 Lecture

## MongoDB on MAC terminal
1. Check for services
MacOS
```
brew services list 
```
Check for mongoDB and collection/table
```
mongosh
show databases
use 'database'
show collections
db.'collection/table'.findOne()
```

2. Start server
```
brew services start mongodb-community@6.0
```

3. Import data into MongoDB
```
mongoimport "mongodb://localhost:27017" --drop -d bgg -c comments --jsonArray --file comment.json
mongoimport "mongodb://localhost:27017" -d bgg -c games --jsonArray --file game.json
```

--drop if exists
-d database
-c collection/table
--jsonArray type of data 


## PM
```
1. Log into Railway website
2. Create Project
3. Add MongoDB service
4. Go to Studio 3T
5. New Connection
6. Paste mongo url from Railway MongoDB 'connect'. i.e. Mongo Connection URL "<url>"
7. Set connection name. e.g. mongo@playstore
8. Make sure you are on Hotspot or NUS ISS MOBILE wifi to connect
9. If project has been created on railway website, use railway link
```

Importing .csv file into Railway mongoDB database
```
mongoimport --authenticationDatabase=admin "<url>" -d <db_name> -c <collection> --type=csv --headerline --file googleplaystore.csv
```