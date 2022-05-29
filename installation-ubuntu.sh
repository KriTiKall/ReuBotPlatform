# varable 
R='\033[0;31m'
G='\033[0;32m'
O='\033[0;33m'
B='\033[0;34m'
P='\033[0;35m'
C='\033[0;36m'

LR='\033[1;31m'
LG='\033[1;32m'
LO='\033[1;33m'
LB='\033[1;34m'
LP='\033[1;35m'
LC='\033[1;36m'

NC='\033[0m' # No Color


printf "${LC}Docker installation${NC}\n"

# Set up the repository
sudo apt-get update

sudo apt-get install \
    ca-certificates \
    curl \
    gnupg \
    lsb-release
   
if [ ! -f /etc/apt/keyrings/docker.gpg ]; then
    echo "File not found!"
    sudo mkdir -p /etc/apt/keyrings
	curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
fi
   


echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# Install Docker Engine
sudo apt-get update
sudo apt-get install docker-ce docker-ce-cli containerd.io docker-compose-plugin


printf "${LC}PostgreSQL installation${NC}\n"

# Install PostgreSQL Client
sudo apt install postgresql-client


printf "${LC}Maven installation${NC}\n"

sudo apt install maven

printf "${LC}Git installation${NC}\n"

sudo apt install git

