# A SPARQL Function to Rule Them All

We developed a SPARQL function called `wfn:call` that takes as a parameter an URI representing a function and then other arguments for the specified function. The `wfn:call` function executes the specified URI function against an appropriate remote endpoint (that implements such a function), passing the arguments and getting the result back. It allows interoperable remote invocation of functions (including high-order functions) from within a SPARQL query. 

Further details available at the [official website](http://atzori.webofcode.org/projects/wfn/).

## Installation 
The following steps have been tested using Apache Fuseki 2.4.0 and Apache Jena 3.1.0, Ubuntu 16.04 and OpenJDK "1.8.0_91".

1. download and unzip Apache Fuseki 2.4.0 and Apache Jena 3.1.0
2. clone the callsparql repository
3. *(this step is optional; a compiled version is already present)* compile callsparql and create the file `wfn_call.jar` with the following commands (Java 8 is required):
```bash
cd callsparql
# create compiled classes on org/ dir (assuming jena is installed at ../apache-jena-3.1.0/)
javac -cp "../apache-jena-3.1.0/lib/*" -d . call.java
# create jar file
jar cvf wfn_call.jar org
# clean
rm -rf org/
```
4. go to the fuseki dir and run it: `java -Xmx1200M -cp fuseki-server.jar:../callsparql/wfn_call.jar org.apache.jena.fuseki.cmd.FusekiCmd`


## Test
The following query:
```
#PREFIX wfn: <http://webofcode.org/wfn/>
PREFIX wfn: <java:org.webofcode.atzori.>
PREFIX fn: <http://www.w3.org/2005/xpath-functions#>
SELECT *
{
    BIND( wfn:call(fn:concat,"a","b") as ?a)
}
```
will compute the concatenation of the strings "a" and "b" on the localhost (without data)

After the first use in a sparql query, the function is registered as `<http://webofcode.org/wfn/call>` within the Fuseki function registry.

The following:
```
PREFIX wfn: <http://webofcode.org/wfn/>  # this should work now
PREFIX fn: <http://www.w3.org/2005/xpath-functions#>
SELECT *
{
  BIND( wfn:call("http://www.w3.org/2005/xpath-functions#concat@http://127.0.0.1:3030/ds/sparql","a","b") as ?a)
}
```

will compute again the same concatenation on the localhost, this time against the "ds" dataset (must be present in fuseki).




## Notes

callSPARQL depends on the following Jena libraries: jena-core, jena-arq and jena-iri.

