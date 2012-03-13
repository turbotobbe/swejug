package se.lingonskogen.gae.swejug.json;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


public class JsonNode
{
   private JSONObject json = new JSONObject();
   private Map<String, JsonNode> nodes = new HashMap<String, JsonNode>();
   private Map<String, Map<String, JsonNode>> listnodes = new HashMap<String, Map<String, JsonNode>>();
   
   public void add(String key, Object val) throws JSONException
   {
      int pos = key.indexOf(ContentStore.DIV);
      if (pos < 0)
      {
         json.put(key, val);
      }
      else
      {
         String ikey = key.substring(0, pos);
         String rkey = key.substring(pos+1);
         
         int p = rkey.indexOf(ContentStore.DIV);
         if (p > 0 && rkey.substring(0, p).matches("[0-9]+"))
         {
            String ci = rkey.substring(0, p);
            String cr = rkey.substring(p+1);

            if (!listnodes.containsKey(ikey))
            {
               listnodes.put(ikey, new HashMap<String, JsonNode>());
            }
            Map<String, JsonNode> map = listnodes.get(ikey);
            if (!map.containsKey(ci))
            {
               map.put(ci, new JsonNode());
            }
            JsonNode node = map.get(ci);
            node.add(cr, val);
         }
         else
         {
            if (!nodes.containsKey(ikey))
            {
               nodes.put(ikey, new JsonNode());
            }
            JsonNode node = nodes.get(ikey);
            node.add(rkey, val);
         }
      }
   }
   
   public JSONObject toJSON() throws JSONException
   {
      for (String key : nodes.keySet())
      {
         JsonNode node = nodes.get(key);
         json.put(key, node.toJSON());
      }
      for (String key : listnodes.keySet())
      {
         JSONArray jArr = new JSONArray();
         Map<String, JsonNode> map = listnodes.get(key);
         for (String k : map.keySet())
         {
            JsonNode node = map.get(k);
            jArr.put(node.toJSON());
         }
         json.put(key, jArr);
      }
      return json;
   }

   @Override
   public String toString()
   {
      return "JsonNode [json=" + json + ", nodes=" + nodes + ", listnodes=" + listnodes + "]";
   }

   
}
