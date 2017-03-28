#!/bin/bash

. ./utils.sh
if [[ $# -lt 1 ]];
then
    echo "host missing"
    exit 1
else
    host=$1
fi

pj_name="GraphTrail"
version=`getVersion`
echo "Version is $version"
user=`whoami`

chmod 755 target/pack/script/*.sh

echo "Creating directory on remote"
#bash extract.sh
ssh ${host} "mkdir -p /x/home/$user/workspace/${pj_name}_$version/jars/ /x/home/$user/workspace/${pj_name}_$version/script/"
rsync -azP --delete target/pack/lib/ ${host}:/x/home/${user}/workspace/${pj_name}_${version}/jars/lib/
#rsync -azP target/pack/resources/*.xml ${host}:/x/home/${user}/workspace/${pj_name}_${version}/script//resources/
rsync -azP target/pack/script/* ${host}:/x/home/${user}/workspace/${pj_name}_${version}/script/
rsync -azP target/pack/bin/* ${host}:/x/home/${user}/workspace/${pj_name}_${version}/bin/

