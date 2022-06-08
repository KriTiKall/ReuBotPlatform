cd sl-model
# build jar file
mvn clean package

cd ../
# build images
sudo docker-compose build
# run containers
sudo docker-compose up -d --force-recreate

cd sda-db
# run liquibase
mvn package


