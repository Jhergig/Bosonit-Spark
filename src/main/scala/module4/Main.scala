package module4

import org.apache.spark.sql.SparkSession

object Main {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local[1]")
      .appName("SparkByExample")
      .getOrCreate();

//    val ages = List(2, 52, 44, 23, 17, 14, 12, 82, 51, 64);
//    val grouped = ages.groupBy { age =>
//      if (age >= 18 && age < 65) "adult"
//      else if (age < 18) "child"
//      else "senior"
//    }
//    grouped.foreach(println);
//
//    val logs = spark.sparkContext.textFile("src/main/resources/weblogs/2013-09-15.log");
//    val rdd = logs.map(line => line.split(' ')).map(words => (words(2), 1));
//    val red = rdd.reduceByKey((v1, v2) => v1 + v2);
//    red.foreach(println);
//
//    val newRed = red.map(campo => campo.swap);
//    newRed.sortByKey(false).map(field => field.swap).take(10).foreach(println);
//
//    val rdd2 = logs.map(line => line.split(' ')).map(words =>
//      (words(2), words(0))).groupByKey();
//    rdd2.take(10).foreach(println);
//    rdd2.take(10).foreach(x => {
//      println("ID: " + x._1);
//      println("IPS:");
//      for (ip <- x._2) {
//        println(ip)
//      }
//    });
//    rdd2.take(10).foreach { case (key, values) => println("ID: " + key + "\n" + "IPS:" + "\n" + values.mkString("\n")) }
//
//    val accounts = spark.sparkContext.textFile("src/main/resources/accounts.csv");
//    val rdd3 = accounts.map(line => line.split(",")).map(fields => (fields(0),fields.slice(1,fields.length)));
//    rdd3.take(5).foreach(x => println(x._1 + "\t-\t" + x._2.mkString("\t")));
//    val joined = red.join(rdd3);
//    joined.take(5).foreach(x => println(x._1 + "\t-\t" + x._2._1 + "\t" + x._2._2.mkString("\t")));
//    val rdd4 = joined.map(x => (x._1, x._2._1, x._2._2(2), x._2._2(3)));
//    rdd4.take(10).foreach(println);
//
//    val rdd5 = accounts.map(line => line.split(",")).keyBy(x => x(8));
//    rdd5.take(5).foreach(x => println(x._1 + "\t-\t" + x._2.mkString("\t")));
//    val rdd6 = rdd5.mapValues(x => (x(4),x(3))).groupByKey();
//    rdd6.take(10).foreach(println);
//    rdd6.sortByKey().take(5).foreach(x => println("--- " + x._1 + "\n" + x._2.mkString("\n")));

    val shakespeareComedies = spark.sparkContext.textFile("src/main/resources/shakespeare/comedies");
    val wordcount = shakespeareComedies.flatMap(line => line.split("\\W")).groupBy( x => x.toLowerCase()).map(x => (x._2.size,x._1)).sortByKey(false).map(x => x.swap)
    wordcount.take(20).foreach(println)
  }
}