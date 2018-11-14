# Cassandra-Metrics
===========================

This tool is used to collect Cassandra JMX data from Cassandra instances and push those metrics to Graphite.

Build
-----
    $ mvn package

Run
---
    $ java -jar cassandra-jmx-<version>-SNAPSHOT-bundle.jar
    Usage: Command <jmx host> <jmx port> <graphite host> <graphite port> <prefix>

For example:

    $ java -jar /path/to/cassandra-jmx-<version>-SNAPSHOT-bundle.jar \
          db-1.example.com \
          7199 \
          carbon-1.example.com \
          2003 \
          cassandra.db-1


Testing locally
---------------
To test locally, use `nc` (netcat).

Open a socket and listen on port 2003:

    $ nc -lk 2003

Collect Cassandra metrics and write to netcat:

    $ java -jar /path/to/cassandra-jmx-<version>-SNAPSHOT-bundle.jar \
          localhost \
          7199 \
          localhost \
          2003 \
          myhost_tokbox_com


Note: This project was originally forked from https://github.com/wikimedia/cassandra-metrics-collector, but that project collected too much data, so this was created to be more selective in the data collection.
