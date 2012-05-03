/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: transcend
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend.main;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class XLogger extends Handler{
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    private PrintWriter pw;
    
    public XLogger(File f) throws UnsupportedEncodingException, FileNotFoundException{
        OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(f),"UTF-8");
        pw = new PrintWriter(fw);
    }

    public String sformat(LogRecord record) {
        if(MainFrame.gameLog!=null)
            MainFrame.gameLog.addMessage(sdf.format(record.getMillis())+" ["+MainFrame.mode+"]["+record.getLevel().getName()+"]"+record.getMessage());
        if(record.getThrown()==null)
            return sdf.format(record.getMillis())+" ["+MainFrame.mode+"]["+record.getLevel().getName()+"]"+record.getMessage();
        else{
            System.out.println(sdf.format(record.getMillis())+" ["+MainFrame.mode+"]["+record.getLevel().getName()+"]"+record.getMessage()
                +"\n"+record.getResourceBundleName()+"."+record.getSourceClassName()+"."+record.getSourceMethodName()+": ");
            record.getThrown().printStackTrace();
            return "";
        }
    }

    public String format(LogRecord record) {
        if(record.getLevel()==null)record.setLevel(Level.INFO);
        if(record.getThrown()==null)
            return sdf.format(record.getMillis())+" ["+MainFrame.mode+"]["+record.getLevel().getName()+"]"+record.getMessage();
        else
            return sdf.format(record.getMillis())+" ["+MainFrame.mode+"]["+record.getLevel().getName()+"]"+record.getMessage()
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
