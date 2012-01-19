/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: transcend
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.logging.LogRecord;
import java.util.logging.Handler;
import java.util.logging.Level;

public class XLogger extends Handler{
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    PrintWriter pw;
    
    public XLogger(File f) throws UnsupportedEncodingException, FileNotFoundException{
        OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(f),"UTF-8");
        pw = new PrintWriter(fw);
    }

    public String sformat(LogRecord record) {
        if(record.getThrown()==null)
            return sdf.format(record.getMillis())+" ["+record.getLevel().getName()+"]"+record.getMessage();
        else{
            System.out.println(sdf.format(record.getMillis())+" ["+record.getLevel().getName()+"]"+record.getMessage()
                +"\n"+record.getResourceBundleName()+"."+record.getSourceClassName()+"."+record.getSourceMethodName()+": ");
            record.getThrown().printStackTrace();
            return "";
        }
    }

    public String format(LogRecord record) {
        if(record.getLevel()==null)record.setLevel(Level.INFO);
        if(record.getThrown()==null)
            return sdf.format(record.getMillis())+" ["+record.getLevel().getName()+"]"+record.getMessage()
                +"\n"+record.getResourceBundleName()+"."+record.getSourceClassName()+"."+record.getSourceMethodName();
        else
            return sdf.format(record.getMillis())+" ["+record.getLevel().getName()+"]"+record.getMessage()
                +"\n"+record.getResourceBundleName()+"."+record.getSourceClassName()+"."+record.getSourceMethodName()+": "
                +record.getThrown().getMessage();
    }

    @Override
    public void publish(LogRecord record) {
        System.out.println(sformat(record));
        if(pw!=null){
            pw.println(format(record));
            pw.flush();
        }
    }

    public void flush() { pw.flush(); }
    public void close() throws SecurityException { pw.close(); }

}
