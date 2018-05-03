package br.com.oncase.control;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.Post;

import br.com.oncase.utils.CSVUtils;


public class FacebookCrawler {
	private FacebookClient fb;

	public FacebookCrawler() {
		
	}
	public FacebookCrawler(String token) {
		fb = new DefaultFacebookClient(token, Version.VERSION_2_6);
	}
	public FacebookClient getFb() {
		return fb;
	}
	public void setFb(FacebookClient fb) {
		this.fb = fb;
	}
	
	public List<br.com.oncase.model.Post> getPosts(String publisher){
		List<br.com.oncase.model.Post> r = new ArrayList<br.com.oncase.model.Post>();
		Connection<com.restfb.types.Post> posts = fb.fetchConnection(publisher+"/feed", Post.class);
		for (List<Post> feedPage : posts) {
			for(Post post :feedPage) {
				Post p = fb.fetchObject(post.getId(),
						  Post.class,
						  Parameter.with("fields", "from,to,likes.limit(0).summary(true),comments.limit(0).summary(true),shares.limit(0).summary(true)"));
				
				br.com.oncase.model.Post temp = new br.com.oncase.model.Post();
				temp.setTitle(p.getMessage());
				temp.setLikesCount(p.getLikesCount().intValue());
				temp.setReactCount(p.getReactionsCount().intValue());
				temp.setCommentCount(p.getCommentsCount().intValue());
				temp.setShareCount(p.getSharesCount().intValue());
				r.add(temp);
			}
		}
		return r;
	}
	
	public void getPosts(String publisher, String fileOutput){
		Connection<com.restfb.types.Post> posts = fb.fetchConnection(publisher+"/feed", Post.class);
		FileWriter writter = null;
		try {
			writter = new FileWriter(fileOutput);
			CSVUtils.writeLine(writter, Arrays.asList("Publisher", "Message", "Likes","Reactions" ,"Comments", "Share", "Data"));
		
			for (List<Post> feedPage : posts) {
				for(Post post :feedPage) {
					Post p = fb.fetchObject(post.getId(),
							  Post.class,
							  Parameter.with("fields", "from,to,likes.limit(0).summary(true),comments.limit(0).summary(true),shares.limit(0).summary(true)"));
					if(p != null) {
						String message = post.getMessage();
						if(message != null) {
							message = message.replace("\n", "").replace("\r", "");
						}
						SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy ");
						String data_criacao = formato.format(post.getCreatedTime()).toString();
						System.out.println(post.getId());
						CSVUtils.writeLine(writter, Arrays.asList(publisher
								, message 
								, p.getLikesCount().intValue()+""
								,p.getReactionsCount().intValue() +""
								,p.getCommentsCount().intValue()+""
								, p.getSharesCount().intValue()+""
								,data_criacao)
								);	
					}
				}
			}
			
			writter.flush();
			writter.close();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		String token = "EAACEdEose0cBABoCKsvYcbeGnxyIjC6yXycPixJDQeMYm9Lwnp9CnHpBYcSB2hwyWZCIQlCorsoTw8IpLBwtKVHZB8Gy6DwnfcyOWJ4HiNSkhGxmxoq4g0YMSx4TPiqmoY5MjOoHOXZBR8UtobbIq8rOJiT0pXY8F2IueDjFtTe6kiyJGWqcQbWohTTvIxdMb682b4N4gZDZD";
		String csvFile = "D:\\projetos\\eleicao\\tarantulla-facebook\\data\\cadidatos.csv";
		FacebookCrawler fc = new FacebookCrawler(token);
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                String publisher = line;
                String file = "D:\\\\projetos\\\\eleicao\\\\tarantulla-facebook\\\\data\\\\"+publisher+".csv";
                System.out.println(file);
                fc.getPosts(publisher, file);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
		
		
		
		
		
		
	}
}
