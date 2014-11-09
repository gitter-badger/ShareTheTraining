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

import play.Logger;
import play.Play;

public class SolrSuggestions {

	public static String getSuggestions(String queryStr) {
		if (queryStr != null && !queryStr.isEmpty()) {
			try {
				String solrURL = Play.application().configuration()
						.getString("solr.url");
				SolrServer server = (HttpSolrServer) SolrServerFactory
						.getInstance().createServer(solrURL);
				ModifiableSolrParams params = new ModifiableSolrParams();
				params.set("qt", "/spell");
				params.set("q", queryStr);
				params.set("spellcheck", "true");
				params.set("spellcheck.collate", "true");
				QueryResponse response = server.query(params);
				if (response != null) {
					SpellCheckResponse scr = response.getSpellCheckResponse();
					if (scr != null && scr.getCollatedResult() != null)
						return scr.getCollatedResult();
				}
			} catch (Exception e) {
				Logger.error(e.toString());
			}
		}
		return queryStr;
	}
}
