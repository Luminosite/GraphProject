. ./utils.sh
if [[ $# -lt 1 ]];
then
    echo "host missing"
    exit 1
else
    host=$1
fi
version=`getVersion`
echo "Version is $version"
suffix="_$version"
user=`whoami`
chmod 755 target/pack/script/*.sh
echo "Creating directory on remote"
bash extract.sh

ssh $host "mkdir -p /x/home/$user/workspace/BSLOffline$suffix/jars/ /x/home/$user/workspace/BSLOffline$suffix/script/"
rsync -azP --delete target/pack/lib/ $host:/x/home/$user/workspace/BSLOffline$suffix/jars/lib/
rsync -azP target/pack/resources/*.xml $host:/x/home/$user/workspace/BSLOffline$suffix/script//resources/
rsync -azP target/pack/script/* $host:/x/home/$user/workspace/BSLOffline$suffix/script/
rsync -azP target/pack/bin/* $host:/x/home/$user/workspace/BSLOffline$suffix/bin/

