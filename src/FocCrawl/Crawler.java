package FocCrawl;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.TreeMap;

/*
 * Crawler
TreeMap scoreURL
Queue ParseDoc
Add seed URL to queue
Loop
o Pop URL from queue
o Send to RefPage class
o Accept score and add in TreeMap scoreURL
o Accept rellink map and add links to queue according to rel score
Display scoremap
 */

public class Crawler {
	
	//private static final String USER_AGENT = null;
	public static LinkAnalysis linkanalysis = new LinkAnalysis();
	public static	SeedAnalysis seedanalysis = new SeedAnalysis();
	public static TreeMap scoreURL = new TreeMap();
	public static comparator com = new comparator(scoreURL);
	public static TreeMap new_scoreURL = new TreeMap(com);
	
	public static void main(String[] args) throws Exception
        {
		
		TreeMap rellink = new TreeMap();
		LinkedList<String> queue = new LinkedList<String>();
		Scanner s = new Scanner(System.in);
                String stpwrdurl="D:\\College works\\TE\\Ryerson\\Labs\\InformationRetievalandWebSearch\\Project\\Code\\stopwords.txt";
		//get the topic to be searched
		System.out.println("Enter your topic: ");
		String topic = s.nextLine();
		String[] queryarr = topic.split(" ");
		
		StringBuilder sBuild = new StringBuilder();
		for(int i=0;i<queryarr.length;i++)
                {
			if(i>0)
                        {
				sBuild.append("+");
			}
			sBuild.append(queryarr[i]);
		}
		//searching the topic on google
		String seedurl = "https://www.google.com/search?q="+sBuild.toString();
		//System.out.println(seedurl);
		//Adding seed url to queue
				queue.addLast(seedurl);
				
		//for seed treemap use the wikipedia page; for links use google page
				StringBuilder sBuild1 = new StringBuilder();
				for(int i=0;i<queryarr.length;i++)
                                {
					if(i>0)
                                        {
						sBuild1.append("_");
					}
					sBuild1.append(queryarr[i]);
				}
		
		int cnt = 1;
                System.out.println("How many pages do you want to crawl ?");
                int howMany = s.nextInt();
                String tarun_seedurl="http://en.wikipedia.org/wiki/"+sBuild1.toString();
                //System.out.println("tarun"+tarun_seedurl);
		TreeMap seedVector = seedanalysis.generateseedvector(tarun_seedurl, stpwrdurl);
                while(  (cnt<howMany) && (! (queue.isEmpty())))
                {
                        double score=0;
			String url = queue.removeFirst();
                        url=url.replaceAll("http://", "");
                        //System.out.println(queue.toString());
                        if(!(url.contains("google")))
                        {
                            try
                            {
		            score = linkanalysis.generatescore(seedVector, url, stpwrdurl);
                            }
                            catch(Exception E)
                            {
                                cnt--;
                            }
                        }
                        if(!(Double.isNaN(score)))
                        {
                            if(score!=0)
                            {
                               System.out.println("URL is "+url+": "+Double.toString(score));
                                scoreURL.put(url,score);
                            	
			cnt++;
                        }
                        }
                        try
                        {
                        if(url.equals(seedurl))
                                {
                                        rellink = linkanalysis.linksbyrelevance(url,true);	//flag=true for seed url
                                  //      System.out.println("Hello");
                                }
                        else if( (url.contains("www.")))
                        {
                                 rellink = linkanalysis.linksbyrelevance(url,false);
                                // System.out.println(rellink.entrySet());
                        }
                        else
                                rellink.clear();
                        }
                        catch (Exception e)
                        {
                            cnt--;
                        }
			//System.out.println(rellink);
			
			for(Object entry : rellink.entrySet())
                        {
                                
				entry=entry.toString().substring(entry.toString().indexOf('=')+1);
				queue.addLast(entry.toString());	
                                
			}
			}
                        System.out.println("cnt = "+cnt);
                        new_scoreURL.putAll(scoreURL);
                        System.out.println("Final output"+new_scoreURL);
                        //System.out.println("How many pages do you want to download?");
                        //int no = s.nextInt();
                        linkanalysis.downloadpages(new_scoreURL, howMany, "D:\\College works\\TE\\Ryerson\\Labs\\InformationRetievalandWebSearch\\Project\\DownloadedPages");
                
                
        }

}
