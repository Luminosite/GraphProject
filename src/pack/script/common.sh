#!/usr/bin/env bash
export BSL_VERSION='_V2'
export BSL_ITERATION='dev'
function getCluster(){
    cluster=`hadoop org.apache.hadoop.conf.Configuration|grep fs.defaultFS|sed 's/.*hdfs:\/\/\(.*\)<\/value>.*/\1/g'`
    echo $cluster
}
function setSparkConf(){
    #dir=`dirname $(readlink -f ~/spark/conf)`
    if [  -d ~/spark/conf ]; then
        echo "direcotry ~/spark/conf exists, so seting SPARK_CONF_DIR to it"
        export SPARK_CONF_DIR=~/spark/conf
    fi
}
function doRemove(){
    path=$1
    echo "Removing $path"
    hadoop fs -rmr -skipTrash $path
}
function removeFiles(){
    date=$1
    doRemove /apps/risk/dst/BSL${BSL_VERSION}/offline/${BSL_ITERATION}/$date/tmp
    doRemove /apps/risk/dst/BSL${BSL_VERSION}/offline/${BSL_ITERATION}/$date/linking/full
    doRemove /apps/risk/dst/BSL${BSL_VERSION}/offline/${BSL_ITERATION}/$date/linking/general
    doRemove /apps/risk/dst/BSL${BSL_VERSION}/offline/${BSL_ITERATION}/$date/linking/group_direct
}
function doCheck(){
    rc=$?;
    pit=$1
    today=$2
    hostname=`hostname -s`
    if [[ $rc != 0 ]]; then
        echo "Failed" | mail -s "job failed, pit:$pit, today:$today, host:$hostname"  $user@paypal.com $NOTIFIER
        exit $rc;
    fi
}
root_dir="/apps/risk/dst/BSL${BSL_VERSION}/offline/${BSL_ITERATION}/"