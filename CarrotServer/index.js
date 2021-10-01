const express = require('express')
const app = express()
const mysql = require('mysql')
const connection = mysql.createConnection({
  host: 'localhost',
  user: 'root',
  password: 'ehduq8234',
  database: 'carrot'
})

app.use(express.json())
app.use(express.urlencoded({ extended: true }));
app.use(function (req, res, next) {
  res.header('Access-Control-Allow-Origin', req.header('Origin'))
  res.header('Access-Control-Allow-Credentials', true)
  res.header('Access-Control-Allow-Methods', 'GET, PUT, POST, DELETE')
  res.header('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept, Authorization')
  next()
})


app.get('/preview', function (req, res) {
  connection.query('select a.id, a.title, b.location, a.date, a.price, max(c.url) as url from product as a join user as b join picture as c on a.userid = b.id and a.id = c.productid', (err, results, fields) => {
    if (err) res.json({ error: err })
    else res.json({ data: results })
  })
})

app.get('/categories', function (req, res) {
  connection.query('select id, title from category', (err, results, fields) => {
    if (err) res.json({ error: err })
    else res.json({ data: results })
  })
})

app.get('/detail', function (req, res) {
  connection.query(`select b.pic, b.id, b.location, a.title, a.category, a.date, a.descr, a.price from product as a join user as b join picture as c on a.userid = b.id and a.id = c.productid where a.id = ${req.query.id}`, (err, results, fields) => {
    if (err) res.json({ error: err })
    else res.json({ data: results })
  })
})

app.get('/detail_pic', function (req, res) {
  connection.query(`select b.url from product as a join picture as b on a.id = b.productid where a.id = ${req.query.id}`, (err, results, fields) => {
    if (err) res.json({ error: err })
    else res.json({ data: results })
  })
})

app.post('/product', function (req, res) {
  console.log(req)
  connection.query(`insert into product(userid, title, category, descr, date, price) values('${req.body.userid}', '${req.body.title}', ${req.body.category}, '${req.body.descr}', now(), ${req.body.price})`, (err, results, fields) => {
    if (err) {
      console.log(err)
      res.json({ error: err })
    }
    else {
      console.log(results)
      res.json({ data: results })
    }
  })
})

app.put('/product', function(req, res) {

})

app.delete('/product', function(req, res) {

})

app.listen(3000)