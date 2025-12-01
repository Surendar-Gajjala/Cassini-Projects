scp -i ~/MyHome/EC2/cassiniplm.pem ./target/cassini.pdm.war ubuntu@cassiniapps.com:/home/ubuntu/cassini-home/webapps
ssh -i ~/MyHome/EC2/cassiniplm.pem ubuntu@cassiniapps.com 'cd cassini-home;./deploy-and-run.sh'
