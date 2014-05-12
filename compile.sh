export JENA_HOME=/home/gr/projects/jena/apache-jena-2.11.1
export CLASSPATH=$CLASSPATH:/home/gr/projects/jena:/home/gr/projects/jena/wfn_run.jar:$JENA_HOME/lib/jena-core-2.11.1.jar:$JENA_HOME/lib/jena-arq-2.11.1.jar:$JENA_HOME/lib/jena-iri-1.0.1.jar

javac -cp ./apache-jena-2.11.1/lib/jena-core-2.11.1.jar:./apache-jena-2.11.1/lib/jena-arq-2.11.1.jar:./apache-jena-2.11.1/lib/jena-iri-1.0.1.jar -d . runSPARQL.java
jar cvf wfn_run.jar org
cp wfn_run.jar ./apache-jena-2.11.1/lib/

export CLASSPATH=$CLASSPATH:/home/gr/projects/jena/wfn_call.jar

javac -cp ./apache-jena-2.11.1/lib/jena-core-2.11.1.jar:./apache-jena-2.11.1/lib/jena-arq-2.11.1.jar:./apache-jena-2.11.1/lib/jena-iri-1.0.1.jar -d . call.java
jar cvf wfn_call.jar org
cp wfn_call.jar ./apache-jena-2.11.1/lib/
