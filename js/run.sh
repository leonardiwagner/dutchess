sudo apt update
sudo apt install -y unzip
sudo apt install -y build-essential
wget -qO- https://raw.githubusercontent.com/nvm-sh/nvm/v0.38.0/install.sh | bash
source ~/.bashrc
nvm install 12
git clone https://github.com/leonardiwagner/dutchess.git
wget  https://dutchess-movies-english.s3.eu-north-1.amazonaws.com/english/movies.zip
mkdir movies1 movies2 movies3 movies4
unzip movies.zip
cd movies
unzip ./*.zip
sudo chmod -R 777 .

x=("../movies1" "../movies2" "../movies3" "../movies4")
c=0
for f in *
do
    mv "$f" "${x[c]}"
    c=$(( (c+1)%4 ))
done
