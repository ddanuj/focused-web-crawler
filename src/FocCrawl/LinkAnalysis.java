package FocCrawl;

import static com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl.DEBUG;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
//import sun.reflect.generics.tree.Tree;
//import static webcrawltest.SeedAnalysis.potters;
/**
 *
 * @author AbisTarun
 */
public class LinkAnalysis {
    public static final int    SEARCH_LIMIT = 20;  // Absolute max pages 
    public static final boolean DEBUG = false;
    public static final String DISALLOW = "Disallow:";
    public static final int MAXSIZE = 20000; // Max size of file 
	private static final String USER_AGENT = null;

	static SeedAnalysis seedclass=new SeedAnalysis();
    
    private static TreeMap generatelinkvec(String url, String stpwrdurl, TreeMap seeddict) 
    {
        TreeMap linkdict=new TreeMap();
        TreeMap linkvect=new TreeMap();
        for(Object entry : seeddict.entrySet())
        {
            String wrd=entry.toString().substring(0, entry.toString().lastIndexOf("="));
            linkdict.put(wrd, 0);
        }
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
                                   element=linkdict.get(curr_wrd);
                               if(element!=null)
                                {
                                element=Integer.parseInt(element.toString())+1;
                                linkdict.put(curr_wrd, element);
                                //System.out.println(eledic);
                                }
                               //System.out.println(curr_wrd);
                                }
                            } 
            }
            for(Object entry : linkdict.entrySet()) 
            {
                String wrd=entry.toString().substring(0, entry.toString().lastIndexOf("="));
                int wrdno=Integer.parseInt(entry.toString().substring(entry.toString().lastIndexOf("=")+1));
                double sc=0;
                if(wrdno!=0)
                {
                    sc=(1 + Math.log10(wrdno));
                }
                linkvect.put(wrd, sc);
            }
        }
        catch (Exception ex) 
        {
            System.out.println(ex.getMessage());
        }
        return linkvect;
    }
 
    public static TreeMap linksbyrelevance(String url,boolean flag) throws IOException{
    	
    	//TreeMap to store (relevance,link)
    	TreeMap rellink = new TreeMap();
        rellink.clear();
    	
    	//get the document
    	
    	
		
		if(flag == true){
			//get all the links
                        String htmlcode=seedclass.getHtmlCode(url);
                        Document doc = Jsoup.parse(htmlcode);
		
			Elements links = doc.select("li[class=g]");
			int cnt = 0;
			//put all the links into the map 
			for(Element link : links){
    		
				//get the link title
				Elements anchors = link.select("cite[class=vurls]");
				String anchortext = anchors.html();
				anchortext = anchortext.replaceAll("<b>", "");
				anchortext = anchortext.replaceAll("</b>", "");
			
				Elements contents = link.select("span[class=st]");
				String content = contents.text();
			
				//System.out.println("anchor: "+anchortext);
				//System.out.println("content: "+content);
				/*for now the reflevance of every link is 0,
				 * Later incorporate mechanism for calculating the relevance of the link 
				 */
                                if(!(anchortext.contains("...")))
                                    {         
                                    if(!(anchortext.equals("")))
                                        rellink.put(cnt, anchortext);
                                    }
				cnt++;
				}
		}else if(flag == false){
			//get the outlinks
	url="http://"+url;
        String htmlcode=Jsoup.connect(url).get().toString();
                        Document doc = Jsoup.parse(htmlcode);
		
                        Elements links = doc.select("a[href]");
			int cnt = 0;
			for(Element link : links){
				
				//get the link
				String outlink = link.attr("href");
				String text = link.text();
				
				if( ! (outlink.contains("google")) ){
					
					//links with "#" are internal links
					//System.out.println("link: "+outlink);
					//System.out.println("link: "+text);
					
					//add the links to a queue
                                    if(outlink.contains("http:"))
                                    {
                                        rellink.put(cnt, outlink);
                                    }
					cnt++;
                                        
				}
			}
			
		}
		
    	return rellink;
    	}
    	
		
    
    
    public static void downloadpages(TreeMap links, int number, String destinationurl)
    {
        String urls = links.keySet().toString();
        //System.out.println(urls);
        String[] urlarr=urls.substring(urls.indexOf("[")+1, urls.indexOf("]")).split(",");
        
        for(int i=0;i<links.size();i++)
        {
            try
            {
            urlarr[i]=urlarr[i].trim();
            urlarr[i]="http://"+urlarr[i];
            if((urlarr[i].charAt(urlarr[i].length()-1))=='/')
                   urlarr[i]=urlarr[i].substring(0, urlarr[i].length()-1);
            System.out.println("Page downloaded: "+urlarr[i]);
            String htmlcode=Jsoup.connect(urlarr[i]).timeout(5000).get().toString();
            Document htmldoc = Jsoup.parse(htmlcode);
            //htmlcode=htmlcode.replaceAll("//upload.wikimedia.org", "http://Upload.wikimedia.org");
            //Elements srcatt=htmldoc.select("[src]");
            //LinkedList srclink=new LinkedList();
            /*for(Element src : srcatt)
            {
            srclink.add(srcatt.attr("abs:src"));
            }
            System.out.println(srclink.toString());
            */
            
            String docname=urlarr[i].substring(urlarr[i].lastIndexOf("/")+1);
            if(docname.contains("?"))
                docname=docname.substring(0,docname.indexOf("?")-1);
            String dest=destinationurl+"\\Rank"+i+"\\"+"main.html";
            String destloc=destinationurl+"\\Rank"+i;
            
            File fileurl=new File(destloc);
            fileurl.mkdir();
            PrintWriter writer = new PrintWriter(dest, "UTF-8");
            writer.print(htmlcode);
            writer.close();
            }
        catch(Exception ex)
        {
            System.out.println("Error"+ex.getMessage());
            ex.printStackTrace();
        }
            
        }
        
    }

    public static double generatescore(TreeMap seedvector, String linkurl, String stpwrdurl)
    {
        //Double[] seedvectorsc = (Double[])(seedvector.values().toArray());
        //Double[] seedvectorsc = (Double[])(seedvector.values().toArray());
    	double seedmod=0, linkmod=0, sedlik=0, score=0;
        try
        {
        linkurl="http://"+linkurl;
        //System.out.println("linkurl");
        if(robotSafe(new URL(linkurl)))
        {
          //  System.out.println(seedvector.entrySet());
        TreeMap linkvector = generatelinkvec(linkurl, stpwrdurl, seedvector);
        //double seedmod=0, linkmod=0, sedlik=0, score;
        //System.out.println(seedvector.entrySet());
        //System.out.println(linkvector.entrySet());
        for(Object entry : seedvector.entrySet())
        {
            String seedwrd=entry.toString().substring(0, entry.toString().lastIndexOf("="));
            double seedtfi=Double.parseDouble(entry.toString().substring(entry.toString().lastIndexOf("=")+1));
            double linktfi=Double.parseDouble(linkvector.get(seedwrd).toString());
            seedmod=seedmod+(seedtfi*seedtfi);
            linkmod=linkmod+(linktfi*linktfi);
            sedlik=sedlik+(seedtfi*linktfi);
        }
        //System.out.println(sedlik+" "+seedmod +" "+linkmod);
        score=sedlik/Math.sqrt(seedmod*linkmod);
        }
        }
        catch(Exception ex)
        {
            //System.out.println(ex.getMessage());
        }
 
        return score;
    }
    
    public static boolean robotSafe(URL url) 
    {
    String strHost = url.getHost();

	// form URL of the robots.txt file
    String strRobot = "http://" + strHost + "/robots.txt";
    URL urlRobot;
    try { urlRobot = new URL(strRobot);
	} catch (MalformedURLException e) {
	    // something weird is happening, so don't trust it
            System.out.println("Error");
	    return false;
	}

    if (DEBUG) System.out.println("Checking robot protocol " + urlRobot.toString());
    String strCommands;
    try {
       InputStream urlRobotStream = urlRobot.openStream();

	    // read in entire file
       byte b[] = new byte[1000];
       int numRead = urlRobotStream.read(b);
       strCommands = new String(b, 0, numRead);
       while (numRead != -1) {
          numRead = urlRobotStream.read(b);
          if (numRead != -1) {
             String newCommands = new String(b, 0, numRead);
	         strCommands += newCommands;
		}
	    }
       urlRobotStream.close();
	} catch (IOException e) {
	    // if there is no robots.txt file, it is OK to search
	    return true;
	}
        if (DEBUG) System.out.println(strCommands);

	// assume that this robots.txt refers to us and 
	// search for "Disallow:" commands.
	String strURL = url.getFile();
	int index = 0;
	while ((index = strCommands.indexOf(DISALLOW, index)) != -1) {
	    index += DISALLOW.length();
	    String strPath = strCommands.substring(index);
	    StringTokenizer st = new StringTokenizer(strPath);

	    if (!st.hasMoreTokens())
		break;
	    
	    String strBadPath = st.nextToken();

	    // if the URL starts with a disallowed path, it is not safe
	    if (strURL.indexOf(strBadPath) == 0)
            {
                //System.out.println("Error");
		return false;
            }
	}

	return true;
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
    
}
