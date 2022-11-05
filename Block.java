import java.time.*;
import java.security.MessageDigest ;  
public class Block 
{
byte[] data;//signed and hashed data
int nonce;
String prevHash;
int blocknumber;
Instant timestamp ;
  Block(byte[] data, int nonce,String prevhash, int blocknumber )
  {
    this.data=data;
    this.nonce= nonce;
    this.prevHash = prevhash;
    this.blocknumber=blocknumber;
    this.timestamp = Instant.now();
  }

  
}