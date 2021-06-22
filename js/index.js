var fs = require('fs');
const path = require('path');
var database = require('./database')

const detectCharacterEncoding = require('detect-character-encoding');

var parser = require('subtitles-parser');

async function getFiles(dir) {
    const dirents = await fs.readdirSync(dir, { withFileTypes: true });
    const files = await Promise.all(dirents.map((dirent) => {
        const res = path.resolve(dir, dirent.name);
        return dirent.isDirectory() ? getFiles(res) : res;
    }));
    return Array.prototype.concat(...files);
}

const state = {
    workers: 0,
    totalSubs: 0,
    parsedSubs: 0,
    onHoldQueries: 0,
    onHoldInserts: 0,
    MAX_WORKERS: 8,
    MAX_ON_HOLD_INSERTS: 100000,
    MAX_ON_HOLD_QUERIES: 100,
    INSERTS_SIZE: 30000,
    DATABASE_HOST: '',
    DATABASE_NAME: '',
    DATABASE_PASSWORD: '',
    SUBTITLES_PATH: '/home/ubuntu/movies1'
}


database.setState(state)


const go = async() => {
    const path = state.SUBTITLES_PATH
    const files = await getFiles(path)
    
    const srts = files.filter(x => {
        const xs = x.split('.')
        const ext = xs[xs.length -1]
        return ext.toLowerCase() === 'srt'
    })

    state.totalSubs = srts.length;

    setInterval(() => {
        if(state.workers <= state.MAX_WORKERS && state.onHoldInserts < state.MAX_ON_HOLD_INSERTS) {
            const srt = srts.pop()
            readSubtitles(srt)
        }
    }, 200)
   
}




const readSubtitles = async(file) => {
    state.workers++;
    //console.log(`\nfile: ${file}`)
    const fileBuffer = fs.readFileSync(file);
    const { encoding, confidence } = detectCharacterEncoding(fileBuffer);

    let nodeEncoding = 'latin1';
    if(encoding === 'UTF-8') {
        nodeEncoding = 'utf8'
    }

    var srt = fs.readFileSync(file, nodeEncoding);

    var data = parser.fromSrt(srt);


    var subtitleWords = data.reduce((acc, x) => {
        const textWithoutHtml = x.text.replace(/<[^>]*>/ig, '').toLowerCase()
        const lines = textWithoutHtml.split('\n')
        const words = lines.reduce((acc, x) => acc.concat(x.split(' ')), [])

        const cleanWords = words.map((word) => {
            const x = word.replace(/[!|(|)|\[|\].|,|\"|?|:]/ig, '')
            return x
        }).filter(x => x.length > 1)
        

        return acc.concat(cleanWords)
    }, [])

    for(let i = 0; i < subtitleWords.length; i++){
        const word = subtitleWords[i].trim()
        
        let words = word

        database.saveWord(0, word)
        for(let j = 1; j < 10; j++){
            if(i + j < subtitleWords.length) {
                words = `${words} ${subtitleWords[i + j].trim()}`

               database.saveWord(j, words)
            }
        }
    }

    const filename = path.basename(file)

    fs.renameSync(file, './parsed/' + filename)

    state.workers--;
    state.parsedSubs++;
    return Promise.resolve()

}

go();
  