package models.spellchecker;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import play.Logger;
import play.Play;
 
 
public class SolrDao <T>
{
 
    HttpSolrServer server = null;
    
    public SolrDao ()
    {
    	String solrURL = Play.application().configuration().getString("solr.url");
        server = (HttpSolrServer) SolrServerFactory.getInstance().createServer(solrURL);
        configureSolr (server);
    }
    
    public void put (T dao)
    {
        put (createSingletonSet (dao));
    }
    
    public void put (Collection<T> dao)
    {
        try 
        {
            UpdateResponse rsp = server.addBeans (dao);
            //System.out.println ("Added documents to solr. Time taken = " + rsp.getElapsedTime() + ". " + rsp.toString());
        }
        catch (SolrServerException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void putDoc (SolrInputDocument doc)
    {
        putDoc (createSingletonSet(doc));
    }
    
    public void putDoc (Collection<SolrInputDocument> docs)
    {
        try 
        {
            long startTime = System.currentTimeMillis();
            UpdateRequest req = new UpdateRequest();
            req.setAction( UpdateRequest.ACTION.COMMIT, false, false );
            req.add (docs);
            UpdateResponse rsp = req.process( server );
            long endTime = System.currentTimeMillis();
        }
        catch (SolrServerException e)
        {
            Logger.error(e.getMessage());
        }
        catch (IOException e)
        {
        	Logger.error(e.getMessage());
        }
    }
    
    private void configureSolr(HttpSolrServer server) 
    {
        server.setMaxRetries(1); // defaults to 0.  > 1 not recommended.
        server.setConnectionTimeout(5000); // 5 seconds to establish TCP
        // The following settings are provided here for completeness.
        // They will not normally be required, and should only be used 
        // after consulting javadocs to know whether they are truly required.
        server.setSoTimeout(1000);  // socket read timeout
        server.setDefaultMaxConnectionsPerHost(100);
        server.setMaxTotalConnections(100);
        server.setFollowRedirects(false);  // defaults to false
        // allowCompression defaults to false.
        // Server side must support gzip or deflate for this to have any effect.
        server.setAllowCompression(false);
    }
    
    private <U> Collection<U> createSingletonSet(U dao) 
    {
        if (dao == null)
            return Collections.emptySet();
        return Collections.singleton(dao);
    }
 
}