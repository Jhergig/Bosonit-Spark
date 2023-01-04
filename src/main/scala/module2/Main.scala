package module2

import org.apache.spark.sql.SparkSession

object Main {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local[1]")
      .appName("SparkByExample")
      .getOrCreate();

    val textfile = spark.sparkContext.textFile("src/main/resources/relato.txt")
    println(textfile.first())
    println(textfile.count())
    val textfile2 = textfile.flatMap(x => x.split(" "))
    textfile2.foreach(println)
    textfile.collect();

    val route = "src/main/resources/weblogs/2013-09-15.log"
    val logs = spark.sparkContext.textFile(route)
    val jpglogs = logs.filter(x => x.contains(".jpg"))
    jpglogs.take(5).foreach(println)
    val jpglogs2 = logs.filter(x => x.contains(".jpg")).count()
    println(jpglogs2)
    logs.map(x => x.length()).take(5).foreach(println)
    logs.map(x => x.split(" ")).take(5).foreach(x => x.foreach(println))
    val logwords = logs.map(x => x.split(" "))
    val ips = logs.map(x => x.split(" ")(0))
    ips.take(5).foreach(println)
    ips.collect().foreach(println)
    for (x <- ips.take(10)) {
      println(x)
    }
//    ips.saveAsTextFile("src/main/resources/iplist")
//    val ips2 = spark.sparkContext.textFile("src/main/resources/weblogs").map(x => x.split(" ")(0))
    val htmllogs = logs.map( x => { val y = x.split(" "); y(0) + "/" + y(2)})
    htmllogs.take(5).foreach(println)

  }
}