package org.webofcode.atzori;

import org.apache.jena.sparql.function.*;
import org.apache.jena.sparql.expr.NodeValue;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.RDFNode;
//import org.apache.jena.sparql.util.Utils;
import org.apache.jena.sparql.expr.ExprEvalException;
import org.apache.jena.sparql.expr.ExprList;
import org.apache.jena.sparql.ARQInternalErrorException;
import java.util.List;

public class call extends FunctionBase {
    static {
        FunctionRegistry.get().put("http://webofcode.org/wfn/call", call.class) ;
    }

    public call() { 
        super() ; 
        FunctionRegistry.get().put("http://webofcode.org/wfn/call", call.class) ;
    }

    @Override
    public void checkBuild(String uri, ExprList args) { 
        if ( args.size() < 1 )
            throw new QueryBuildException("Function '"+ "call" /*Utils.className(this)*/ +"' takes at least one argument") ;
    }

    @Override
    public NodeValue exec(List<NodeValue> args)
    {
        if ( args == null )
            // The contract on the function interface is that this should not happen.
            throw new ARQInternalErrorException("Function '"+ "call" /*Utils.className(this)*/ +"': Null args list") ;
        
        if ( args.size() < 1 )
            throw new ExprEvalException("Function '"+ "call" /*Utils.className(this)*/ +"' takes at least one argument") ;
        NodeValue nvFunction = args.remove(0);
        return compute(nvFunction, args) ;
    }




    public NodeValue compute(NodeValue nvFunction, /*NodeValue nvEndpoint, NodeValue nvArg1, NodeValue nvArg2*/  List<NodeValue> args) {
    
	    String template = 	"SELECT ?result {" +
				    "VALUES (?a) {('')} "+  // this line is required on VIRTUOSO 
				    "BIND( <%func%>(%args%) " +
				    "AS ?result)" +
				    "} LIMIT 1";

        String function = nvFunction.asUnquotedString();
	    String service = ""; 
        if(function.contains("@")) {
            // endpoint is specified
            String[] s = function.split("@");
            function = s[0];
            service = s[1];
        } else {
            // compute service by nvFunction
            service = "localhost";
        }

        //nvEndpoint.asUnquotedString();


        String stringArguments = "";
        String loopDelim = "";

        for(NodeValue nv : args) {
            stringArguments += loopDelim;
            stringArguments += nv.toString();
            loopDelim = ",";        
        }

	    String query = template
		    .replace("%func%",function)
		    .replace("%args%",stringArguments);

        // for debug purpuses
        System.out.println("query: "+query); 
        System.out.println("service: "+service);

		QueryExecution qe;
		if (service.equals("localhost")) {
		    qe = QueryExecutionFactory.create(query, DatasetFactory.create());
		} else { 
	        qe = QueryExecutionFactory.sparqlService(service, query);
		}
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
