/**********************\
  file: FileStorage.java
  package: transcend
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package transcend;

import NexT.util.SimpleSet;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class FileStorage {
    private SimpleSet<String,File> storage = new SimpleSet<String,File>();
    private HashMap<String,String> fullName = new HashMap<String,String>();
    private ArrayList<File> indexed = new ArrayList<File>();

    public FileStorage(){
        Const.LOGGER.info("[FileStorage] Initial indexing...");
        indexFolder(MainFrame.basedir);
        Const.LOGGER.info("[FileStorage] Done, indexed "+indexed.size()+" files.");
    }

    public File getFile(String name){
        if(name.contains(".")) name = name.substring(0,name.indexOf("."));
        if(name.contains("/")) return storage.get(name);
        else return storage.get(fullName.get(name));
    }
    public int size(){return storage.size();}
    public boolean isIndexed(File folder){return indexed.contains(folder);}

    public void storeFile(String name,File file){
        if(file.isDirectory())return;
        if(name.contains("/"))fullName.put(name.substring(name.indexOf("/")+1), name);
        else                  fullName.put(name,name);
        if(storage.containsValue(file))Const.LOGGER.warning("[FileStorage] Warning: Re-indexing file "+file);
        if(storage.containsKey(name))Const.LOGGER.warning("[FileStorage] Warning: Overwritign file "+file);
        storage.put(name, file);
    }


    public void indexFolder(File folder){
        if(!folder.isDirectory())return;
        if(indexed.contains(folder))return;
        indexed.add(folder);
        File[] contents = folder.listFiles();
        for(int i=0;i<contents.length;i++){
            if(contents[i].isDirectory())indexFolder(contents[i]);
            else storeFile(getFileName(contents[i]), contents[i]);
        }
    }

    public String getFileName(File f){
        String name = f.getName();
        if(name.contains("."))name=name.substring(0,name.indexOf("."));
        return f.getParentFile().getName()+"/"+name;
    }
}
