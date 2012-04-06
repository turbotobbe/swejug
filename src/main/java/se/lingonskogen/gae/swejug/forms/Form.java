package se.lingonskogen.gae.swejug.forms;

import java.util.ArrayList;
import java.util.List;

public class Form
{
   public static final String ACTION = "action";
   
   public static final String INPUTS = "inputs";
   
   private Action action;
   
   private final List<Input> inputs = new ArrayList<Input>();

   public Action getAction()
   {
      return action;
   }

   public void setAction(Action action)
   {
      this.action = action;
   }

   public void addInput(Input input)
   {
      inputs.add(input);
   }

   public List<Input> getInputs()
   {
      return inputs;
   }
   
}
