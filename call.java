package org.webofcode.atzori;

import com.hp.hpl.jena.sparql.function.*;
import com.hp.hpl.jena.sparql.expr.NodeValue;
import com.hp.hpl.jena.query.*;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.sparql.util.Utils;

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
            throw new QueryBuildException("Function '"+Utils.className(this)+"' takes at least one argument") ;
    }

    @Override
    public NodeValue exec(List<NodeValue> args)
    {
        if ( args == null )
            // The contract on the function interface is that this should not happen.
            throw new ARQInternalErrorException("Function '"+Utils.className(this)+"': Null args list") ;
        
        if ( args.size() < 1 )
            throw new ExprEvalException("Function '"+Utils.className(this)+"' takes at least one argument") ;
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
            service = "http://swipe.unica.it/jena/sparql";
        }

        //nvEndpoint.asUnquotedString();


        String stringArguments = ",";
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
        System.out.println(query); 
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



















/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hp.hpl.jena.sparql.function;

import java.util.List ;

import com.hp.hpl.jena.query.QueryBuildException ;
import com.hp.hpl.jena.sparql.ARQInternalErrorException ;
import com.hp.hpl.jena.sparql.expr.ExprEvalException ;
import com.hp.hpl.jena.sparql.expr.ExprList ;
import com.hp.hpl.jena.sparql.expr.NodeValue ;
import com.hp.hpl.jena.sparql.util.Utils ;

/** Support for a function of one argument. */

public abstract class FunctionBase1 extends FunctionBase
{
    @Override
    public void checkBuild(String uri, ExprList args)
    { 
        if ( args.size() != 1 )
            throw new QueryBuildException("Function '"+Utils.className(this)+"' takes one argument") ;
    }

    @Override
    public final NodeValue exec(List<NodeValue> args)
    {
        if ( args == null )
            // The contract on the function interface is that this should not happen.
            throw new ARQInternalErrorException("FunctionBase1: Null args list") ;
        
        if ( args.size() != 1 )
            throw new ExprEvalException("FunctionBase1: Wrong number of arguments: Wanted 1, got "+args.size()) ;
        
        NodeValue v1 = args.get(0) ;
        
        return exec(v1) ;
    }
    
    public abstract NodeValue exec(NodeValue v) ;
}

