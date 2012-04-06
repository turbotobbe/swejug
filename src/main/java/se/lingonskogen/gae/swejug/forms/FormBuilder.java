package se.lingonskogen.gae.swejug.forms;

import java.util.logging.Logger;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;


public class FormBuilder
{
   private static final String CLASS = FormBuilder.class.getName();
   private static final Logger LOG = Logger.getLogger(CLASS);
   
   public JSONObject toJSON(Form form) throws JSONException
   {
      JSONObject jsonForm = new JSONObject();
      Action action = form.getAction();
      JSONObject jsonAction = new JSONObject();
      jsonAction.put(Action.LABEL, action.getLabel());
      jsonAction.put(Action.METHOD, action.getMethod().toString());
      jsonAction.put(Action.URL, action.getUrl());
      jsonAction.put(Action.REDIRECT, action.getRedirect());
      jsonForm.put(Form.ACTION, jsonAction);
      
      JSONArray jsonInputs = new JSONArray();
      for (Input input : form.getInputs())
      {
         JSONObject jsonInput = new JSONObject();
         jsonInput.put(Input.ID, input.getId());
         jsonInput.put(Input.LABEL, input.getLabel());
         jsonInput.put(Input.DESCRIPTION, input.getDescription());
         jsonInput.put(Input.TYPE, input.getType().toString());
         jsonInput.put(Input.REQUIRED, input.getRequired());
         jsonInputs.put(jsonInput);
      }
      jsonForm.put(Form.INPUTS, jsonInputs);
      return jsonForm;
   }

}
