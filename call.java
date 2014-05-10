package org.webofcode.atzori;
import com.hp.hpl.jena.sparql.function.*;
import com.hp.hpl.jena.sparql.expr.NodeValue;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class call extends FunctionBase4 {


    public call() { super() ; 
        FunctionRegistry.get().put("http://webofcode.org/wfn/call", call.class) ;

    }

    public NodeValue exec(NodeValue nvFunction, NodeValue nvEndpoint, NodeValue nvArg1, NodeValue nvArg2) {

    
	String template = 	"SELECT ?result {" +
				"VALUES (?a) {('')} "+
				"BIND( <%func%>(%arg1%,%arg2%) " +
				"AS ?result)" +
				"} LIMIT 1";

	String query = template
		.replace("%func%",nvFunction.asUnquotedString())
		//.replaceAll("%endpoint%",nvEndpoint.toString())
		.replace("%arg1%",nvArg1.toString())
		.replace("%arg2%",nvArg2.toString());


	//System.out.println("\n====================\n"+query+"\n====================\n");
	//query = "select * from <Cagliari.rdf> where {?s ?result ?o } limit 1";
        
	//String service = "http://dbpedia.org/sparql";

	//String query = "select * { <http://dbpedia.org/resource/Milan> ?p ?lab } limit 3";
        System.out.println(query);
	String service = nvEndpoint.asUnquotedString();
	System.out.println(service);

        QueryExecution qe = QueryExecutionFactory.sparqlService(service, query);
        ResultSet rs = qe.execSelect();
        
	if (rs == null) return NodeValue.makeString("null"); //throw new RuntimeException("runSPARQL: null as result");
	if (!rs.hasNext()) return NodeValue.makeString("no results"); //throw new RuntimeException("runSPARQL: no result");
	if (rs.hasNext()) {
		QuerySolution res = rs.next();
		if(res == null) return NodeValue.makeString("res null"); //throw new RuntimeException("runSPARQL: qs null");
		RDFNode node = res.get("result");
		if (node == null) return NodeValue.makeString("node null"); //throw new RuntimeException("runSPARQL: node null");
		System.out.println("node " + node.toString());
	        return NodeValue.makeNode(node.asNode());
	} else
		return NodeValue.makeString("no result!");



	//return NodeValue.makeString("ciao");
    }

}
