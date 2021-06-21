sudo apt update
sudo apt install unzip
sudo apt install build-essential

wget -qO- https://raw.githubusercontent.com/nvm-sh/nvm/v0.38.0/install.sh | bash
source ~/.bashrc
nvm install 12

git clone https://github.com/leonardiwagner/dutchess.git
wget  https://dutchess-movies-english.s3.eu-north-1.amazonaws.com/english/movies.zip