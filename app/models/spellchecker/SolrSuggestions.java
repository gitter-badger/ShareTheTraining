package models.spellchecker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse.Suggestion;
import org.apache.solr.common.params.ModifiableSolrParams;

public class SolrSuggestions {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String, List<String>> suggMap = getSuggestions("I luve currenc");
		
		if(suggMap == null || suggMap.isEmpty())
			System.out.println("Sorry no suggestions.");
		else{
			Set<String> tokenSet = suggMap.keySet();
			for(String token : tokenSet){
				List<String> suggLst = suggMap.get(token);
				if(suggLst != null && !suggLst.isEmpty()){
					System.out.println("Suggestions for "+token+" are: "+suggLst);
				}					
			}
		}

	}
	
	private static Map<String, List<String>> getSuggestions(String queryStr){
		Map<String, List<String>> suggestionsMap = new HashMap<String, List<String>>();
		if (queryStr != null &&  !queryStr.isEmpty()) {	
								
			try {
			
				SolrServer server = getSolrServer();

			    ModifiableSolrParams params = new ModifiableSolrParams();
			    params.set("qt", "/spell");
			    params.set("q", queryStr);
			    params.set("spellcheck", "true");
			    params.set("spellcheck.collate", "true");

			    QueryResponse response = server.query(params);
			    System.out.println("response = " + response);
				
			    if(response != null){
			    	SpellCheckResponse scr = response.getSpellCheckResponse();
			    	if(scr != null){
			    		List<Suggestion> suggLst = scr.getSuggestions();
			    		for(Suggestion sugg : suggLst){
			    			System.out.println("Suggestion for token "+sugg.getToken()+" are: ");
			    			System.out.println(sugg.getAlternatives());
							suggestionsMap.put(sugg.getToken(), sugg.getAlternatives());
						}
			    		System.out.println("Collated Result: "+scr.getCollatedResult());

			    	}
			    }
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		return suggestionsMap;
		
	}
	
	private static SolrServer getSolrServer(){
		String url = "http://localhost:8983/solr";
		
		HttpSolrServer server = new HttpSolrServer(url);
		
		server.setSoTimeout(10000); // socket read timeout
		server.setConnectionTimeout(10000);
		server.setDefaultMaxConnectionsPerHost(100);
		server.setMaxTotalConnections(100);
		server.setFollowRedirects(false); // defaults to false
		server.setMaxRetries(1); // defaults to 0. > 1 not recommended.

		return server;	
	}

}
