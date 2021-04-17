const { Pool } = require('pg')
const pool = new Pool({
    user: 'postgres',
    host: '172.18.0.2',
    database: 'dutchess',
    password: 'pass123',
    port: 5432,
  })

  pool.on('error', (err, client) => {
    console.error('Unexpected error on idle client', err)
    process.exit(-1)
  })


const saveWord = (wordTable, word) => 
    pool.query(`INSERT INTO words${wordTable} (word) VALUES ('${word.replace(/'/ig, '\'\'')}')`)
    

module.exports = { saveWord }

//console.log(res.rows[0].message) // Hello world!
//await client.end()