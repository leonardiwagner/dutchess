const { Pool } = require('pg')
const pool = new Pool({
    user: 'postgres',
    host: '',
    database: 'dutchess',
    password: '',
    port: 5432,
  })

  pool.on('error', (err, client) => {
    console.error('Unexpected error on idle client', err)
    process.exit(-1)
  })

let state
const setState = (_state) => { state = _state}

const bulk = [
    [], [], [], [], [], [], [], [], [], []
]

const doBulk = () => {
  {
   
    const local = [
      [], [], [], [], [], [], [], [], [], []
    ]
  
    const sliceSize = bulk[0].length > state.INSERTS_SIZE ? state.INSERTS_SIZE : bulk[0].length
    
  
    local[0] = bulk[0].splice(0, sliceSize)
    local[1] = bulk[1].splice(0, sliceSize)
    local[2] = bulk[2].splice(0, sliceSize)
    local[3] = bulk[3].splice(0, sliceSize)
    local[4] = bulk[4].splice(0, sliceSize)
    local[5] = bulk[5].splice(0, sliceSize)
    local[6] = bulk[6].splice(0, sliceSize)
    local[7] = bulk[7].splice(0, sliceSize)
    local[8] = bulk[8].splice(0, sliceSize)
    local[9] = bulk[9].splice(0, sliceSize)
  
    if(local[0].length === 0) return
  
    for(let i = 0; i <= 9; i++) {
      //const tableI = i === 0 ? '' : i + 1
  
      const words = local[i].map(x => `('${x.replace(/'/ig, '\'\'')}')`).join(',')
      
      state.onHoldQueries++;
      pool.query(`INSERT INTO words${i + 1} (word) VALUES ${words}`)
        .then((res) => {
          state.onHoldQueries--;
          //console.log(res.rows[0])
        })
      //.catch(e => console.log(e + ' ' + words))
    }
  }
}


setInterval(async () => {
  state.onHoldInserts = bulk[0].length

  if(state.onHoldInserts > state.INSERTS_SIZE && state.onHoldQueries <= state.MAX_ON_HOLD_QUERIES) {
    doBulk()
  }
  
 

  process.stdout.clearLine();
  process.stdout.cursorTo(0);
  process.stdout.write(JSON.stringify(state));
  
}, 200)





const saveWord = (i, word) => {
  
  return bulk[i].push(word)
}
    
    

module.exports = { saveWord, setState }

//console.log(res.rows[0].message) // Hello world!
//await client.end()