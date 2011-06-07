/**********************\
  file: Expression file is undefined on line 2, column 11 in Templates/Classes/Class.java.
  package: gui
  author: Nick
  team: NexT
  license: -
  version: 0.1a
\**********************/

package gui;
import java.awt.Dimension;
import java.awt.Frame;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import transcend.Const;

public class DisplayModeChooser extends JDialog implements ActionListener, ItemListener{
    int w=300,h=200;
    HashMap<String,Boolean> fs_capable;
    ArrayList<DisplayMode> dmodes;
    JButton ok,cancel,advanced;
    JComboBox modes;
    JCheckBox vsync,full;
    JLabel lmodes;
    boolean status;
    Const constants;

    public DisplayModeChooser(String title){
        super((Frame)null,title,true);
        setSize(w+15,h+40);
        Dimension dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int)(dim.getWidth()/2)-w/2,(int)(dim.getHeight()/2)-h);
        setLayout(null);
        setResizable(false);

        fs_capable = new HashMap<String,Boolean>();
        dmodes = new ArrayList<DisplayMode>();
        constants = new Const();

        ok = new JButton("Ok");
        cancel=new JButton("Cancel");
        advanced=new JButton("Advanced");
        modes = new JComboBox();
        vsync = new JCheckBox("Enable VSync");
        full = new JCheckBox("Enable Fullscreen");
        lmodes = new JLabel("Display Mode:");

        ok.addActionListener(this);
        cancel.addActionListener(this);
        advanced.addActionListener(this);
        modes.addItemListener(this);

        lmodes.setBounds(15,10,w-20,30);
        modes.setBounds(15,40,w-20,30);

        full.setBounds(15,80,w-20,30);
        vsync.setBounds(15,120,w-20,30);

        ok.setBounds(w/2-150,h-30,100,30);
        cancel.setBounds(w/2-45,h-30,100,30);
        advanced.setBounds(w/2+60,h-30,100,30);

        add(ok);
        add(cancel);
        add(advanced);
        add(modes);
        add(vsync);
        add(full);
        add(lmodes);

        int tosel=0;
        try{
            DisplayMode[] ddmodes = Display.getAvailableDisplayModes();
            for(int i=0;i<ddmodes.length;i++){
                if(ddmodes[i].getFrequency()>=60&&ddmodes[i].getBitsPerPixel()==32){
                    DisplayMode mode = ddmodes[i];
                    String label = mode.getWidth()+"x"+mode.getHeight()+" @"+mode.getFrequency();
                    fs_capable.put(label, mode.isFullscreenCapable());
                    dmodes.add(mode);
                    modes.addItem(label);
                    if(constants.gString("DEFAULT_SCREEN").equals(label))
                        tosel=dmodes.size()-1;
                }
            }
        }catch(Exception ex){ex.printStackTrace();}

        modes.setSelectedIndex(tosel);
        full.setSelected(constants.gBoolean("DEFAULT_FULL"));
        vsync.setSelected(constants.gBoolean("DEFAULT_VSYNC"));

        getRootPane().setDefaultButton(ok);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    }
    
    public boolean getStatus(){
        return status;
    }

    public static boolean showDialog(String title){
        DisplayModeChooser dmc = new DisplayModeChooser(title);
        dmc.setVisible(true);
        return dmc.getStatus();
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Ok")){
            status=true;
            try{
            Display.setDisplayMode(dmodes.get(modes.getSelectedIndex()));
            Display.setFullscreen(full.isSelected());
            Display.setVSyncEnabled(vsync.isSelected());
            }catch(Exception ex){/*Nobody cares.*/}
            constants.sString("DEFAULT_SCREEN",modes.getSelectedItem().toString());
            constants.sBoolean("DEFAULT_FULL",full.isSelected());
            constants.sBoolean("DEFAULT_VSYNC",vsync.isSelected());
            constants.saveRegistry();
            setVisible(false);
        }else if(e.getActionCommand().equals("Advanced")){
            AdvancedDialog adialog = new AdvancedDialog("Advanced Settings");
            adialog.setVisible(true);
        }else{
            status=false;
            setVisible(false);
        }
    }

    public void itemStateChanged(ItemEvent e) {
        if(e.getStateChange()==ItemEvent.SELECTED){
            full.setEnabled(fs_capable.get(e.getItem()));
            if(!full.isEnabled())full.setSelected(false);
        }
    }

    private class AdvancedDialog extends JDialog implements ActionListener{
        private int w=300,h=400;
        private JButton ok,cancel;
        private JScrollPane scroll;
        private JTable list;

        public AdvancedDialog(String title){
            super((Frame)null,title,true);
            setSize(w+15,h+40);
            Dimension dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            setLocation((int)(dim.getWidth()/2)-w/2,(int)(dim.getHeight()/2)-h);
            setLayout(null);
            setResizable(false);

            //create entry list.
            HashMap map = constants.registry;
            String[][] arr = new String[map.size()][2];
            String[] columns = {"Key","Value"};
            Set entries = map.entrySet();
            Iterator entriesIterator = entries.iterator();
            int i = 0;
            while(entriesIterator.hasNext()){
                Map.Entry mapping = (Map.Entry) entriesIterator.next();
                arr[i][0] = mapping.getKey()+"";
                arr[i][1] = mapping.getValue()+"";

                i++;
            }

            ok = new JButton("Ok");
            cancel=new JButton("Cancel");
            list = new JTable(arr,columns);
            scroll = new JScrollPane(list);

            ok.setBounds(w/2-105,h-30,100,30);
            cancel.setBounds(w/2+5,h-30,100,30);
            scroll.setBounds(10,10,w-10,h-50);

            add(ok);
            add(cancel);
            add(scroll);

            ok.addActionListener(this);
            cancel.addActionListener(this);

            getRootPane().setDefaultButton(ok);
            setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        }

        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("Ok")){
                for(int i=0;i<constants.registry.size();i++){
                    constants.registry.put(list.getValueAt(i,0)+"",list.getValueAt(i,1)+"");
                }
                constants.saveRegistry();
            }
            setVisible(false);
        }
    }
}
