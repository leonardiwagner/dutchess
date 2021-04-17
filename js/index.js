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

const go = async() => {
    const path = '/home/wagner/Downloads'
    const files = await getFiles(path)
    
    const srts = files.filter(x => {
        const xs = x.split('.')
        const ext = xs[xs.length -1]
        return ext.toLowerCase() === 'srt'
    })

    for(let i = 0; i <srts.length; i++) {
        await readSubtitles(srts[i])
    }
   
}




const readSubtitles = async(file) => {
    console.log(file)
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
            const x = word.replace(/[!|.|,|\"|?|:]/ig, '')
            return x
        }).filter(x => x.length > 1)
        

        return acc.concat(cleanWords)
    }, [])

    for(let i = 0; i < subtitleWords.length; i++){
        const word = subtitleWords[i]

        await database.saveWord('', word)
        /*
        let words = word
        for(let j = 1; j < 10; j++){
            if(i + j < subtitleWords.length) {
                words = `${words} ${subtitleWords[i + j]}`

               await database.saveWord(j + 1, words)
            }
        }*/
    }

    return Promise.resolve()

}

const doIt = async() => {
    go();
}

(() => {
    doIt() 
})();
  