import org.flywaydb.core.Flyway

trait DatabaseMigration {
  val url = "jdbc:postgresql://127.0.0.1/exampledb"
  val user = "example"
  val password = "password"

  def migrate(url: String = url, user: String = user, password: String = password) = {
    val flyway: Flyway = Flyway.configure().dataSource(url, user, password).load()
    flyway.baseline()
    flyway.migrate()
  }

}
