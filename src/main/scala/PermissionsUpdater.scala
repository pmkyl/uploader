import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import cats.effect.{ContextShift, IO}
import com.decodified.scalassh.{HostConfig, PublicKeyLogin, SSH}
import doobie.implicits._
import doobie.util.transactor.Transactor.Aux
import doobie.{ConnectionIO, Transactor}
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.io.StdIn

class UpdaterActor extends Actor with ActorLogging {

  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
  val xa: Aux[IO, Unit] = Transactor.fromDriverManager[IO](
    "com.mysql.cj.jdbc.Driver", "jdbc:mysql://localhost:3306/databaseName?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/Copenhagen", "user", "pass"
  )
  case class Product(name: String)
  def find(): ConnectionIO[List[Product]] =
    sql"select name from product_lang WHERE name <> '' LIMIT 10"
      .query[Product]
      .to[List]

  def writeToFile(data:Seq[Any]): Unit ={
    import java.io._
    val pw = new PrintWriter(new File("/home/user/hello.txt" ))
    pw.write(data.toString())
    pw.close
  }

  def upload():Unit = {
    val data = SSH("host.com", HostConfig(PublicKeyLogin("root", "/home/user/.ssh/id_rsa"))) {
      client =>
        for {
          _ <- client.upload("/home/user/hello.txt","/root/hello.txt")
          directoryListing <- client.exec("ls -al /root")
        } yield directoryListing.stdOutAsString()
    }
    println(data.toString)
  }

  override def receive = {
    case UpdatePermissions =>
      val data: Seq[Any] = find().transact(xa).unsafeRunSync()
      println(data)
      upload()
  }
}

case object UpdatePermissions

object PermissionsUpdater {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("updater-system")
    val updaterActor = system.actorOf(Props(classOf[UpdaterActor]))
    try {
      system.scheduler.schedule(0 millis, 10 seconds, updaterActor, UpdatePermissions)
      StdIn.readLine()
    } finally {
      system.terminate()
    }
  }
}
