package cat.uib.secom.multicoupon2d.common.msg.converters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.simpleframework.xml.transform.Matcher;
import org.simpleframework.xml.transform.Transform;


	
public class DateMatcher implements Matcher, Transform<Date> {

      public Transform match(Class type) throws Exception {
         if(type == Date.class) {
            return this;
         }
         return null;
      }

      public Date read(String value) throws Exception {
    	 DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
         return formatter.parse(value);
      }

      public String write(Date value) throws Exception {
    	 DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    	 
         return formatter.format(value);
      }
   }


