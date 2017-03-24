if [[ $# -lt 1 ]];
then
    echo "host missing"
    exit 1
else
    host=$1
fi
rsync -azP --delete target/pack/lib/bsloffline_2.11-0.1.0-SNAPSHOT.jar $host:/x/home/chufang/workspace/BSLOffline_new/jars/lib/bsloffline_2.11-0.1.0-SNAPSHOT.jar
#rsync -azP --delete target/scala-2.10/nrtlinking_2.10-0.1.0-SNAPSHOT.jar stampy:/x/home/chufang/workspace/NRTLinking/jars
