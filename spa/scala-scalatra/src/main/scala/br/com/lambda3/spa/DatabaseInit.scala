package br.com.lambda3.spa

import com.mchange.v2.c3p0.ComboPooledDataSource
import org.squeryl.adapters.PostgreSqlAdapter
import org.squeryl.Session
import org.squeryl.SessionFactory
import org.slf4j.LoggerFactory

trait DatabaseInit {

  val logger = LoggerFactory.getLogger(getClass)

  val databaseUsername = "spa"
  val databasePassword = "spa"
  val databaseConnection = "jdbc:postgresql://localhost:5432/spa"

  var cpds = new ComboPooledDataSource

  def configureDb() {
    cpds.setDriverClass("org.postgresql.Driver")
    cpds.setJdbcUrl(databaseConnection)
    cpds.setUser(databaseUsername)
    cpds.setPassword(databasePassword)

    cpds.setMinPoolSize(1)
    cpds.setAcquireIncrement(1)
    cpds.setMaxPoolSize(50)

    SessionFactory.concreteFactory = Some(() => connection)

    def connection = {
      logger.info("Creating connection with c3po connection pool")
      Session.create(cpds.getConnection, new PostgreSqlAdapter)
    }
  }

  def closeDbConnection() {
    logger.info("Closing c3po connection pool")
    cpds.close()
  }
}