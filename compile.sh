export JENA_HOME=/home/gr/projects/jena/apache-jena-2.11.1
export CLASSPATH=$CLASSPATH:/home/gr/projects/jena:/home/gr/projects/jena/r.jar:$JENA_HOME/lib/jena-core-2.11.1.jar:$JENA_HOME/lib/jena-arq-2.11.1.jar:$JENA_HOME/lib/jena-iri-1.0.1.jar

javac -cp ./apache-jena-2.11.1/lib/jena-core-2.11.1.jar:./apache-jena-2.11.1/lib/jena-arq-2.11.1.jar:./apache-jena-2.11.1/lib/jena-iri-1.0.1.jar -d . runSPARQL.java
jar cvf r.jar org
cp r.jar ./apache-jena-2.11.1/lib/

export CLASSPATH=$CLASSPATH:/home/gr/projects/jena/c.jar

javac -cp ./apache-jena-2.11.1/lib/jena-core-2.11.1.jar:./apache-jena-2.11.1/lib/jena-arq-2.11.1.jar:./apache-jena-2.11.1/lib/jena-iri-1.0.1.jar -d . call.java
jar cvf c.jar org
cp c.jar ./apache-jena-2.11.1/lib/
