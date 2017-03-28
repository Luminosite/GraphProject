package priv.lum.graph.sparkx.trial
import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD

import scala.reflect.ClassTag

/**
  * Created by kunfu on 3/24/17.
  */
object SearchGraph {

  def hopsNodesAccumulate[VD, VVD, ED: ClassTag](z: VVD)(hopNum: Int, graph: Graph[VD, ED], edgeDirection: EdgeDirection, ops: (VVD, VD) => VVD): RDD[(VertexId, VVD)] = {
    val baseCollection = graph.vertices.map(tuple => (tuple._1, Array((tuple._1, tuple._2))))
    val baseGraph = Graph[Array[(VertexId,VD)], ED](baseCollection, graph.edges)

    val finalGraph = (1 to hopNum).foldLeft[Graph[Array[(VertexId, VD)], ED]](baseGraph)((baseG, n) => {
      val neighbourValues = baseG.collectNeighbors(edgeDirection).map(tuple => {
        val vid = tuple._1
        val values = tuple._2.flatMap(_._2).distinct
        (vid, values)
      })
      baseG.joinVertices[Array[(VertexId, VD)]](neighbourValues)((vid, arr1, arr2) => (arr1 ++ arr2).distinct)
    })

    finalGraph.vertices.map(tuple => {
      val vid = tuple._1
      val value = tuple._2.map(_._2).foldLeft[VVD](z)(ops)
      (vid, value)
    })
  }
}
