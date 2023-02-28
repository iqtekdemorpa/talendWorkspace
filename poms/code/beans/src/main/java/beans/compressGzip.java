package beans;

/*
 * user specification: the function's comment should contain keys as follows: 1. write about the function's comment.but
 * it must be before the "{talendTypes}" key.
 * 
 * 2. {talendTypes} 's value must be talend Type, it is required . its value should be one of: String, char | Character,
 * long | Long, int | Integer, boolean | Boolean, byte | Byte, Date, double | Double, float | Float, Object, short |
 * Short
 * 
 * 3. {Category} define a category for the Function. it is required. its value is user-defined .
 * 
 * 4. {param} 's format is: {param} <type>[(<default value or closed list values>)] <name>[ : <comment>]
 * 
 * <type> 's value should be one of: string, int, list, double, object, boolean, long, char, date. <name>'s value is the
 * Function's parameter name. the {param} is optional. so if you the Function without the parameters. the {param} don't
 * added. you can have many parameters for the Function.
 * 
 * 5. {example} gives a example for the Function. it is optional.
 */

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

public class compressGzip {

    /**
     * helloExample: not return value, only print "hello" + message.
     * 
     * 
     * {talendTypes} String
     * 
     * {Category} User Defined
     * 
     * {param} string("world") input: The string need to be printed.
     * 
     * {example} helloExemple("world") # hello world !.
     */
	public static Map<String, byte[]> compressString(String input, String acceptEncodingHeader) {
	    byte[] inputData = input.getBytes();
	    Map<String, byte[]> compressedData = new HashMap<>();

	    if (acceptEncodingHeader != null) {
	        String[] encodings = acceptEncodingHeader.split("\\s*,\\s*");

	        for (String encoding : encodings) {
	            String[] parts = encoding.split("\\s*;\\s*q=\\s*");

	            String scheme = parts[0].trim().toLowerCase();
	            float qValue = 1.0f;

	            if (parts.length > 1) {
	                try {
	                    qValue = Float.parseFloat(parts[1]);
	                } catch (NumberFormatException e) {
	                    // Ignore invalid q-values
	                }
	            }

	            if (scheme.equals("br") && qValue > 0) {
	                byte[] compressed = compressData(inputData, Deflater.BEST_COMPRESSION);
	                compressedData.put("br", compressed);
	            } else if (scheme.equals("gzip") && qValue > 0) {
	                byte[] compressed = compressData(inputData, Deflater.DEFAULT_COMPRESSION);
	                compressedData.put("gzip", compressed);
	            }
	        }
	    }

	    if (compressedData.isEmpty()) {
	        // No encoding schemes were specified or supported, so use default compression
	        byte[] compressed = compressData(inputData, Deflater.DEFAULT_COMPRESSION);
	        compressedData.put("gzip", compressed);
	    }

	    return compressedData;
	}

	private static byte[] compressData(byte[] data, int compressionLevel) {
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    Deflater deflater = new Deflater(compressionLevel);
	    DeflaterOutputStream dos = new DeflaterOutputStream(baos, deflater);

	    try {
	        dos.write(data);
	        dos.close();
	    } catch (Exception e) {
	        // Handle compression errors
	    }

	    return baos.toByteArray();
	}
}
