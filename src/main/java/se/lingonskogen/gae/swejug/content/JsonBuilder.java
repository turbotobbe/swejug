package se.lingonskogen.gae.swejug.content;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import se.lingonskogen.gae.swejug.beans.Content;

public interface JsonBuilder<T extends Content>
{
   T buildContent(JSONObject json) throws JSONException;
   
   JSONObject buildJSONObject(T content) throws JSONException;
}
