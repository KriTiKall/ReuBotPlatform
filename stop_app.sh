cd sl-model
# build jar file
mvn clean

cd ../
sudo docker-compose kill -s SIGINT