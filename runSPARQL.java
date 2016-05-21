package org.webofcode.atzori;
import com.hp.hpl.jena.sparql.function.*;
import com.hp.hpl.jena.sparql.expr.NodeValue;

import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class runSPARQL extends FunctionBase3 {

 static {
        FunctionRegistry.get().put("http://webofcode.org/wfn/runSPARQL", runSPARQL.class) ;
    }

    public runSPARQL() { 
        super() ; 
        FunctionRegistry.get().put("http://webofcode.org/wfn/runSPARQL", runSPARQL.class) ;
    }


    public NodeValue exec(NodeValue nvQuery, NodeValue nvEndpoint, NodeValue nvInputVar) {
    
	String template = 
      	"PREFIX wfn: <java:org.webofcode.atzori.>\n"+
	"SELECT ?result \n"+ 
	"FROM <dataset.rdf>\n"+
	"{ \n"+
	"    # bind variables to parameter values\n"+ 
	"    VALUES (?query ?endpoint ?i0) { ( \n"+
	"        %query% \n"+
	"        %endpoint% \n"+
	"        %i0% \n"+
	"    )} \n"+
	"    # the recursive query \n\n"+
	"    %queryexec% \n"+
	"} LIMIT 1\n";

	String query = template
		.replaceAll("%query%",nvQuery.toString())
		.replaceAll("%endpoint%",nvEndpoint.toString())
		.replaceAll("%i0%",nvInputVar.toString())
		.replace("%queryexec%",nvQuery.toString().trim().replaceAll("^\\\"(.*)\\\"$","$1"));

	//System.out.println("\n====================\n"+query+"\n====================\n");
	//query = "select * from <Cagliari.rdf> where {?s ?result ?o } limit 1";
        
        QueryExecution qe = QueryExecutionFactory.create(query);
        ResultSet rs = qe.execSelect();
        
	if (rs == null) throw new RuntimeException("runSPARQL: null as result");
	if (!rs.hasNext()) throw new RuntimeException("runSPARQL: no result");
	if (rs.hasNext()) {
		QuerySolution res = rs.next();
		if(res == null) throw new RuntimeException("runSPARQL: qs null");
		RDFNode node = res.get("result");
		if (node == null) throw new RuntimeException("runSPARQL: node null");
		System.out.println("node " + node.toString());
	        return NodeValue.makeNode(node.asNode());
	} else
		return NodeValue.makeString("no result!");

	//return NodeValue.makeString("ciao");
    }

}
