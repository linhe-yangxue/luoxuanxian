cd ..
cd SsmLogin
java -server -ms512m -mx512m -Xmn128m -jar target/SsmLogin-0.0.1-beat.jar &
cd ..
cd SsmGame
java -server -ms512m -mx512m -Xmn128m -jar target/SsmGame-0.0.1-beat.jar &
cd ..
cd SsmChat
java -server -ms512m -mx512m -Xmn128m -jar target/SsmChat-0.0.1-beat.jar &
