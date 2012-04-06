package se.lingonskogen.gae.swejug.system.model;


public class Tupel<L,R>
{
   private L left;
   
   private R right;

   public Tupel()
   {
   }
   
   public Tupel(L left, R right)
   {
      this.left = left;
      this.right = right;
   }

   public L getLeft()
   {
      return left;
   }

   public void setLeft(L left)
   {
      this.left = left;
   }

   public R getRight()
   {
      return right;
   }

   public void setRight(R right)
   {
      this.right = right;
   }
   
}
