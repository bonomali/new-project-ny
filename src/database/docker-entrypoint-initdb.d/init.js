// initialization goes here
db = db.getSiblingDB('nyst');
db.item.insertOne({_id: 1, name: "one"});
db.item.insertOne({_id: 2, name: "two"});
db.item.insertOne({_id: 3, name: "three"});
db.item.insertOne({_id: 4, name: "four"});
db.item.insertOne({_id: 5, name: "five"});
