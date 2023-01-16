package module6

import org.apache.spark._
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.Seconds

object Main {
  def main(args: Array[String]): Unit = {
    val spark = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount")

    val ssc = new StreamingContext(spark,Seconds(5))
    ssc.sparkContext.setLogLevel("ERROR")
    val mystream = ssc.socketTextStream("localhost",4444)

//    val words = mystream.flatMap(line => line.split("\\W"))
//    val wordCounts = words.map(x => (x, 1)).reduceByKey((x, y) => x + y)
//    wordCounts.print()

//    val lines = mystream.filter(line => line.contains("KBDOC"))
//    lines.foreachRDD(rdd => rdd.foreach(println))
//    lines.foreachRDD(rdd => println("Número de líneas: " + rdd.count()))

    ssc.checkpoint("checkpoints")
    val lines2 = mystream.filter(line => line.contains("KBDOC"))
    lines2.countByWindow(Seconds(10), Seconds(5)).print()

    ssc.start()
    ssc.awaitTermination()
  }
}