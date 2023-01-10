package module5

import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.sameersingh.scalaplot.Implicits.{output,PNG}
import org.sameersingh.scalaplot.{MemXYSeries, XYChart, XYData}
import org.sameersingh.scalaplot.gnuplot.GnuplotPlotter

object Main {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .master("local[1]")
      .appName("SparkByExample")
      .getOrCreate();

    //    module1(spark);
    //    module2(spark);
    //    module3(spark);
    module4(spark);
  }

  def module1(spark: SparkSession) {
    val zips = spark.read.json("src/main/resources/zips.json");
    zips.show();
    zips.filter("pop > 10000").show();
    zips.createOrReplaceTempView("zips");
    spark.sql("SELECT * FROM zips WHERE pop > 10000").show();
    spark.sql("SELECT city, count(_id) as nCP FROM zips GROUP BY city HAVING nCP > 100").show();
    spark.sql("SELECT sum(pop) as wisconsinPop FROM zips WHERE state LIKE 'WI' GROUP BY state").show();
    spark.sql("SELECT state, sum(pop) as statePop FROM zips GROUP BY state ORDER BY statePop DESC LIMIT 5").show();
  }

  def createZipsView() {

  }

  def module2(spark: SparkSession) {

  }

  def module3(spark: SparkSession) {
    val partidosRDD = spark.sparkContext.textFile("src/main/resources/DataSetPartidos.txt");
    val rowRDD = partidosRDD.map(row => row.split("::")).map(fields => Row(fields(0), fields(1), fields(2), fields(3), fields(4), fields(5), fields(6), fields(7), fields(8)))

    val schemaStr = "idPartido::temporada::jornada::EquipoLocal::EquipoVisitante::golesLocal::golesVisitante::fecha::timestamp";
    val fields = schemaStr.split("::").map(field => StructField(field, StringType, true));
    val schema = StructType(fields);

    val partidosDF = spark.createDataFrame(rowRDD, schema);

    partidosDF.createOrReplaceTempView("partidos");

    spark.sql("SELECT temporada, sum(golesVisitante) as golesComoVisitante FROM partidos WHERE EquipoVisitante LIKE 'Real Oviedo' GROUP BY temporada ORDER BY golesComoVisitante DESC LIMIT 1").show();
    spark.sql("SELECT count(distinct temporada) as temporadas FROM partidos WHERE EquipoLocal LIKE 'Sporting de Gijon'").show()
    spark.sql("SELECT count(distinct temporada) as temporadas FROM partidos WHERE EquipoLocal LIKE 'Real Oviedo'").show()
  }

  def module4(spark: SparkSession) {
    val simpsonsDF = spark.read.options(Map("inferSchema" -> "true", "header" -> "true")).csv("src/main/resources/simpsons.csv")
    simpsonsDF.createOrReplaceTempView("simpsons")

    val meanRatingPerSeasonDF = spark.sql("SELECT season, avg(imdb_rating) as season_imdb_rating FROM simpsons GROUP BY season")
    meanRatingPerSeasonDF.show()

    val meanRatingPerSeasonRDD = meanRatingPerSeasonDF.rdd.map(line => (line.getInt(0).toDouble, line.getDouble(1)));
    val x = meanRatingPerSeasonRDD.keys;
    val y = meanRatingPerSeasonRDD.values;
    x.take(20).foreach(println);
    y.take(20).foreach(println);
    val series = new MemXYSeries(x.collect(), y.collect(), "puntuacion")
    val data = new XYData(series)
    val chart = new XYChart("Media de puntuación de episodios de Los Simpsons durante sus temporadas", data)
    output(PNG("src/main/resources/", "test-simpsons"), chart)
  }
}