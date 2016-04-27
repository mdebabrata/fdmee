package test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Created by deba1 on 4/27/2016.
 */
public class Debug {	
	public  String[] header=null;
	public  String[] periodHeader=null;
	public String[] getHeader(String row) {
		String[] fields = new String[0];		
		String delimiter = ",";
		if ((row.contains("\"")) || (row.contains("'")))
		{
		  String line = new Debug().replaceQuotedDelimiter(delimiter, row);
		  fields = line.split("\\" + delimiter);
		  int index = 0;
		  while (index < fields.length)
		  {
		    fields[index] = fields[index].replaceAll("\\$DELIM", "\\" + delimiter);
		    index++;
		  }
		}
		else
		{
		  fields = row.split(Pattern.quote(delimiter));
		}
		return fields;
	}
	public void displayHeaderInfo(String directoryName,String fileName) {
		String fileEncoding = "UTF-8";
		Charset charEncoding = Charset.forName(fileEncoding);
		File file = new File(directoryName + "/" + fileName);
		try {
			FileInputStream fis = new FileInputStream(file);		
			BufferedInputStream bis = new BufferedInputStream(fis);
			BufferedReader dis = new BufferedReader(new InputStreamReader(bis, charEncoding));
			String[] header=new Debug().getHeader(dis.readLine());
			String[] periodHeader=new Debug().getHeader(dis.readLine());
			this.setHeader(header);
			this.setPeriodHeader(periodHeader);
		} catch (Exception e) {
			System.out.println("Exception in reading file");
		}
	}
	public void setHeader(String[] header) {
		this.header=header;
	}
	public void setPeriodHeader(String[] periodHeader) {
		this.periodHeader=periodHeader;
	}
	
	public String replaceQuotedDelimiter(String delimiter, String text)
	  {
	    StringBuilder line = new StringBuilder(text);
	    //System.out.println("inside replace function");
	    boolean inQuote = false;
	    int index = 0;
	    while (index < line.length())
	    {
	      if ((delimiter.equalsIgnoreCase(Character.toString(line.charAt(index)))) && (inQuote)) {
	        line = line.replace(index, index + 1, "$DELIM");
	      } else if ("\"".equalsIgnoreCase(Character.toString(line.charAt(index)))) {
	        inQuote = !inQuote;
	      }
	      index++;
	    }
	    //System.out.println(line);
	    return line.toString();
	  }
	public int getHeaderIndex(String impFldSourceColumnName,String[] header)
	  {
	    int index = 0;
	    for (String headername : header)
	    {
	      headername = headername.replaceAll("\"", "");
	      if (headername.equalsIgnoreCase(impFldSourceColumnName)) {
	        return index;
	      }
	      index++;
	    }
	    return -1;
	  }
	public static void main(String[] args) {
        System.out.println("***Start of Execution***");        
        String[] fields = new String[0];
        String rowCellValue = null;        
        int fileType=1;        
        String delimiter=",";
        Boolean dataSyncFlag=true;
        int periodHeaderIndex = 0;
        //
        int impFldStartPos=1;
        int impfldlength=1;
        String impfldFieldName="CURKEY";
        String impFldSourceColumnName="Currency";
        String row = "\"FY14\",\"Plan\",\"Working\",\"000\",\"P_000\",\"DSO\",45";
        String[] header={"Year", "Scenario", "Version", "Entity", "Product", "Account", "Period"};
        
        Debug dbg=new Debug();
        String fileName="SampleApp_01.txt";
        String directoryName="C:\\Github\\fdmee\\Exception";
        dbg.displayHeaderInfo(directoryName, fileName);
        System.out.println("HEADER:"+Arrays.toString(dbg.header));
        System.out.println("Header Index:"+Arrays.toString(dbg.periodHeader));
        
        header=dbg.header;
        System.out.println("Input Row:"+row);
        System.out.println("Input Row Length:"+row.length());
        System.out.println("HEADER:"+Arrays.toString(header));
        try {
	        if (fileType == 0) {
	          if (row.length() < impFldStartPos + impfldlength)
	          {
	            if (row.length() >= impFldStartPos) {
	              rowCellValue = row.substring(impFldStartPos - 1);
	              System.out.printf("Row Length:%d,Cell Value:%d",row.length(),rowCellValue);
	            }
	          }
	          else {
	            rowCellValue = row.substring(impFldStartPos - 1, impFldStartPos - 1 + impfldlength);
	            System.out.printf("Row Length:%d,Cell Value:%s",row.length(),rowCellValue);
	          }
	        }
	        else {
	          if ((fields == null) || (fields.length == 0)) {
	            if ((row.contains("\"")) || (row.contains("'")))
	            {
	              String line = new Debug().replaceQuotedDelimiter(delimiter, row);
	              
	              fields = line.split("\\" + delimiter);
	              int index = 0;
	              while (index < fields.length)
	              {
	                fields[index] = fields[index].trim().replaceAll("\\$DELIM", "\\" + delimiter);
	                if ((fields[index] != null) && (fields[index].startsWith("\"")) && (fields[index].endsWith("\""))) {
	                  fields[index] = fields[index].replaceAll("\"", "");
	                }
	                index++;
	              }
	            }
	            else
	            {
	              fields = row.split(Pattern.quote(delimiter));
	            }
	            for(int i=0;i<fields.length;i++) {
	            	System.out.println(i+":"+fields[i]);
	            }
	          } 
	          if (!dataSyncFlag) {
	              rowCellValue = fields[(impFldStartPos - 1)];
	              System.out.println("Case - 01: DataSync: false");
	              System.out.println("RowCellValue:"+rowCellValue);
	            } else if (impfldFieldName.equalsIgnoreCase("AMOUNT")) {
	            	System.out.println("Case - 02: Amount Column");            	
	            	rowCellValue = fields[(header.length + periodHeaderIndex - 1)];
	            	System.out.println("RowCellValue:"+rowCellValue);
	            } else {
	            	System.out.println("Case - 03: Other Columns");
	            	rowCellValue = fields[new Debug().getHeaderIndex(impFldSourceColumnName,header)];
	            	System.out.println("RowCellValue:"+rowCellValue);
	            }
	        }
        } catch (Exception e) {
            System.out.println("Exception while applying " + impfldFieldName + " mapping on the row:[" + row + "]");
          }
    }
}
