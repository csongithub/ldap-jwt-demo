/**
 * 
 */
package biz.neustar.idaas.ldap.connector;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;



/**
 * Standard interface to be used for Password encryption/decryption when stored
 * in property files or other locations. The default ALGORITHM is "AES" with
 * "CBC" block mode and "PKCS5Padding" Implementations can override this if
 * needed to for nearly all cases the AES/CBC/PKCS5Padding is more than
 * sufficient.
 * 
 * 
 * 
 * @author Scott Brand (Scott Brand)
 *
 */
public interface IPasswordEncryptor
{
    /**
     * Configuration of used algorithm. Do not use less secure DES. Setting up
     * another value can prevent of using this software and will require to
     * specify some additional parameters for initial vector.
     */
    static final String ALGORITHM  = "AES";

    /**
     * Configuration of block mode. Do not use less secure ECB mode.
     */
    static final String BLOCK_MODE = "CBC";

    /**
     * Configuration of "padding".
     */
    static final String PADDING    = "PKCS5Padding";

    /**
     * Definition that should be used in a properties or config file that is
     * read to get the "salt" seed value. In the properties file, this value is
     * typically Hex or Base64 encoded
     */
    static final String SALT       = "salt";

    /**
     * Definition that should be used in a properties or config file that is
     * read to get the secret key value used to encrypt/decrypt. In the
     * properties file, this value is typically Hex or Base64 encoded
     */
    static final String KEY        = "key";


    /**
     * Used by implementation providers to describe the type encrypting and decrypting
     * encoding they use.
     * A provider that declares the property of HEX_ENCODING means that it expects to
     * read and write the encrypted byte[] initially as an upper case HEX encoding String.
     * And that string is usually wrapped by ENC(..)
     */
    static final String HEX_ENCODING = "encoding.type=hex";
    static final String HEX_ENCODING_FILTER = "(" + HEX_ENCODING + ")";

    
    /**
     * Used by implementation providers to describe the type encrypting and decrypting
     * encoding they use.
     * A provider that declares the property of BASE64_ENCODING means that it expects to
     * read and write the encrypted byte[] initially as a BASE64 encoding String.
     * And that string is usually wrapped by ENC(..)
     */
    static final String BASE64_ENCODING = "encoding.type=base64";


    /**
     * Default implementation is {@link #ALGORITHM}
     * 
     * @return
     */
    default String getAlgorithm()
    {
        return ALGORITHM;
    }





    /**
     * Default implementation is {@link #BLOCK_MODE}
     * 
     * @return
     */
    default String getBlockMode()
    {
        return BLOCK_MODE;
    }





    /**
     * Default implementation is {@link #PADDING}
     * 
     * @return
     */
    default String getPadding()
    {
        return PADDING;
    }





    /**
     * Given an encryptedText string, use the current algorithm/block/padding
     * along with defined IV value and SecretKey to return the dencrypted
     * String.
     * 
     * 
     * @param encryptedText
     *            - Usually a Hex or Base64 encoded encrypted string
     * @return clear text decrypted String
     * @throws Exception
     *             if any aspect of the decryption could not occur.
     */
    String decrypt(String encryptedText) throws Exception;





    /**
     * Given a clear text string, use the current algorithm/block/padding along
     * with defined IV value and SecretKey to return the encrypted String.
     * 
     * 
     * @param clearText
     *            - The text string to be encrypted
     * @return the encrypted String. The value is usually Hex or Base64 encoded
     *         depending on the implementation
     * @throws Exception
     *             if any aspect of the encryption could not occur.
     */
    String encrypt(String clearText) throws Exception;





    /**
     * Implementation class returns an instance of IvParameterSpec appropriately
     * created with a good salt value. For the currently defined
     * "AES/CBC/PKCS5Padding" the IV is a byte[16].
     * 
     * @return IvParameterSpec to be used as the salt value
     */
    IvParameterSpec getIvParameterSpec();





    /**
     * Implementation class returns an instance of SecretKeySpec appropriately
     * created with a sufficient key value. For the currently defined
     * "AES/CBC/PKCS5Padding" the key is a byte[16].
     * 
     * @return SecretKeySpec to be used as the key value to be used for
     *         encryption
     */
    SecretKeySpec getSecretKeySpec();

}
