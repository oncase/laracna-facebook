import java.util.Iterator;
import java.util.List;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.types.Comment;
import com.restfb.types.Insight;
import com.restfb.types.Page;
import com.restfb.types.Post;

public class Teste {
	public static void main(String[] args) {
		String token = "EAACEdEose0cBABORSLV0gCfRkGWrZCWoLq2P9ZB1ds6sTYBw2mUr74Aw3XDjLJ1mhlDAQHif3hjwShvWjlh2vvV5tBhopaCrMFBexcs24ZCZCuSYc7LtIG7pZAkozxbVOnGCipYf4C31kpOXYNRgnGJHTJ5uYqHgMYV132kJ3TjneA2Xl8lK8ur1zZAHg6q0bT8vz71tqobAZDZD";
		FacebookClient fb = new DefaultFacebookClient(token, Version.VERSION_2_6);
		Page page = fb.fetchObject("cirogomesoficial", Page.class, Parameter.with("fields", "likes.limit(0).summary(true)"));
		Connection<Post> posts = fb.fetchConnection("oncasesolucoes/feed", Post.class);
		int count = 0;
		Post postEscolhido = null;
		for (List<Post> feedPage : posts) {
			for(Post post :feedPage) {
				Post p = fb.fetchObject(post.getId(),
						  Post.class,
						  Parameter.with("fields", "from,to,likes.limit(0).summary(true),comments.limit(0).summary(true),shares.limit(0).summary(true)"));
						System.out.println("Mensagem: " + post.getMessage());
						System.out.println("Likes count: " + p.getLikesCount());
						System.out.println("Shares count: " + p.getSharesCount());
						System.out.println("Comments count: " + p.getCommentsCount());
				
				count++;
			}
		}
		
		/*Connection<Insight> insightsConnection = 
				fb.fetchConnection("oncasesolucoes/insights", 
				      Insight.class, // the insight type
				      Parameter.with("metric", "total_count"));

			for (List<Insight> insights: insightsConnection) {
			  for (Insight insight : insights) {
			    System.out.println(insight.getName());
			  }
			}*/
		
		//System.out.println(count);
	}
}
