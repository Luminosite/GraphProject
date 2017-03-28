package priv.lum.graph.sparkx.trial

import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD
import priv.lum.graph.util.ContextHelper

/**
  * Created by kunfu on 3/24/17.
  */
object GraphTrial extends ContextHelper with App{
  override val appName: String = "GTest"
//  val sqlContext = getHiveContext(appName)
//  val sc = sqlContext.sparkContext
  val sc = getSparkContext(appName)

  {
    val arrs: Seq[(Long, Double)] = (1 to 11).map(i => (i.toLong, i.toDouble))
    val users: RDD[(VertexId, Double)] = sc.parallelize(arrs)
    // Create an RDD for edges
    val relationships: RDD[Edge[Int]] =
      sc.parallelize(Seq(
        Edge(1L, 2L, 1),
        Edge(2L, 3L, 2),
        Edge(1L, 3L, 3),
        Edge(2L, 4L, 4),
        Edge(3L, 5L, 5),
        Edge(6L, 9L, 7),
        Edge(3L, 8L, 8),
        Edge(9L, 7L, 9),
        Edge(6L, 7L, 13),
        Edge(7L, 10L, 6),
        Edge(6L, 10L, 11),
        Edge(9L, 10L, 19),
        Edge(8L, 11L, 20),
        Edge(5L, 11L, 16)))
    // Build the initial Graph
    val graph = Graph[Double, Int](users, relationships)

    val collection2 = SearchGraph.hopsNodesAccumulate[Double, Double, Int](0.0)(3, graph, EdgeDirection.In, _+_)

    collection2 take 20 foreach println
  }
}
