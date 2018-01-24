package FocCrawl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.*;
/**
 *
 * @author AbisTarun
 */
public class SeedAnalysis 
    {
    private static String USER_AGENT = null;

	/*public static void main(String[] args) 
    {
        String seedurl="http://en.wikipedia.org/wiki/Cricket";
        String linkurl="http://en.wikipedia.org/wiki/Sachin_Tendulkar";
        String stpwrdurl="/media/E_drive/anuj files/ryerson/IRWS/asss1/stopwords.txt";
        TreeMap seedvect=generateseedvector(seedurl, stpwrdurl);
        LinkAnalysis linkana=new LinkAnalysis();
        double score = linkana.generatescore(seedvect, linkurl, stpwrdurl);
        //System.out.println(score);
        //createvector(url,"D:\\College works\\TE\\Ryerson\\Labs\\InformationRetievalandWebSearch\\Project\\Code\\stopwords.txt");
        //generatelinkvec(url,"D:\\College works\\TE\\Ryerson\\Labs\\InformationRetievalandWebSearch\\Project\\Code\\stopwords.txt");
    }*/
    public static TreeMap generateseedvector(String url, String stpwrdurl) 
    {
        TreeMap dict=new TreeMap();
        TreeMap seedvector=new TreeMap();
        try 
        {
            Scanner stpwrd=new Scanner(new File(stpwrdurl));
            String stpwrds=stpwrd.useDelimiter("\\Z").next(), curr_wrd;
            Object element;
            
            String htmlcode=Jsoup.connect(url).get().toString();
            String document=Jsoup.parse(htmlcode).text();
            String[] words=document.split(" ");
            for(int i=0;i<words.length;i++)
            {
                curr_wrd=words[i];
                            curr_wrd=curr_wrd.toLowerCase();
                            curr_wrd=potters(curr_wrd, true, false);
                            if(!(stpwrds.contains(curr_wrd)))
                            {
                               if(!curr_wrd.equals(""))
                               {
                               element=dict.put(curr_wrd, 1);
                               if(element!=null)
                                {
                                dict.put(curr_wrd, (Integer.parseInt(element.toString())+1));
                                //System.out.println(eledic);
                                }
                               //System.out.println(curr_wrd);
                            }
                            } 
            }
            //System.out.println(dict.entrySet());
            List sourcevect=null;
            for(Object entry : dict.entrySet()) 
            {
                String wrd=entry.toString().substring(0, entry.toString().lastIndexOf("="));
                int wrdno=Integer.parseInt(entry.toString().substring(entry.toString().lastIndexOf("=")+1));
                double sc=(1 + Math.log10(wrdno));
                seedvector.put(wrd, sc);
            }
        }
        catch (Exception ex) 
        {
            System.out.println(ex.getMessage());
        }
        return seedvector;
    }
    
    public static String potters(String pinp, boolean potflg, boolean numflg)
   {
       String u;
       if(numflg)
       {
           pinp=pinp.replaceAll("[^a-z\\sA-Z\\s0-9]","");
       }
       else
       {
           pinp=pinp.replaceAll("[^a-z\\sA-Z]","");
       }
       if(potflg)
       {
       stemmer s = new stemmer();
       s.add(pinp);  
       s.stem();
       u = s.toString();
       }
       else
       {
           u=pinp;
       }
      return u;
   }

    public static String getHtmlCode (String url) throws ClientProtocolException, IOException{
    	DefaultHttpClient client = new DefaultHttpClient();
    	HttpHost host = new HttpHost("www.google.com");
		HttpGet request = new HttpGet(url);
		
		
 
		// add request header
		USER_AGENT = "Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:25.0) Gecko/20100101 Firefox/25.0";
		request.addHeader("User-Agent", USER_AGENT);
		//System.out.println("1");
		HttpResponse response = client.execute(host,request);
 
		//System.out.println("Response Code : "+ response.getStatusLine().getStatusCode());
 
		BufferedReader rd = new BufferedReader(
		new InputStreamReader(response.getEntity().getContent()));
 
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		return result.toString();
    }
}
