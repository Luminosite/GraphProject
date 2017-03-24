#!/bin/bash

SCRIPT_ABSOLUTE_PATH=$(readlink -f $0)
SCRIPT_FILENAME=$(basename ${SCRIPT_ABSOLUTE_PATH})
SCRIPT_PATH=$(dirname ${SCRIPT_ABSOLUTE_PATH})
SCRIPT_NAME=${SCRIPT_FILENAME%.*}
MAIN_CLASS=$1
shift

JAR_DIR=${JAR_DIR:-"$SCRIPT_PATH/../jars"}
JARS=`find -L ${JAR_DIR} -type f -name '*.jar' | paste -s -d, -`
OWN_JAR=`find -L ${JAR_DIR} -type f -name 'bsl*.jar'`

SPARK_NUM_EXECUTORS=${SPARK_NUM_EXECUTORS:-50}
SPARK_EXECUTOR_MEMORY=${SPARK_EXECUTOR_MEMORY:-4G}
SPARK_EXECUTOR_CORE=${SPARK_EXECUTOR_CORE:-2}
SPARK_DRIVER_MEMORY=${SPARK_DRIVER_MEMORY:-8G}
SPARK_STORAGE_MEMORYFRACTION=${SPARK_STORAGE_MEMORYFRACTION:-0.01}
SPARK_SHUFFLE_MEMORYFRACTION=${SPARK_SHUFFLE_MEMORYFRACTION:-0.5}
SPARK_DRIVER_CORE=${SPARK_DRIVER_CORE:-2}
SPARK_EXECUTOR_JAVA_OPTIONS=${SPARK_EXECUTOR_JAVA_OPTIONS:-'-XX:MaxPermSize=3g -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps '}

SPARK_VERSION=${SPARK_VERSION:-latest}
#TODO better to check whether it's in horton/Stampy
QUEUE=${QUEUE:-'risk_grs'}
echo "CORE:$SPARK_EXECUTOR_CORE, SHUFFLE_FRACTION:$SPARK_SHUFFLE_MEMORYFRACTION, QUEUE:$QUEUE"
echo "SPARK version: $SPARK_VERSION"
echo "/x/home/pp_risk_grs_rds_ops_batch/spark/spark-${SPARK_VERSION}/bin/spark-submit"

#TODO:change to use standard config
cd ${SCRIPT_PATH} && \
/x/home/pp_risk_grs_rds_ops_batch/spark/spark-${SPARK_VERSION}/bin/spark-submit \
  --class priv.lum.graph.${MAIN_CLASS} \
  --conf "spark.storage.memoryFraction=${SPARK_STORAGE_MEMORYFRACTION}"\
  --conf "spark.shuffle.memoryFraction=${SPARK_SHUFFLE_MEMORYFRACTION}"\
  --conf "spark.executor.extraJavaOptions=${SPARK_EXECUTOR_JAVA_OPTIONS}" \
  --conf "spark.yarn.executor.memoryOverhead=6000" \
  --conf "spark.driver.maxResultSize=3G" \
  --conf "spark.ui.port=4060" \
  --conf "spark.port.maxRetries=32" \
  --master yarn-client \
  --jars ${JARS} \
  --queue ${QUEUE} \
  --driver-memory ${SPARK_DRIVER_MEMORY} \
  --driver-cores ${SPARK_DRIVER_CORE} \
  --executor-memory ${SPARK_EXECUTOR_MEMORY} \
  --executor-cores ${SPARK_EXECUTOR_CORE} \
  --num-executors ${SPARK_NUM_EXECUTORS} \
  ${OWN_JAR} \
  $@
