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

# Remove packeges
sudo yum remove docker \
                  docker-client \
                  docker-client-latest \
                  docker-common \
                  docker-latest \
                  docker-latest-logrotate \
                  docker-logrotate \
                  docker-engine
                  
# Set up the repository
sudo yum install -y yum-utils

sudo yum-config-manager \
    --add-repo \
    https://download.docker.com/linux/centos/docker-ce.repo

# Install 
sudo yum install docker-ce docker-ce-cli containerd.io docker-compose-plugin

# Start 
sudo systemctl start docker

if [ ! -f /usr/local/bin/docker-compose ]; then
  sudo curl -L "https://github.com/docker/compose/releases/download/1.23.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
  sudo chmod +x /usr/local/bin/docker-compose
  docker-compose --version
fi

printf "${LC}PostgreSQL installation${NC}\n"

# Install PostgreSQL Client
sudo yum install -y postgresql-client

printf "${LC}Maven installation${NC}\n"

# Download
if [ ! -d /usr/lib/apache-maven-3.6.3 ]; then

  wget http://mirrors.estointernet.in/apache/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.tar.gz -P /tmp
  tar xvf /tmp/apache-maven-3.6.3-bin.tar.gz -C /usr/lib/

  M2_HOME='usr/lib/apache-maven-3.6.3'

  printf "M2_HOME=\"/${M2_HOME}\"
  export M2_HOME\n
  M2=\"${M2_HOME}/bin\"
  MAVEN_OPTS=\"-Xms256m -Xmx512m\"
  export M2 MAVEN_OPTS\n
  PATH=${M2_HOME}/bin:${PATH}
  export PATH\n" >> /etc/profile

  . /etc/profile
fi

env | grep M2

printf "${LC}Git installation${NC}\n"

sudo yum install git -y
