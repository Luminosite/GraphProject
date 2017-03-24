package priv.lum.graph.sparkx.trial

import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD
import priv.lum.graph.util.ContextHelper

/**
  * Created by kunfu on 3/24/17.
  */
object GraphTrial extends ContextHelper with App{
  override val appName: String = "GTest"
  val sqlContext = getHiveContext(appName)
  val sc = sqlContext.sparkContext

  {
    val arrs = (1 to 11).map(i => (i.toLong, i)).toArray
    val users: RDD[(VertexId, Int)] = sc.parallelize(arrs)
    // Create an RDD for edges
    val relationships: RDD[Edge[Int]] =
      sc.parallelize(Array(
        Edge(3L, 7L, 1), Edge(5L, 3L, 3),
        Edge(2L, 5L, 2), Edge(5L, 7L, 4)))
    // Build the initial Graph
    val graph = Graph[Int, Int](users, relationships)
  }
}
