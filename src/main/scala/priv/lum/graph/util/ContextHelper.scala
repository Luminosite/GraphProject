package priv.lum.graph.util

import com.typesafe.scalalogging.slf4j.LazyLogging
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkConf, SparkContext}

trait ContextHelper extends LazyLogging{
  val appName: String
  val defaultPartitionNums = 3000
  def getHiveContext(appName: String, partition:Option[Int] = Some(defaultPartitionNums),configs:Map[String, String]= Map()):SQLContext = {
    val conf = new SparkConf()
    configs.foreach(config => conf.set(config._1, config._2))
    val sparkConf = conf.setAppName(appName)
    val sc = new SparkContext(sparkConf)
    val sqlContext = new HiveContext(sc)
    sqlContext.sql("use edw")
    if(partition.isDefined){
      logger.info(s"Setting partitions to ${partition.get}")
      sqlContext.setConf("spark.sql.shuffle.partitions", partition.get.toString)
    }
    sqlContext
  }

  def getSparkContext(appName: String, configs:Map[String, String]= Map()):SparkContext = {
    val conf = new SparkConf()
    configs.foreach(config => conf.set(config._1, config._2))
    val sparkConf = conf.setAppName(appName)
    val sc = new SparkContext(sparkConf)
    sc
  }
}

