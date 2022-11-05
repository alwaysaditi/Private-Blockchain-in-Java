public class Transaction
  {
      int amount;
int s_id;
int r_id;
   
    boolean valid=false;
    Transaction()
    {
      
    }
Transaction (int s_id, int r_id, int amount)
    {
    
    
      this.s_id = s_id;
      this.r_id=r_id;
      this.amount = amount ;
      //return this;
    
    }

  }