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

let workers = 0;
let totalSubs = 0;
let parsedSubs = 0;

const go = async() => {
    const path = '/home/wagner/Documents/out'
    const files = await getFiles(path)
    
    const srts = files.filter(x => {
        const xs = x.split('.')
        const ext = xs[xs.length -1]
        return ext.toLowerCase() === 'srt'
    })

    totalSubs = srts.length;

    setInterval(() => {
        if(workers < 2) {
            const srt = srts.pop()
            readSubtitles(srt)

            //process.stdout.clearLine();
            //process.stdout.cursorTo(0);
            //process.stdout.write(`file: ${totalSubs - parsedSubs} from ${totalSubs}`);

        }
    }, 1500)
   
}




const readSubtitles = async(file) => {
    workers++;
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
        const word = subtitleWords[i]
        
        //process.stdout.clearLine();
        //process.stdout.cursorTo(0);
        //process.stdout.write(`word: ${i} from ${subtitleWords.length}`);
        
        let words = word

        await database.saveWord(0, word)
        for(let j = 1; j < 10; j++){
            if(i + j < subtitleWords.length) {
                words = `${words} ${subtitleWords[i + j]}`

               const missingFiles = parseInt(totalSubs - parsedSubs)
               await database.saveWord(j, words, missingFiles)
            }
        }
    }

    workers--;
    parsedSubs++;
    return Promise.resolve()

}

go();
  