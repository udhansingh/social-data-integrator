/*
   Copyright 2011 Udayakumar Dhansingh (Udy)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
   
 */
package org.onesun.commons.text.classification.opencalais;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.onesun.commons.StreamUtils;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

public class OpenCalaisClassifier {
	private Map<String, List<String>> entities = new TreeMap<String, List<String>>();

	public Map<String, List<String>> getEntities() {
		return entities;
	}

	public OpenCalaisClassifier() {
	}

	private Set<String> getEntityTypes(Model model){
		if(model == null) return null;
		
		final Query classNameQuery = QueryFactory.create(
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
				+ "SELECT distinct ?o ?x where {" + "?x rdf:type ?o " +
				"} "
			);

		QueryExecution qe = QueryExecutionFactory.create(classNameQuery, model);
		ResultSet results = qe.execSelect();
		Set<String> classifications = new HashSet<String>();
		
		for (; results.hasNext();) {
			QuerySolution soln = results.nextSolution() ;
			
			Resource ro = soln.getResource("?o");
			
			String resAsString = ro.toString();
			String namespace = ro.getNameSpace();
			
			int lastIndexOfSlash = resAsString.lastIndexOf("/") + 1;
			String classificaton = resAsString.substring(lastIndexOfSlash);
			
			if (namespace.startsWith("http://s.opencalais.com/1/type/em/e/")){
				classifications.add(classificaton);
			}else if (namespace.startsWith("http://s.opencalais.com/1/type/er/")){
				classifications.add(classificaton);
			}
		}

		return classifications;
	}
	
	private Model model = null;
	public void classifyDocument(String rdfText)
			throws IOException {
		
		model = ModelFactory.createDefaultModel();
		
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(rdfText.getBytes("UTF-8"));
			model.read(in, null);
			in.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		Set<String> entityTypes = getEntityTypes(model);
		
		for(String entityType : entityTypes){
			final String queryString = "PREFIX c: <http://s.opencalais.com/1/pred/>"
				+ "PREFIX s: <http://s.opencalais.com/1/type/em/e/>"
				+ "SELECT ?term "
				+ "WHERE {"
				+ "	   ?iTerm a s:" + entityType.trim() + " . "
				+ "	   ?iTerm c:name ?term" + "      }";
			
			entities.put(entityType, (doQuery(queryString)));
		}
	}

	private ArrayList<String> doQuery(String queryString) throws IOException {

		ArrayList<String> resultList = new ArrayList<String>();

		Query query = QueryFactory.create(queryString);

		QueryExecution qe = QueryExecutionFactory.create(query, model);

		ResultSet results = qe.execSelect();

		for (; results.hasNext();) {
			QuerySolution soln = results.nextSolution();

			Iterator<String> terms = soln.varNames();

			while (terms.hasNext()) {
				resultList.add(soln.getLiteral(terms.next()).getString());
			}
		}

		qe.close();

		return resultList;
	}

	public List<Map<String, String>> doExtendedQuery(String queryString)
			throws IOException {

		List<Map<String, String>> resultList = new ArrayList<Map<String, String>>();

		Query query = QueryFactory.create(queryString);

		QueryExecution qe = QueryExecutionFactory.create(query, model);

		ResultSet results = qe.execSelect();

		for (; results.hasNext();) {
			QuerySolution soln = results.nextSolution();

			Iterator<String> terms = soln.varNames();

			Map<String, String> map = new HashMap<String, String>();

			while (terms.hasNext()) {
				map.put("s", (String) soln.getLiteral(terms.next()).getString());
				map.put("o", (String) soln.getLiteral(terms.next()).getString());
			}

			resultList.add(map);
		}

		qe.close();

		return resultList;
	}
	
	public static void main(String[] args){
		String pathname = "/smconsole/work/RFAILED/oc.rdf";
		try {
			String rdfText = StreamUtils.streamToString(new File(pathname));
			OpenCalaisClassifier occ = new OpenCalaisClassifier();
			occ.classifyDocument(rdfText);
			
			Map<String, List<String>> entities = occ.getEntities();
			
			for(String entityType : entities.keySet()){
				System.out.format("%30s - %s\n", entityType, entities.get(entityType));
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
