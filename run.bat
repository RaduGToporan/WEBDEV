@echo off
call mvn package
docker image build -t green/ai_marketplace:latest . 
docker run -p 8080:8080 green/ai_marketplace:latest