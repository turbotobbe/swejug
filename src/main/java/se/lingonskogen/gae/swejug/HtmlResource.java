package se.lingonskogen.gae.swejug;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path ("/")
@Produces (MediaType.TEXT_HTML)
public class HtmlResource
{
    @GET
    public String getHome()
    {
        List<String> list = Collections.emptyList();
        return build(list);
    }
    
    @GET
    @Path ("{alpha}")
    public String getAlpha(@PathParam ("alpha") String alpha)
    {
        List<String> list = Collections.singletonList(alpha);
        return build(list);
    }

    @GET
    @Path ("{alpha}/{beta}")
    public String getBeta(@PathParam ("alpha") String alpha,
                           @PathParam ("beta") String beta)
    {
        List<String> list = new ArrayList<String>();
        list.add(alpha);
        list.add(beta);
        return build(list);
    }

    private String build(List<String> list)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<title>");
        sb.append(list.size() > 0 ? list.get(list.size()-1) : "SweJug");
        sb.append("</title>");
        sb.append("</head>");
        sb.append("<body>");
        sb.append("<h1>SweJug</h1>");
        for (String name : list)
        {
            sb.append(name);
            sb.append(" - ");
        }
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }

}
