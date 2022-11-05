import java.util.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
 
  

class Main {
   private static final String
        SIGNING_ALGORITHM
        = "SHA256withRSA";
    private static final String RSA = "RSA";
  public static int blocknumber =0;// these variables are static to allow changing from any method and to keep the samevalue throughout the code
  public static int nonce =0;
  public static Block blockchain[] = new Block[10];
  public static void main(String[] args) {
    for(int i=0;i<10;i++)
      {
        blockchain[i]=null;
      }
    int p=0;
    Transaction array[] = new Transaction[10];//placing this outside while loop was advantageous because it allowed you to not re-initialize the array over and over again and solved the HUGE ERROR
        byte [][] datarec = new byte[10][];//signatures 
    byte [][] origdata = new byte [10][];//actual data that was signed
    PublicKey publickeys[] = new PublicKey[10];
   
    for(int k=0;k<10;k++)
      {
        datarec[k] = null;
        publickeys[k]=null;
        origdata[k] = null;
      }
    System.out.println("Blockchain with genesis block");
    createBlockchain(null);
    while(true)
      {
         System.out.println("Hello world!");
    User miner = new User(1,"miner","miner","miner1",0);
     User sender = new User(2,"sender","sender","sender1",100);
     User receiver = new User(3,"receiver","receiver","receiver1",200);
    User[] users = new User[3];
      users[0] = miner;
  users[1] = sender;
  users[2] = receiver;  
        KeyPairGenerator keyPairGen = null;
        KeyPair keyPair = null;
        PublicKey publicKey = null;
        PrivateKey privateKey = null;
        // necessary to initialize these before since what if we are not able to initialize in try-catch?
        try {
keyPairGen = KeyPairGenerator.getInstance("RSA");
keyPairGen.initialize(2048);
 keyPair    = keyPairGen.genKeyPair();
publicKey  = keyPair.getPublic();
privateKey = keyPair.getPrivate();
         // System.out.println("private key = "+privateKey);
        }
        catch (Exception e5)
          {
            
          }

        
 for(int i=0;i<3;i++)
      {
        System.out.println("username: "+users[i].id);
        System.out.println("password "+users[i].password);
      }
    Main obj2 = new Main();
    //*****************AUTHENTICATING WHICH USER WANTS TO SIGN IN*************************
    int user;
    user =obj2.signin(users);
    Scanner sc = new Scanner (System.in);
     if(user ==1)
    {
      System.out.println(" your current balance is "+users[0].crypto);
      System.out.println("successfully authenticated miner!");
      System.out.println("The list of invalidated transactions (as of now) are as follows ");
      for(int a=0;a<p;a++)
        {
          if(array[a]!=null)
          {
           System.out.println("serial number= "+a+" sender id = "+array[a].s_id+" receiver id= "+array[a].r_id+" amount = "+array[a].amount); 
          }
          
        }
      int t_id;
      System.out.println("select a transaction you would like to validate ");
      t_id = sc.nextInt();
      System.out.println("Chosen transaction sender id = "+array[t_id].s_id+" amount= "+array[t_id].amount+"current validation status = "+array[t_id].valid);
      System.out.println("chosen transaction digital signature= "+byteArrayToHex(datarec[t_id]));
      System.out.println("click 'y' to validate digital signature ");
      char input ;
      boolean authenticated =false;
      input = sc.next().charAt(0);
     
      System.out.println("Step1: Authenticate digital signature ");
      try
        {
          authenticated = Verify_Digital_Signature(origdata[t_id], datarec[t_id], publickeys[t_id]);
        }
      catch (Exception e7)
        {
          
        }
      
      if(authenticated==true)
      {
        System.out.println("sucessfully authenticated transaction!");
       
      }
      else
      {
        System.out.println("transaction is not authenticated !");
      }
      System.out.println("Step2: verifying the UTXOs");
      int amount_trans = array[t_id].amount;
      boolean validated = false; 
      if(amount_trans<= users[1].crypto)
      {
        validated = true;
        System.out.println("Successfully validated transaction!");
        //users[1].crypto = users[1].crypto-amount_trans;
      }
      else 
      {
        System.out.println("Transaction is invalid!");
      }

      if((validated==true)&&(authenticated==true))
      {
        byte shasign[]=null;
        boolean solved=false;
        System.out.println("Transaction can now be added to block!");
        System.out.println("STEP 3: checking proof of work");
      
        shasign = HashandPoW(datarec[t_id]);
        if(shasign!=null)
        {
          solved=true;
        }
          if(solved==true)
        {
          System.out.println("miner amount= "+users[0].crypto);
        users[0].crypto = users[0].crypto+ 5;
           System.out.println("miner amount updated = "+users[0].crypto);
          System.out.println("sender amount= "+users[1].crypto);
        users[1].crypto = users[1].crypto-amount_trans;
          System.out.println("sender amount updated = "+users[1].crypto);
          System.out.println("receiver amount= "+users[2].crypto);
        users[2].crypto = users[2].crypto+amount_trans;
        System.out.println("receiver amount updated= "+users[2].crypto);  
        System.out.println("you have been blessed with 1 bitcoin. your current balance is "+users[0].crypto);
        datarec[t_id] = null;
        publickeys[t_id]=null;
        origdata[t_id] = null;
         array[t_id].valid = true;
        System.out.println("Current status of transaction is ");
         System.out.println("Chosen transaction sender id = "+array[t_id].s_id+" amount= "+array[t_id].amount+"current validation status = "+array[t_id].valid);
        array[t_id]=null;
          System.out.println("*************************************************");
          System.out.println("STEP4: Adding block to the chain");
          boolean created = createBlockchain(shasign);
            
        }
       
       
      }
      
       
      
      
      
    }
    else if(user==2){
      System.out.println("successfully authenticated payer!");
      
    int amount;
    int choice;
    System.out.println("Enter 1 to check your current balance or 2 to transfer money");
    choice = sc.nextInt();
      if(choice==1)
      {
       System.out.println("The current balance you have is "+users[1].crypto); 
      }
      else
      {
    System.out.println("Enter the amount that you wish to transfer");
    
    amount = sc.nextInt();
    //obj.amount = amount;
    Transaction obj = new Transaction(1,2,amount);
    array[p]=obj;
      
    ByteArrayOutputStream bos = new ByteArrayOutputStream();//for writing the result to a byte array
    ObjectOutputStream oos;
    try
      {
       oos = new ObjectOutputStream(bos);
         oos.writeObject(obj);
      oos.flush();
      }
    catch (IOException e)
      {
        //System.out.println(e.getstackTrace());
      }
    //for writing a sort of  serializable object
   
    byte [] data = bos.toByteArray();
      origdata[p] = data;
      //datarec[p] = data;
      try{
      
byte signature[] = Create_Digital_Signature(data, privateKey);
    //byteArrayToHex(data);
   datarec[p]=signature;
    publickeys[p]= publicKey; 
        System.out.println("added signature to record ");
      }
      catch (Exception e4)
        {
          
        }
      // 

      
      
      // for(int k=0;k<=p;k++)
      //   {
      //     System.out.println("index= "+k+" amount = "+array[k].amount+" digital signature ="+byteArrayToHex(datarec[p]));
      //   }
    p++;}}
    else if (user==3)
    {
      System.out.println("successfully authenticated payee!");
      System.out.println("your current balance is "+users[2].crypto);
    } 
    }
    
      }
   
    
  public static byte[] HashandPoW(byte[] signeddata)
  {
    boolean pow=false;
     nonce =0;
String signeddatavar = byteArrayToHex(signeddata);
    String check ="";
    String temp="";
    byte[] shasign=null;
    while(true)
      {
        temp = signeddatavar;
        System.out.println("nonce= "+nonce);
        temp = temp+Integer.toString(nonce);
        try{
          shasign = getSHA(temp);
          check = byteArrayToHex(shasign);
          if(check.startsWith("00")&&!(check.startsWith("000")))
          {
            System.out.println("Proof of work solved with nonce value = "+nonce);
            System.out.println("SHA256 of signed data= "+check);
            pow = true;
            break;
          }
        }
        catch(NoSuchAlgorithmException e)
          {
            
          }
        nonce++;
        
      }
    return shasign;
  }

  int signin(User []users)
  {
    Scanner sc = new Scanner(System.in);
    //int id;
    //String password;
    System.out.println("Enter the user id");
    int idcheck = sc.nextInt();
    int i;
     System.out.println("Enter the password");
    String password = sc.next();
    boolean found=false;
    for( i=0;i<3;i++)
      {
        if(users[i].id==idcheck)
        {
          System.out.println("yes");
          if(users[i].password.equals(password))
          {
            found = true;
            break;
          }
          
        }
      }
   
    return (i+1);
    
  }



   // Function to implement Digital signature
    // using SHA256 and RSA algorithm
    // by passing private key.
    public static byte[] Create_Digital_Signature(
        byte[] input,
        PrivateKey Key)
        throws Exception
    {
    
     
         Signature signature
            = Signature.getInstance(
                SIGNING_ALGORITHM);
        signature.initSign(Key);
        signature.update(input);
        return signature.sign();
      
      
      
    }
  
    // Generating the asymmetric key pair
    // using SecureRandom class
    // functions and RSA algorithm.
    public static KeyPair Generate_RSA_KeyPair()
        throws Exception
    {
        SecureRandom secureRandom
            = new SecureRandom();
        KeyPairGenerator keyPairGenerator
            = KeyPairGenerator
                  .getInstance(RSA);
        keyPairGenerator
            .initialize(
                2048, secureRandom);
        return keyPairGenerator
            .generateKeyPair();
    }
  
    // Function for Verification of the
    // digital signature by using the public key
    public static boolean
    Verify_Digital_Signature(
        byte[] input,
        byte[] signatureToVerify,
        PublicKey key)//signature done using Private key is verified by public key
        throws Exception
    {
        Signature signature
            = Signature.getInstance(
                SIGNING_ALGORITHM);
        signature.initVerify(key);
        signature.update(input);
        return signature
            .verify(signatureToVerify);
    }

  public static String byteArrayToHex(byte[] a) {//for converting byte array to string
   StringBuilder sb = new StringBuilder(a.length * 2);
   for(byte b: a)
      sb.append(String.format("%02x", b));
    //System.out.println("Digital signature= "+sb.toString());
   return sb.toString();
}
   public static byte[] getSHA(String input) throws NoSuchAlgorithmException
    {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = MessageDigest.getInstance("SHA-256");
 
        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }
public static boolean createBlockchain(byte[]shdata)
  {
    String prevhash;
   
   if(blocknumber==0)
   {
    String init= "0000000000000000000000000";
    byte [] initarr = init.getBytes();
    //int initnonce =0;
    String prevhashinit="00";
     Block genesis = new Block(initarr,nonce,prevhashinit,blocknumber);
    blockchain[blocknumber] = genesis;
     blocknumber++;
   }

    else
   {
    prevhash = byteArrayToHex(blockchain[blocknumber-1].data); 
     Block newblock = new Block(shdata,nonce,prevhash,blocknumber);
     blockchain[blocknumber]=newblock;
     blocknumber++;
   }

  int i=0;
    while(blockchain[i]!=null)
      {
        System.out.println("Block number= "+blockchain[i].blocknumber);
        System.out.println("Nonce= "+blockchain[i].nonce);
        System.out.println("Previous block's hash= "+blockchain[i].prevHash);
        System.out.println("Hash root of data = "+byteArrayToHex(blockchain[i].data));
        System.out.println("Timestamp= "+blockchain[i].timestamp);
        System.out.println("******************************************************");
        i++;
      }
   
  return false;
  }
}