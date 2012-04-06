package se.lingonskogen.gae.swejug.content;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import se.lingonskogen.gae.swejug.beans.Content;
import se.lingonskogen.gae.swejug.beans.DateUser;
import se.lingonskogen.gae.swejug.beans.Link;
import se.lingonskogen.gae.swejug.beans.Meta;
import se.lingonskogen.gae.swejug.modules.user.User;

public abstract class ContentJsonBuilder<T extends Content> implements JsonBuilder<T>
{
   public T buildContent(JSONObject json, T content) throws JSONException
   {
      JSONObject jsonLink = json.getJSONObject(Content.LINK);
      if (jsonLink != null)
      {
         boolean add = false;
         Link link = new Link();
         String rel = jsonLink.getString(Link.REL);
         if (rel != null)
         {
            link.setRel(rel);
            add = true;
         }
         String label = jsonLink.getString(Link.LABEL);
         if (label != null)
         {
            link.setLabel(label);
            add = true;
         }
         String url = jsonLink.getString(Link.URL);
         if (url != null)
         {
            link.setUrl(url);
            add = true;
         }
         if (add)
         {
            content.setLink(link);
         }
      }
      JSONObject jsonMeta = json.getJSONObject(Content.META);
      if (jsonMeta != null)
      {
         JSONArray jsonTotals = jsonMeta.getJSONArray(Meta.TOTAL);
         if (jsonTotals != null)
         {
            for (int i = 0; i < jsonTotals.length(); i++)
            {
               JSONObject jsonTotal = jsonTotals.getJSONObject(i);
               for (@SuppressWarnings("unchecked")
               Iterator<String> it = jsonTotal.keys(); it.hasNext();)
               {
                  String key = it.next();
                  long total = jsonTotal.getLong(key);
                  content.getMeta().getTotal().put(key, total);
               }
            }
         }
      }
      return content;
   }

   public JSONObject buildJSONObject(JSONObject json, T content) throws JSONException
   {
      JSONObject jsonMeta = new JSONObject();
      jsonMeta.put(Meta.TYPE, content.getMeta().getType());
      DateUser created = content.getMeta().getCreated();
      if (created != null)
      {
         JSONObject jsonCreated = new JSONObject();
         boolean add = false;
         Date date = created.getDate();
         if (date != null)
         {
            jsonCreated.put(DateUser.DATE, date);
            add = true;
         }
         User user = created.getUser();
         if (user != null)
         {
            jsonCreated.put(DateUser.USER, user.getFullname()); // should be a proper user
            add = true;
         }
         if (add)
         {
            jsonMeta.put(Meta.CREATED, jsonCreated);
         }
      }
      DateUser updated = content.getMeta().getUpdated();
      if (updated != null)
      {
         JSONObject jsonUpdated = new JSONObject();
         boolean add = false;
         Date date = updated.getDate();
         if (date != null)
         {
            jsonUpdated.put(DateUser.DATE, date);
            add = true;
         }
         User user = updated.getUser();
         if (user != null)
         {
            jsonUpdated.put(DateUser.USER, user.getFullname()); // should be a proper user
            add = true;
         }
         if (add)
         {
            jsonMeta.put(Meta.CREATED, jsonUpdated);
         }
      }
      JSONArray jsonTotals = new JSONArray();
      Map<String, Long> totals = content.getMeta().getTotal();
      for (String key : totals.keySet())
      {
         JSONObject jsonTotal = new JSONObject();
         jsonTotal.put(key, content.getMeta().getTotal().get(key));
         jsonTotals.put(jsonTotal);
      }
      if (jsonTotals.length() > 0)
      {
         jsonMeta.put(Meta.TOTAL, jsonTotals);
      }
      Link link = content.getLink();
      if (link != null)
      {
         boolean add = false;
         JSONObject jsonLink = new JSONObject();
         String rel = link.getRel();
         if (rel != null)
         {
            jsonLink.put(Link.REL, rel);
            add = true;
         }
         String label = link.getLabel();
         if (label != null)
         {
            jsonLink.put(Link.LABEL, label);
            add = true;
         }
         String url = link.getUrl();
         if (url != null)
         {
            jsonLink.put(Link.URL, url);
            add = true;
         }
         if (add)
         {
            json.put(Content.LINK, jsonLink);
         }
      }
      json.put(Content.META, jsonMeta);
      return json;
   }

}
