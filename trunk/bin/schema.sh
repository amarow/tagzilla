#!/bin/sh
HOME=..
LIB=$HOME/lib
 
CP=$HOME/classes
#CP=$CP:$LIB/mysql-connector-java-5.1.0-bin.jar
CP=$CP:$LIB/gna-jorm-runtime-05.08.jar
#CP=$CP:$LIB/jetty-6.1.15.rc3.jar
#CP=$CP:$LIB/jetty-util-6.1.15.rc3.jar
#CP=$CP:$LIB/hessian-3_2-snap.jar
#CP=$CP:$LIB/servlet-api.jar 
echo $CP

java -classpath $CP de.ama.db.tools.SchemaManager tagzilla 
