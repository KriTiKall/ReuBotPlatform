Code for create database if not exists:
echo "SELECT 'CREATE DATABASE mydb' WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'mydb')\gexec" | psql -U smfd -W smfd