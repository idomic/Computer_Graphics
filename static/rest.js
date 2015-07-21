// console.log('Hello');
//var items = require('./items.js');
var express = require('express');
var app = express();
var port = process.env.PORT || 8080;


app.get('/items', function(req, res){
	console.log('GET');
	res.json(items.get());
});

app.use(express.static(__dirname));
//app.use(express.bodyParser());


app.put('/items/:itemId',function(req, res){
	console.log('PUT');
	res.json(items.add(req.params.itemId,req.body));
});

app.delete('/items/:itemId',function(req, res){
	console.log("DELETE : " + req.params.itemId);
	res.json(items.remove(req.params.itemId));
});

app.listen(port);