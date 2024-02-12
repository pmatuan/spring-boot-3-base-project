package org.base.migrate;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.apache.commons.cli.*;
import org.javatuples.Pair;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.function.BiFunction;

public class MigrateApplication {
  private static final String DEFAULT_CHANGELOG_FILE = "/db.changelog-master.xml";
  private static final String DEFAULT_SEEDS_FILE = "/db.changelog-seeds.xml";

  /**
   MIGRATE_CLI_ARGUMENTS:
   "-o migrate --host=localhost --port=3306 --database=example --user=root --password=abc123";
   */

  /**
   SEED_CLI_ARGUMENTS:
   "-o seeds --host=localhost --port=3306 --database=example --user=root --password=abc123";
   */

  public static void main(String[] args) throws
      ParseException,
      SQLException,
      LiquibaseException {

    Pair<Connection, String> result = getConnection(args);
    Connection connection = result.getValue0();
    String operation = result.getValue1();

    Database db = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));

    Contexts contexts = new Contexts();

    // Seeding
    if (operation.equals("seeds")) {
      Liquibase liquibase = new Liquibase(
          DEFAULT_SEEDS_FILE, new ClassLoaderResourceAccessor(), db);
      liquibase.validate();
      liquibase.update(contexts);
      return;
    }

    // Migrate and rollback
    Liquibase liquibase = new Liquibase(DEFAULT_CHANGELOG_FILE, new ClassLoaderResourceAccessor(), db);

    liquibase.validate();

    if (operation.equals("migrate")) {
      liquibase.update(contexts);
    }

    if (operation.contains("rollback")) {
      String tag = operation
          .replaceAll("rollback", "")
          .trim();

      liquibase.rollback(tag, contexts);
    }
  }

  private static Pair<Connection, String> getConnection(String... args)
      throws ParseException, SQLException {
    Options options = new Options()
        .addOption("e", false, "Get db credentials from env")
        .addOption(Option.builder("o").longOpt("operation").argName("operation")
            .hasArg().type(String.class).required().build())
        .addOption(null, "host", true, "host")
        .addOption(null, "port", true, "port")
        .addOption(null, "database", true, "database")
        .addOption(null, "user", true, "user")
        .addOption(null, "password", true, "password")
        .addOption(null, "params", true, "params");

    DefaultParser parser = new DefaultParser();
    CommandLine commandLine = parser.parse(options, args);

    BiFunction<String, String, String> envOrOption = (env, optionName) ->
        commandLine.hasOption(optionName)
            ? commandLine.getOptionValue(optionName)
            : System.getenv(env);

    String host = envOrOption.apply("MYSQL_HOST", "host");
    String port = envOrOption.apply("MYSQL_PORT", "port");
    String database = envOrOption.apply("MYSQL_DATABASE", "database");
    String user = envOrOption.apply("MYSQL_USERNAME", "user");
    String password = envOrOption.apply("MYSQL_PASSWORD", "password");
    String params = envOrOption.apply("MYSQL_PARAMS", "params");

    String operation = commandLine.getOptionValue("o");

    Connection connection = dataSource(
        host, port, database,
        user, password, params);

    return Pair.with(connection, operation);
  }

  private static Connection dataSource(
      String host,
      String port,
      String databaseName,
      String username,
      String password,
      String params
  ) throws SQLException {
    String str = String.format("jdbc:mysql://%s" +
        ":%s/" +
        "%s" +
        "?user=%s" +
        "&password=%s" +
        "&%s", host, port, databaseName, username, password, params);

    return DriverManager.getConnection(str);
  }
}
